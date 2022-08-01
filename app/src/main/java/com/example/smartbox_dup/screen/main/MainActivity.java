package com.example.smartbox_dup.screen.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.SerializableObject;
import com.example.smartbox_dup.network.RetrofitManager;
import com.example.smartbox_dup.utils.ToastManager;
import com.example.smartbox_dup.utils.ViewCreateManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;

public class MainActivity extends AppCompatActivity {

    // components
    private Context context;
    private TextView txt_for_params;
    private TextView txt_title;
    private JsonArray menu_items;
    private ConstraintLayout lo_main;
    private byte[] serializedObj;

    // variable
    Pair<ViewGroup, String> changedLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;

        // components
        lo_main = findViewById(R.id.lo_main);
        txt_title = findViewById(R.id.txt_title);
        txt_for_params = findViewById(R.id.txt_for_params);

        // variable
        changedLayout = Pair.create(lo_main,"");


        try{
            getIntent().getStringExtra("sessionToken");
        } catch(Exception e) {
            ToastManager.getInstance().showToast(this, "sessionToken is empty");
            return ;
        }

        serializedObj = getIntent().getByteArrayExtra("object");
        Log.i("this", "Main: " + String.valueOf(serializedObj));

        ByteArrayInputStream bais = new ByteArrayInputStream(serializedObj);
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object object = ois.readObject();
            SerializableObject obj = (SerializableObject) object;
            Log.i("this", obj.getField1());
            Log.i("this", String.valueOf(obj.getField2()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        getMenuItems();
    }

    private void getMenuItems() {
        new Thread(() ->{
            JsonObject res = RetrofitManager.getInstance().getMenuItems(getIntent().getStringExtra("sessionToken"));
            if(res.get("back4app").getAsString().equals(String.valueOf(RetrofitManager.BACK4APP.FAIL))) {
                ToastManager.getInstance().showToast(context, "요청 실패.");
            } else {
                menu_items = res.get("results").getAsJsonArray();


                for(int i=0; i<menu_items.size();i++) {
                    int finalI = i;
                    runOnUiThread(() -> {
                        String id = ViewCreateManager.getInstance().createView(context, ViewCreateManager.ViewType.BUTTON, lo_main, ViewCreateManager.LayoutType.CONSTRAINTLAYOUT);
                        ((Button)ViewCreateManager.getInstance().getViewById(id)).setText(menu_items.get(finalI).getAsJsonObject().get("title").getAsString());
                        ((Button)ViewCreateManager.getInstance().getViewById(id)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.i("this", "click");
                            }
                        });
                    });
                }


                runOnUiThread(() -> {
                    String id = ViewCreateManager.getInstance().createView(context, ViewCreateManager.ViewType.IMAGEVIEW, lo_main, ViewCreateManager.LayoutType.CONSTRAINTLAYOUT);
                    ((ConstraintLayout.LayoutParams) ViewCreateManager.getInstance().getViewById(id).getLayoutParams()).verticalBias = 0.1f;
                });
            }
        }).start();
    }
}
