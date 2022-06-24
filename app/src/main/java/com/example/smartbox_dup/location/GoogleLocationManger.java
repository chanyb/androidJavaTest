package com.example.smartbox_dup.location;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlin.jvm.Synchronized;

public class GoogleLocationManger {

    private static FusedLocationProviderClient fusedLocationClient;
    private static GoogleLocationManger instance = new GoogleLocationManger();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Activity activity;
    private Task<Location> task;
    private volatile String thislocation="";

    private GoogleLocationManger() {
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    private static boolean wasSetActivity() {
        if (fusedLocationClient == null) {
            Log.i("this", "(warning) Please Set Activity in GoogleLocationManager");
            return false;
        }

        return true;
    }

    public static GoogleLocationManger getInstance() {
        return instance;
    }

    public Location getUserLocation() {
        if(!wasSetActivity()) {
            return null;
        }

        checkPermission();
        if(task == null) task = fusedLocationClient.getLastLocation();
        while(true) {
            try{
                if(task.isComplete()) break;
            } catch (Exception e) {

            }
        }

        return task.getResult();
    }

    public void checkPermission() {
        String [] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
        };
        int ACCESS_FINE_LOCATION  = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        if(ACCESS_FINE_LOCATION == PackageManager.PERMISSION_DENIED) {
            Log.i("this", "ACCESS_FINE_LOCATION 권한없음");
            ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 3);
        }

        int ACCESS_COARSE_LOCATION  = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        if(ACCESS_COARSE_LOCATION == PackageManager.PERMISSION_DENIED) {
            Log.i("this", "ACCESS_COARSE_LOCATION 권한없음");
            ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 3);
        }

        int ACCESS_BACKGROUND_LOCATION  = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        if(ACCESS_BACKGROUND_LOCATION == PackageManager.PERMISSION_DENIED) {
            Log.i("this", "ACCESS_BACKGROUND_LOCATION 권한없음");
            ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 3);
        }

    }
}
