package com.example.smartbox_dup.screen.function.file;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.smartbox_dup.R;

public class FileTest extends AppCompatActivity {
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    Button btn_1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_function_fingerprint);
        init();
        super.onCreate(savedInstanceState);
    }

    private void init() {
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener((view) -> {
            btn_1_action();
        });
    }

    private void btn_1_action() {
        biometricPrompt = new BiometricPrompt(FileTest.this, ContextCompat.getMainExecutor(this), new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                // 지문 인식 실패 횟수가 많은경우 진입, 지문인식을 취소한 경우 진입
                // 이 구역에서 다른 인증 방법을 요청하면 될 것 같다.
                Log.i("this", "onAuthenticationError: " + errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                result.getAuthenticationType();
                if(result.getAuthenticationType() == BiometricPrompt.AUTHENTICATION_RESULT_TYPE_DEVICE_CREDENTIAL) {
                    Log.i("this", "AUTHENTICATION_RESULT_TYPE_DEVICE_CREDENTIAL");
                } else if(result.getAuthenticationType() == BiometricPrompt.AUTHENTICATION_RESULT_TYPE_BIOMETRIC) {
                    // 얼굴인식, 지문인식 - 둘 다 등록되어있다면, 얼굴인식을 진행할 지 지문인식을 진행할 지 선택하는 화면이 나온다.
                    Log.i("this", "AUTHENTICATION_RESULT_TYPE_BIOMETRIC");
                } else if(result.getAuthenticationType() == BiometricPrompt.AUTHENTICATION_RESULT_TYPE_UNKNOWN) {
                    Log.i("this", "AUTHENTICATION_RESULT_TYPE_UNKNOWN");
                } else {
                    Log.i("this", "??");
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.i("this", "onAuthenticationFailed!");
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometic credential")
                .setNegativeButtonText("Use account password")
                .setConfirmationRequired(false)
                .build();

        biometricPrompt.authenticate(promptInfo);
    }
}
