package com.example.smartbox_dup.screen.function.intent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.screen.function.SerializableClass;
import com.example.smartbox_dup.utils.ByteArrayManager;


public class IntentTest extends AppCompatActivity {
    Button btn_1, btn_2, btn_3, btn_4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_intent);
        init();
    }

    private void init() {
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener((view) -> {
            btn_1_action();
        });

        btn_2 = findViewById(R.id.btn_2);
        btn_2.setOnClickListener((view) -> {

        });

        btn_3 = findViewById(R.id.btn_3);
        btn_3.setOnClickListener((view) -> {

        });

        btn_4 = findViewById(R.id.btn_4);
        btn_4.setOnClickListener((view) -> {

        });
    }

    private void btn_1_action() {
        ByteArrayManager<SerializableClass> HumanToBytes = new ByteArrayManager<>();
        SerializableClass human = new SerializableClass("nm", 30, "addr", 3);
        Intent A = new Intent();
        Intent B = new Intent();

        A.putExtra("a", "A");
        A.putExtra("b", "b");
        A.putExtra("c", -1);
        A.putExtra("d", 1);
        A.putExtra("e", 0);
        A.putExtra("f", false);
        A.putExtra("g", true);
        A.putExtra("h", HumanToBytes.getByteArrayFromClassObject(human));

//        IntentManager.getInstance().copyExtras(A, B);

        for (String key : B.getExtras().keySet()) {
            Log.i("this", String.valueOf(B.getExtras().get(key)));
        }


    }
}
