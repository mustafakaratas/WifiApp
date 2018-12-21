package com.example.mustafakaratas.a181220wifiapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    private RecyclerView rv;
    private Button btnYenile;
    private ArrayList<String> arrayList = new ArrayList<>();
    private MyAdapter adapter;
    private int signalLevel;

    private String[] necessaryPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private static final int PERMISSION_ALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnYenile = findViewById(R.id.btnYenile);
        btnYenile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanWifi();
            }
        });
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        rv = findViewById(R.id.rvWifiList);
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(getApplicationContext(), "Wifi is disabled..Making it enabled.", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }
        adapter = new MyAdapter(this, arrayList);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(lm);

        if (hasPermissions(necessaryPermissions)) {
            scanWifi();
        } else {
            ActivityCompat.requestPermissions(this, necessaryPermissions, PERMISSION_ALL);
        }
    }

    private boolean hasPermissions(String[] permissions) {
        if (permissions != null) {
            for (String p : permissions) {
                if (ActivityCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            scanWifi();
        } else {
            showMessage("Permissions is not granted");
        }
    }

    public void scanWifi() {
        arrayList.clear();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        showMessage("Scanning...");
    }

    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                List<ScanResult> mScanResults = wifiManager.getScanResults();

                for (ScanResult scanResult : mScanResults) {
                    signalLevel = wifiManager.calculateSignalLevel(scanResult.level, 5);
                    arrayList.add("" + scanResult.SSID + " - " + signalStrength(signalLevel) + " - " + scanResult.BSSID);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiReceiver);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private String signalStrength(int signalLevel) {
        String result = "";
        switch (signalLevel) {
            case 0:
                result = "Very Low";
                break;
            case 1:
                result = "Low";
                break;
            case 2:
                result = "Medium";
                break;
            case 3:
                result = "High";
                break;
            case 4:
                result = "Very High";
                break;
        }
        return result;
    }
}
