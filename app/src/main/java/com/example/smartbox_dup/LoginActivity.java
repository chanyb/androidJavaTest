package com.example.smartbox_dup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.concurrent.TimeUnit;

import com.example.smartbox_dup.network.RetrofitManager;
import com.example.smartbox_dup.network.ruokAPI;
import com.example.smartbox_dup.utils.ActivitySwitchManager;
import com.example.smartbox_dup.viewmodel.SocialLogin;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonObject;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.talk.TalkApiClient;
import com.kakao.sdk.template.model.Link;
import com.kakao.sdk.template.model.TextTemplate;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.common.util.Utility;
import com.navercorp.nid.NaverIdLoginSDK;
import com.navercorp.nid.oauth.OAuthLoginCallback;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tb_login;
    private TextView tb_signup;
    private RelativeLayout lo_naverLogin;
    private RelativeLayout lo_kakaoLogin;
    private SocialLogin mSocialLogin;
    private TextView tv_findId;
    private TextView tv_findPassword;
    private ImageView iv_logo;
    Intent intent;

    private FusedLocationProviderClient fusedLocationClient;

    // test for rxjava
    com.example.smartbox_dup.rxJavaTest rxJavaTest;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.createNotificationChannel();

        tb_login = findViewById(R.id.tb_login);
        tb_signup = findViewById(R.id.tb_signUp);
        lo_naverLogin = findViewById(R.id.lo_naverLogin);
        lo_kakaoLogin = findViewById(R.id.lo_kakaoLogin);
        tv_findId = findViewById(R.id.tv_findId);
        tv_findPassword = findViewById(R.id.tv_findPassword);
        iv_logo = findViewById(R.id.iv_logo);
        tb_login.setOnClickListener(this);
        tb_signup.setOnClickListener(this);
        lo_naverLogin.setOnClickListener(this);
        lo_kakaoLogin.setOnClickListener(this);
        tv_findId.setOnClickListener(this);
        tv_findPassword.setOnClickListener(this);
        iv_logo.setOnClickListener(this);

        intent = new Intent(LoginActivity.this, SignUpActivity1.class);
        mSocialLogin = new ViewModelProvider(this).get(SocialLogin.class);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        String clientId = "OXfEQAJGQjcM2KYgv726";
        String clientSecret = "M3tnQJOpkE";
        String applicationName = "smartbox_dup";

        NaverIdLoginSDK.INSTANCE.initialize(this, clientId, clientSecret, applicationName);
        KakaoSdk.INSTANCE.init(this, "683f8b322937adcc3782060cd9229af2");
        Log.i("this", Utility.INSTANCE.getKeyHash(this));
        Log.i("this", mSocialLogin.getToken());


        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
        }, 0);

        this.checkPermission();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_logo:
                Log.i("this","clicked");
//                getBatteryStatus();
                receiveBroadcast();
                break;
            case R.id.tb_login:
//                ActivitySwitchManager.getInstance().changeActivity(this, NaverMapActivity.class);
//                rxJavaTest = new rxJavaTest(this);
                JsonObject obj = new JsonObject();
                obj.addProperty("username", "1234");
                obj.addProperty("password", "1234");
                RetrofitManager.getInstance().signUp(obj);

                break;
            case R.id.tb_signUp:
                View tb_signUp = findViewById(R.id.tb_signUp);
                ActivitySwitchManager.getInstance().changeActivity(this, SignUpActivity1.class, this, tb_signUp,"signup");
                //ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, tb_signUp,"signup");
                //startActivity(intent, activityOptions.toBundle());
                //startActivity(intent);
                //overridePendingTransition(0,0);
                break;
            case R.id.tv_findId:
                Log.i("this","click!");
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.channel_name))
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("smartbox_dup")
                        .setContentText("Much longer text that cannot fit one line..")
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(0, builder.build());

                break;
            case R.id.tv_findPassword:
                WorkRequest workRequest = new OneTimeWorkRequest.Builder(WakeupWorker.class).setInitialDelay(10, TimeUnit.SECONDS).build();
                WorkManager.getInstance(this).enqueue(workRequest);
                Log.i("this", "Hello");
                break;
            case R.id.lo_naverLogin:
                Log.i("this", "clicked");
                NaverIdLoginSDK.INSTANCE.authenticate(this, naverLoginCallback);
                break;
            case R.id.lo_kakaoLogin:
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(this)) {
                    // 카카오톡을 실행 가능 할 때,
                    UserApiClient.getInstance().loginWithKakaoTalk(this, new Function2<OAuthToken, Throwable, Unit>() {
                        @Override
                        public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                            if (throwable != null) {
                                Log.i("this", throwable.toString());
                            } else {
                                mSocialLogin.setSocialType("kakaoTalk");
                                mSocialLogin.setToken(oAuthToken.getAccessToken());
                            }
                            return null;
                        }
                    });


                }
                break;
        }
    }

    private OAuthLoginCallback naverLoginCallback = new OAuthLoginCallback() {
        @Override
        public void onSuccess() {
            Log.i("this", "success");
        }

        @Override
        public void onFailure(int i, String s) {
            Log.i("this", "fail");
        }

        @Override
        public void onError(int i, String s) {
            Log.i("this", "error");
        }
    };

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(getString(R.string.channel_name), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void getUserLocation() {
        Log.i("this", "getUserLocation...");

        this.checkPermission();
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Log.i("this", String.valueOf(location));
                        }
                    }
                });

    }

    public void checkPermission() {
        String [] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
        };
        int ACCESS_FINE_LOCATION  = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(ACCESS_FINE_LOCATION == PackageManager.PERMISSION_DENIED) {
            Log.i("this", "ACCESS_FINE_LOCATION 권한없음");
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 3);
        }

        int ACCESS_COARSE_LOCATION  = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if(ACCESS_COARSE_LOCATION == PackageManager.PERMISSION_DENIED) {
            Log.i("this", "ACCESS_COARSE_LOCATION 권한없음");
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 3);
        }

        int ACCESS_BACKGROUND_LOCATION  = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        if(ACCESS_BACKGROUND_LOCATION == PackageManager.PERMISSION_DENIED) {
            Log.i("this", "ACCESS_BACKGROUND_LOCATION 권한없음");
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 3);
        }

    }

    public void startForegroundService() {
        startService(new Intent(this, SampleForegroundService.class));
    }

    public void getBatteryStatus() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);

        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        // How are we charging?
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        Log.i("this", String.valueOf(isCharging));
        Log.i("this", String.valueOf(acCharge));
        Log.i("this", String.valueOf(level));
        Log.i("this", String.valueOf(scale));
    }

    public void receiveBroadcast() {
        BroadcastReceiver br = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);

        this.registerReceiver(br, intentFilter);
    }

    public void kakaotalkToMe() {
        UserApiClient.getInstance().me((user, error) -> {
            if(error != null) {
                Log.e("smartbox_dup", error.toString());
            } else {
                Log.i("this", user.getKakaoAccount().getEmail());
            }
            return null;
        });

        Link link = new Link("https://m.naver.com","https://m.naver.com", null, null);
        TextTemplate textTemplate = new TextTemplate("테스트메시지", link, null, "테스트");

        TalkApiClient.getInstance().sendCustomMemo(77282, error -> {
            if(error != null) {
                Log.i("this", "나에게보내기 실패", error);
            } else {
                Log.i("this", "나에게보내기 성공");
            }
            return null;
        });
    }


}