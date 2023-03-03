package com.example.smartbox_dup.screen.function.location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.utils.PermissionManager;

/**
 * GPS_PROVIDER는 실내에서는 잘 작동하지 않는다.
 * Request를 해도, 응답이 오지 않아서 해당 기능을 사용할 때 Timeout을 설정할 필요가 있다.
 */
public class LocationTest extends AppCompatActivity {
    Button btn_1, btn_2, btn_3;
    TextView txt_location;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_location);
        init();
    }

    private void init() {
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener((view) -> {
            btn_1_action();
        });

        btn_2 = findViewById(R.id.btn_2);
        btn_2.setOnClickListener((view) ->{
            btn_2_action();
        });

        btn_3 = findViewById(R.id.btn_3);
        btn_3.setOnClickListener((view) -> {
            btn_3_action();
        });

        txt_location = findViewById(R.id.txt_location);
    }

    private void btn_1_action() {
        PermissionManager.getInstance().requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i("this", "??");
            return;
        }

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.i("this", "disabled");
            return ;
        } else {
            Log.i("this", "Enable");
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.i("this", location.toString());
                locationManager.removeUpdates(this);
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                Log.i("this", "onProviderDisabled");
                LocationListener.super.onProviderDisabled(provider);
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                Log.i("this", "onProviderEnabled");
                LocationListener.super.onProviderEnabled(provider);
            }
        });
    }

    private void btn_2_action() {
        Log.i("this", "btn_2_action");
        PermissionManager.getInstance().requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.i("this", "requestLocationUpdates");
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 30 * 1, 5, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.i("this", location.toString());
                runOnUiThread(() -> txt_location.setText(location.toString()));
                locationManager.removeUpdates(this);
            }
        });
    }

    public void btn_3_action() {
        PermissionManager.getInstance().requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i("this", "??");
            return;
        }

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.i("this", "disabled");
            return ;
        } else {
            Log.i("this", "Enable");
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Log.i("this", location.toString());
        Toast.makeText(this, location.getLatitude() + " / " + location.getLongitude(), Toast.LENGTH_LONG).show();
        txt_location.setText(location.getLatitude() + " / " + location.getLongitude());
    }
}
