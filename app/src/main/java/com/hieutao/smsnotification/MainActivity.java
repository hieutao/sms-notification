package com.hieutao.smsnotification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private int MY_PERMISSIONS_REQUEST_SMS_RECEIVE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();
        setupAction();

        startForegroundService();

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECEIVE_SMS},
                MY_PERMISSIONS_REQUEST_SMS_RECEIVE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_SMS_RECEIVE) {
            Toast.makeText(getApplicationContext(), "Allow receive sms", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("deprecation")
    boolean foregroundServiceRunning(){
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if (MyForegroundService.class.getName().equals(service.service.getClassName())){
                return true;
            }
        }

        return false;
    }

    void startForegroundService(){
        if (foregroundServiceRunning() == false){
            Intent intent = new Intent(this, MyForegroundService.class);
            startForegroundService(intent);
        }
    }

    void stopForegroundService(){
        Intent intent = new Intent(this, MyForegroundService.class);
        stopService(intent);
    }

    private void setupUI(){
        TextView tvUnique = (TextView) findViewById(R.id.tvUnique);

        String uniqueId = SharedPreferencesManager.getUniqueId(getApplicationContext());
        if (uniqueId == null){
            String newUniqueId =  UUID.randomUUID().toString();
            tvUnique.setText(newUniqueId);
            SharedPreferencesManager.setUniqueId(getApplicationContext(),newUniqueId);
        }else{
            tvUnique.setText(uniqueId);
        }
    }

    private void setupAction(){
        Button btnCopy = (Button) findViewById(R.id.btnCopy);

        btnCopy.setOnClickListener(view -> {
//            TextView tvUnique = (TextView) findViewById(R.id.tvUnique);

//            String newUniqueId =  UUID.randomUUID().toString();
//            tvUnique.setText(newUniqueId);
//            SharedPreferencesManager.setUniqueId(getApplicationContext(),newUniqueId);

            String uniqueId = SharedPreferencesManager.getUniqueId(getApplicationContext());

            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("unique_id", uniqueId);
            clipboard.setPrimaryClip(clip);
        });
    }
}