package com.hieutao.smsnotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootUpBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, intent.getAction(), Toast.LENGTH_SHORT).show();

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            Intent serviceIntent = new Intent(context, MyForegroundService.class);
            context.startForegroundService(serviceIntent);
        }
    }
}
