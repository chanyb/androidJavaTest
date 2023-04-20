package com.example.smartbox_dup.screen.function.ble;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.screen.function.socket.TCPClient;

public class BleTest extends AppCompatActivity {
    private Context mContext;
    private Button btn_1, btn_2, btn_3;

    private BluetoothAdapter bluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 11;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_function_ble);
        mContext = this;
        init();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            Log.i("this", "asdfasdfafAF");
        }
    }

    private void init() {
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener((view) -> btn_1_action());

        btn_2 = findViewById(R.id.btn_2);
        btn_2.setOnClickListener((view) -> {
        });

        btn_3 = findViewById(R.id.btn_3);
        btn_3.setOnClickListener((view) -> {
        });
    }

    private void btn_1_action() {
        checkPermission();
        setBroadcaseReceiver();
        bleScan();
    }

    private void checkPermission() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // reuqest Permission
        if (Build.VERSION_CODES.S <= Build.VERSION.SDK_INT) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN}, 2);
            if (!bluetoothAdapter.isEnabled()) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN}, 1);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                // 블루투스가 꺼져 있을 때
                Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);
            }
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                // 블루투스가 꺼져 있을 때
                Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);
            }
        }

        Log.i("this", "end");
    }

    private void setBroadcaseReceiver() {
        BroadcastReceiver bleBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    Log.d("this", "Device found: " + device.getName() + ", " + device.getAddress());
                }
            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bleBroadcastReceiver, filter);
    }

    private void bleScan() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        bluetoothAdapter.startDiscovery();
    }
}
