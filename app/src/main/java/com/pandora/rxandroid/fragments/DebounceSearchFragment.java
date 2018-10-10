package com.pandora.rxandroid.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.pandora.rxandroid.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;


public class DebounceSearchFragment extends Fragment {
    public static final String TAG = DebounceSearchFragment.class.getSimpleName();

    @BindView(R.id.dsf_lv_log)
    ListView mLogView;
    @BindView(R.id.dsf_input_deb_search)
    EditText mSearchBox;

    private Unbinder mUnbinder;
    private List<String> dataList = Arrays.asList(
            "A-a", "B-a", "C-a", "D-a", "E-a",
            "A-b", "B-b", "C-b", "D-b", "E-b",
            "A-c", "B-c", "C-c", "D-c", "E-c",
            "A-d", "B-d", "C-d", "D-d", "E-d",
            "A-e", "B-e", "C-e", "D-e", "E-e");

    private SearchAdapter mSearchAdapter;
    private List<String> searchResult;


    private HistoryAdapter mHistoryAdapter;
    private List<String> searchHistory;
    private Disposable mDisposable;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_debounce_search, container, false);
        mUnbinder = ButterKnife.bind(this, layout);
        //setSearchHistory();
        setSearchResult();
        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        mDisposable.dispose();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //basic();
        rxTextView();
    }

    public void basic() {
        mDisposable = getObservable()
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter(str -> !TextUtils.isEmpty(str))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver());
    }

    private Observable<CharSequence> getObservable() {
        return Observable.create(
                emitter -> mSearchBox.addTextChangedListener(
                        new TextWatcher() {
                            @Override
                            public void beforeTextChanged(
                                    CharSequence charSequence,
                                    int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(
                                    CharSequence s, // 사용자가 새로 입력한 문자열의 포함하는 EditText 객체의 문자열
                                    int start, // 새로 추가된 문자열의 시작 위칫값
                                    int before, // 새 문자열 대신 삭제된 기존 문자열의 수
                                    int count// 새로 추가된 문자열의 수
                            ) {
                                emitter.onNext(s); // 발행자(emitter)는 onNext() 함수로 변경된 문자열을 입력 받음
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        }));
    }

    private DisposableObserver<CharSequence> getObserver() {
        return new DisposableObserver<CharSequence>() {
            @Override
            public void onNext(CharSequence word) {
                addSearchHistory("Search " + word.toString());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    public void rxTextView() {
        //RxTextView를 활용하여 문자가 바뀌는것만 확인하여 Observable을 생성
        mDisposable = RxTextView.textChangeEvents(mSearchBox) // getObservable()와 같음
                .debounce(100, TimeUnit.MILLISECONDS)// 0.1초 뒤에 작동
                .filter(str -> !TextUtils.isEmpty(str.text().toString())) // str이 empty가 아니면 실행
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserverLib());
    }

    // with RxView Libs
    private DisposableObserver getObserverLib() {
        return new DisposableObserver<TextViewTextChangeEvent>() {
            @Override
            public void onNext(TextViewTextChangeEvent view) {
                //addSearchHistory("Search " + view.text().toString());
                getSearchResult(view.text().toString());
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };
    }

    private void setSearchResult() {
        searchResult = new ArrayList<>();
        mSearchAdapter = new SearchAdapter(getActivity(), new ArrayList<>());
        mLogView.setAdapter(mSearchAdapter);
    }


    private void getSearchResult(String search) {
        Observable
                .fromIterable(dataList)
                .filter(s -> s.contains(search)) //b가 들어간 값만 출력
                .subscribe(new DisposableObserver<String>() {
                               @Override
                               public void onNext(String s) {
                                   searchResult.add(s);
                               }

                               @Override
                               public void onError(Throwable e) {

                               }

                               @Override
                               public void onComplete() {
                                   mSearchAdapter.clear();
                                   mSearchAdapter.addAll(searchResult);
                                   searchResult.clear();
                               }
                           }
                );
    }


    private void addSearchHistory(String search) {
        searchHistory.add(search);
        mHistoryAdapter.clear();
        mHistoryAdapter.addAll(searchHistory);
    }

    private void setSearchHistory() {
        searchHistory = new ArrayList<>();
        mHistoryAdapter = new HistoryAdapter(getActivity(), new ArrayList<>());
        mLogView.setAdapter(mHistoryAdapter);
    }

    class SearchAdapter extends ArrayAdapter<String> {
        public SearchAdapter(Context context, List<String> logs) {
            super(context, R.layout.textview_log, R.id.tv_log, logs);
        }
    }

    class HistoryAdapter extends ArrayAdapter<String> {
        public HistoryAdapter(Context context, List<String> logs) {
            super(context, R.layout.textview_log, R.id.tv_log, logs);
        }
    }
}
