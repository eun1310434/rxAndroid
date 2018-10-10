/*==================================================================================================
□ INFORMATION
   ○ Data : 09.Oct.2018
   ○ Mail : eun1310434@gamil.com
   ○ WebPage : https://eun1310434.github.io/
   ○ Reference
      - RxJava 프로그래밍 P221

□ FUNCTION
   ○ 제어흐름

□ Study
   ○

==================================================================================================*/
package com.pandora.rxandroid.activities;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pandora.rxandroid.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;


public class LoopActivity extends AppCompatActivity {
    public static final String TAG = LoopActivity.class.getSimpleName();

    @BindView(R.id.lv_log)
    ListView mLogView;

    @BindView(R.id.tv_title)
    TextView mTitle;


    private Unbinder mUnbinder;
    private LogAdapter mLogAdapter;
    private List<String> mLogs;

    Iterable<String> samples = Arrays.asList("A-b", "B-a", "C-b", "D-a", "E-a", "F-a");


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loop);
        mUnbinder = ButterKnife.bind(this);

        setupLogger();
        setSampleTitle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        mUnbinder = null;
    }


    private void setSampleTitle() {
        mTitle.append(
                Observable
                        .fromIterable(samples)
                        .reduce((r, s) -> (r + "\n") + s) //각각의 리스트를 엔터로 구분하여 정렬
                        .blockingGet()
        );
    }


    @OnClick(R.id.btn_loop)
    void loop() {
        log(">>>>> get an apple :: java");
        for (String s : samples) {
            if (s.contains("A")) {
                log(s);
                break;
            }
        }
    }

    @OnClick(R.id.btn_loop2)
    void loop2() {
        log(">>>>> get an apple :: rx 1.x");

        //rxJava 1.x
        rx.Observable.from(samples)
                .filter(s -> s.contains("A"))
                .firstOrDefault("Not found")
                .subscribe(this::log);
    }


    @OnClick(R.id.btn_loop3)
    void loop3() {
        log(">>>>> get an apple :: rx 2.x");

        // rxJava 2.x
        //Rx2_observableSet_A();//리스트 중 a가 아닌 값은 무시하여 첫번째 것 출력
        //Rx2_observableSet_B();//리스트 중 a가 아닌 값은 무시하여 첫번째 것 출력
        //Rx2_observableSet_C();
        Rx2_observableSet_D();
    }

    public void Rx2_observableSet_A() {
        Observable
                .fromIterable(samples)
                .filter(s -> s.contains("a")) //a가 들어간 값만 출력
                .first("Not found") // a가 들어있는 값중에 첫번째 것만 전달하며 값이 없을시 "Not found"출력
                .subscribe(this::log);
    }

    public void Rx2_observableSet_B() {
        Observable
                .fromIterable(samples)
                .skipWhile(s -> !s.contains("b")) //b가 아닌 값은 무시
                .first("Not found") // a가 들어있는 값중에 첫번째 것만 전달하며 값이 없을시 "Not found"출력
                .subscribe(this::log);
    }

    public void Rx2_observableSet_C() {
        Observable
                .fromIterable(samples)
                .skipWhile(s -> !s.contains("a")) //a가 아닌 값은 무시
                .subscribe(this::log);
    }

    public void Rx2_observableSet_D() {
        Observable
                .fromIterable(samples)
                .filter(s -> s.contains("b")) //b가 들어간 값만 출력
                .subscribe(this::log);
    }


    private void log(String log) {
        mLogs.add(log);
        mLogAdapter.clear();
        mLogAdapter.addAll(mLogs);
    }

    private void setupLogger() {
        mLogs = new ArrayList<>();
        mLogAdapter = new LogAdapter(this, new ArrayList<>());
        mLogView.setAdapter(mLogAdapter);
    }

    private class LogAdapter extends ArrayAdapter<String> {
        public LogAdapter(Context context, List<String> logs) {
            super(context, R.layout.textview_log, R.id.tv_log, logs);
        }
    }


}
