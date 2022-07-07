package com.example.smartbox_dup;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.logging.Handler;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class rxJavaTest {
    public ArrayList<Long> arr;
    public Context context;
    public rxJavaTest(Context context) {
        this.context = context;
        arr = new ArrayList<>();
        for(Long i=0L; i<9999L; i++){
            arr.add(i);
        }

//        editText = ((Activity)context).findViewById(R.id.ev_id);

        Observable<Long> observable = Observable.fromIterable(arr)
                // subscribeOn은 데이터 발행을 하는 스레드이다.
                .subscribeOn(Schedulers.computation())
                // observerOn은 데이터를 소비하는 스레드이다.
                .observeOn(Schedulers.computation());

        observable.subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull Long s) {
//                Log.i("this", "onNext: " + Thread.currentThread().getName());
//                Log.i("this", "onNext: " + s);
                EditText editText = ((Activity)context).findViewById(R.id.ev_id);

                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        editText.setText(String.valueOf(s));
                    }
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(@NonNull Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

}
