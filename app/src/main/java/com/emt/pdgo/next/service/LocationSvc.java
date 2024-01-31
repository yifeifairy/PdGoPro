package com.emt.pdgo.next.service;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.emt.pdgo.next.common.Common;

/**
 * 定位服务
 * @author wxy
 *
 */
public class LocationSvc extends Service implements LocationListener {

    private LocationManager locationManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Log.e("LocationSvc","onCreate");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        if (locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null) {
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
            locationManager
                    .requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                            this);
        }
        else if (locationManager.getProvider(LocationManager.GPS_PROVIDER) != null) {
            locationManager
                    .requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                            this);
        }
        else {
            Toast.makeText(this, "无法定位", Toast.LENGTH_SHORT).show();
        }



//        if (locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null) locationManager
//                .requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
//                        this);
//        else if (locationManager.getProvider(LocationManager.GPS_PROVIDER) != null) locationManager
//                .requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
//                        this);
//        else Toast.makeText(this, "无法定位", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    @Override
    public void onLocationChanged(Location location) {

        //通知Activity
        Intent intent = new Intent();
        intent.setAction(Common.LOCATION_ACTION);
        intent.putExtra(Common.LOCATION, location.toString());
        sendBroadcast(intent);

        // 如果只是需要定位一次，这里就移除监听，停掉服务。如果要进行实时定位，可以在退出应用或者其他时刻停掉定位服务。
        locationManager.removeUpdates(this);
        stopSelf();
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

}
