package com.example.smartbox_dup.screen.signup;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.network.RetrofitManager;
import com.example.smartbox_dup.utils.ActivitySwitchManager;
import com.example.smartbox_dup.utils.ToastManager;
import com.google.gson.JsonObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SignUpActivity2 extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private EditText ev_id;
    private TextView tb_idDuplicateCheck;
    private TextView tb_phoneCheck;
    private TextView tb_confirm;
    private Intent intent;
    private ToastManager toastManager;
    private TextView tv_idDuplicateMessage;
    private TextView tv_passwordCheckMessage;
    private TextView tv_phoneCheckMessage;
    private TextView tv_timer;
    private int timer;
    private EditText ev_password;
    private EditText ev_passwordCheck;
    private EditText ev_phone;
    private TelephonyManager tm;
    private ImageView iv_backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigup2);

        this.init();
        this.setOnclickListener();
        this.setOnFocusChangeListener();
    }

    public void init() {
        this.ev_id = findViewById(R.id.ev_id);
        this.tb_idDuplicateCheck = findViewById(R.id.tb_idDuplicateCheck);
        this.tb_phoneCheck = findViewById(R.id.tb_phoneCheck);
        this.tb_confirm = findViewById(R.id.tb_confirm);
        this.toastManager = ToastManager.getInstance();
        this.tv_idDuplicateMessage = findViewById(R.id.tv_idDuplicateMessage);
        this.tv_passwordCheckMessage = findViewById(R.id.tv_passwordCheckMessage);
        this.tv_phoneCheckMessage = findViewById(R.id.tv_phoneCheckMessage);
        this.tv_timer = findViewById(R.id.tv_timer);
        this.ev_password = findViewById(R.id.ev_password);
        this.ev_passwordCheck = findViewById(R.id.ev_passwordCheck);
        this.ev_phone = findViewById(R.id.ev_phone);
        this.iv_backBtn = findViewById(R.id.iv_backBtn);

        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        String[] mRequiredPermissions = new String[]{Manifest.permission.READ_PHONE_NUMBERS};

//        ActivityCompat.requestPermissions(this, mRequiredPermissions, 1);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        } else {
//            String tmp = tm.getLine1Number();
//
//            if(tmp.length() == 13) {
//                tmp = "0"+tmp.substring(3, 13);
//                ev_phone.setText(tmp);
//                ev_phone.setInputType(InputType.TYPE_NULL);
//            } else {
//                ev_phone.setText(tmp);
//                ev_phone.setInputType(InputType.TYPE_NULL);
//            }
//        }

    }

    public void setOnclickListener() {
        this.tb_idDuplicateCheck.setOnClickListener(this);
        this.tb_phoneCheck.setOnClickListener(this);
        this.tb_confirm.setOnClickListener(this);
        this.iv_backBtn.setOnClickListener(this);
    }

    public void setOnFocusChangeListener() {
        ev_passwordCheck.setOnFocusChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tb_idDuplicateCheck:
                tv_idDuplicateMessage.setVisibility(View.VISIBLE);
                break;
            case R.id.tb_phoneCheck:
                tv_timer.setVisibility(View.VISIBLE);

                if(timer!=0) {
                    timer = 180;
                    return ;
                }
                timer = 180;
                this.timer().start();
                break;
            case R.id.tb_confirm:
                JsonObject obj = new JsonObject();
                obj.addProperty("username", ev_id.getText().toString());
                obj.addProperty("password", ev_password.getText().toString());



                Observable<Integer> observable = Observable.just(1)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread());

                Context context = this;

                observable.subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        JsonObject res = RetrofitManager.getInstance().signUp(obj);

                        if(res.get("back4app").getAsString().equals(RetrofitManager.BACK4APP.FAIL.toString())) {
                            // fail
                            ToastManager.getInstance().showToast(context, "서버접속이 원활하지 않습니다.");
                        } else {
                            // success
                            ActivitySwitchManager.getInstance().changeActivity(context, SignUpActivity3.class,false);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("smartbox_dup", "error", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

//                intent = new Intent(this, SignUpActivity3.class);
//                startActivity(intent);
                break;
            case R.id.iv_backBtn:
                this.onBackPressed();
                break;
        }
    }

    public Thread timer() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                while(timer > 1) {
                    try{
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        Log.i("this", e.toString());
                    }
                    timer--;

                    String minute = String.valueOf(timer/60);
                    String second = String.valueOf(timer%60);

                    minute = (minute.length()==2) ? minute:"0"+minute;
                    second = (second.length()==2) ? second:"0"+second;

                    String tmp = minute+":"+second;


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_timer.setText(String.valueOf(tmp));
                        }
                    });
                }
            }
        };

        return thread;
    }

    @Override
    public void onFocusChange(View view, boolean isFocus) {

        switch (view.getId()) {
            case R.id.ev_passwordCheck:
                if(!isFocus) {
                    this.passwordCheck();
                    tv_passwordCheckMessage.setVisibility(View.VISIBLE);

                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                break;
        }

    }

    public void passwordCheck() {
        String tmp1 = ev_password.getText().toString();
        String tmp2 = ev_passwordCheck.getText().toString();

        Pattern passPattern1 = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$");
        Matcher passMatcher1 = passPattern1.matcher(tmp1);

        if (!passMatcher1.find()) {
            tv_passwordCheckMessage.setText("비밀번호는 영문과 특수문자 숫자를 포함하며 8자 이상이어야 합니다.");
            tv_passwordCheckMessage.setTextColor(ContextCompat.getColor(this, R.color.red));
            return ;
        }

        if (tmp1.equals(tmp2)) {
            tv_passwordCheckMessage.setText("사용 가능한 비밀번호입니다.");
            tv_passwordCheckMessage.setTextColor(ContextCompat.getColor(this, R.color.blue));
        } else {
            tv_passwordCheckMessage.setText("비밀번호가 일치하지 않습니다.");
            tv_passwordCheckMessage.setTextColor(ContextCompat.getColor(this, R.color.red));
        }

    }
}