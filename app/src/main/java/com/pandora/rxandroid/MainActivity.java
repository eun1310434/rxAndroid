/*==================================================================================================
□ INFORMATION
   ○ Data : 09.Oct.2018
   ○ Mail : eun1310434@gamil.com
   ○ WebPage : https://eun1310434.github.io/
   ○ Reference
      - RxJava 프로그래밍 P205

□ FUNCTION
   ○

□ Study
   ○ 안드로이드에서 사용할 수 있는 리액티브 API와 라이브러리 목록
      - RxLifecycle : RxJava를 사용하는 안드로이드 앱용 라이프 사이클 처리 API
      - RxBinding : 안드로이드 UI 위젯용 RxJava 바인딩 API
      - SqlBrite : SQLiteIpenHelper와 CntentResolver클래스의 래퍼(wrapper)클래스로 쿼리에 리액티브 스트림을 도입
      - RxLocation : 안드로이드용 리액티브 위치 API 라이브러리
      - rx-pregernces : 안드로이드용 리액티브 SharedPregerences 인터페이스
      - RxFit : 안드로이드용 리액티브 웨어러블 API 라이브러리
      - RxWear : 안드로이드용 리액티브 웨어러블 API 라이브러리
      - RxPermissions : RxJava에서 제공하는 안드로이드 런타임 권한 라이브러리
      - RxNotification : RxJava로 일림(notification)을 관리하는 API
      - RxClipboard : 안드로이드 클립 보드용 RxJava 바인딩 API
      - RxBroadcast : 안드로이드 Broadcast 및 LocalBroadcast에 관한 RxJava 바인딩 API
      - RxAndroidBle : 블루투스 LE(Bluetooth)
      - RxImagePicker : 갤러리 또는 카메라에서 이미지를 선택하기 위한 리액티브 라이브러리
      - ReactiveNetwork : 네크워크 연결 상태나 인테넷 연결 상태를 확인하는 리액티브 라이브러리
      - ReactiveBeacons : 주변에 있는 블루투스 LE 기반의 비컨을 수신하는 리액티브 라이브러리
      - RxDataBinding : 안드로이드 데이터 바인딩 라이브러리용 RxJava2 바인딩 API


==================================================================================================*/
package com.pandora.rxandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pandora.rxandroid.fragments.MainFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new MainFragment(), MainFragment.TAG)
                    //.replace(android.R.id.content, new RecyclerViewFragment(), RecyclerViewFragment.TAG)
                    //.replace(android.R.id.content, new DebounceSearchFragment(), DebounceSearchFragment.TAG)
                    .commit();

        }
    }
}
