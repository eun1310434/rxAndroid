/*==================================================================================================
□ INFORMATION
   ○ Data : 09.Oct.2018
   ○ Mail : eun1310434@gamil.com
   ○ WebPage : https://eun1310434.github.io/
   ○ Reference
      - RxJava 프로그래밍 P212

□ FUNCTION
   ○ RxLifecycle 라이브러리
      - 안드로이드의 Activity와 Fragment의 라이프 사이클을 RxJava에서 사용할 수 있게함
      - Subscriptions을 할 때 발생할 수 있는 메모리 누수를 방지하기 위해 사용하며,
        완료하지 못한 구독은 자동으로 해제(dispose)
      - RxLifecycle 라이브 사이클 컴포넌트
        01) RxActivity : 액티비티에 대응
        02) RxDialogFragment : Native/Support 라이브러리인 DialogFragment에 대응
        03) RxFragment : Native/Support 라이브러리인 Fragment에 대응
        04) RxAppCompatActivity : Support 라이브러리 AppCompatActivity에 대응
        05) RxAppCompatDialogFragment : Support 라이브러리 AppCompatDialogFragment에 대응합니다.
        06) RxFragmentActivity : Support 라이브러리 FragmentActivity에 대응합니다.
      - RxLifecycle 라이브러리 사용을 위한 환경 설정(app/build.gradle)
        // rxlifecycle 2.x
        compile 'com.trello.rxlifecycle2:rxlifecycle:2.1.0'
        compile 'com.trello.rxlifecycle2:rxlifecycle-android:2.1.0'
        compile 'com.trello.rxlifecycle2:rxlifecycle-components:2.1.0'


□ Study
   ○

==================================================================================================*/
package com.pandora.rxandroid.activities;

import android.os.Bundle;
import android.widget.TextView;

import com.pandora.rxandroid.R;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;

public class HelloActivityV3 extends RxAppCompatActivity { // 상속을 RxAppCompatActivity클래스를 만듬
    public static final String TAG = HelloActivityV3.class.getSimpleName();

    @BindView(R.id.textView) TextView textView;

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUnbinder = ButterKnife.bind(this);

        Observable.just("Hello, rx world!")
                .compose(bindToLifecycle())
                // 라이프 사이클을 관리하도록 compose를 사용하여 추가.
                // Activity가 종료되면 자동으로 dispose됨.
                // 물론 lifecycle을 따라 Stop()에 dispose를 호출 할 수도있다.
                .subscribe(textView::setText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}
