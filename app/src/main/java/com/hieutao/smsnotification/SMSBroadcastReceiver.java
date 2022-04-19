package com.hieutao.smsnotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SMSBroadcastReceiver extends BroadcastReceiver {

    Bundle bundle;
    SmsMessage currentSMS;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            bundle = intent.getExtras();
            if (bundle != null) {
                Object[] objects = (Object[]) bundle.get("pdus");
                if (objects != null) {
                    String uniqueId = SharedPreferencesManager.getUniqueId(context);
                    if (uniqueId != null) {
                        for (Object obj : objects) {
                            currentSMS = getIncomingMessage(obj, bundle);
                            String senderNo = currentSMS.getOriginatingAddress();
                            String message = currentSMS.getDisplayMessageBody();
                            String slot = detectSim(bundle);

                            Map<String, String> params = new HashMap<String, String>();

                            params.put("UniqueId", uniqueId);
                            params.put("Sim", slot);
                            params.put("Name", senderNo);
                            params.put("Content", message);

                            post(context, params);

                            //Toast.makeText(context, "sender: " + senderNo + "\nMessage: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    this.abortBroadcast();
                }
            }
        }
    }

    private SmsMessage getIncomingMessage(Object aObject, Bundle bundle) {
        String format = bundle.getString("format");
        return SmsMessage.createFromPdu((byte[]) aObject, format);
    }

    // https://stackoverflow.com/questions/35968766/how-to-figure-out-which-sim-received-sms-in-dual-sim-android-device
    private String detectSim(Bundle bundle) {
        int slot = -1;
        Set<String> keySet = bundle.keySet();
        for (String key : keySet) {
            switch (key) {
                case "phone":
                    slot = bundle.getInt("phone", -1);
                    break;
                case "slot":
                    slot = bundle.getInt("slot", -1);
                    break;
                case "simId":
                    slot = bundle.getInt("simId", -1);
                    break;
                case "simSlot":
                    slot = bundle.getInt("simSlot", -1);
                    break;
                case "slot_id":
                    slot = bundle.getInt("slot_id", -1);
                    break;
                case "simnum":
                    slot = bundle.getInt("simnum", -1);
                    break;
                case "slotId":
                    slot = bundle.getInt("slotId", -1);
                    break;
                case "slotIdx":
                    slot = bundle.getInt("slotIdx", -1);
                    break;
                default:
                    if (key.toLowerCase().contains("slot") | key.toLowerCase().contains("sim")) {
                        String value = bundle.getString(key, "-1");
                        if (value.equals("0") | value.equals("1") | value.equals("2")) {
                            slot = bundle.getInt(key, -1);
                        }
                    }
            }
        }

        return String.valueOf(slot);
    }

    private void post(Context context, Map<String, String> params) {
        new Thread(() -> {
            try {


                // code here...


            } catch (Exception e) {
                Log.e("SMSNotification", e.getMessage());
                new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

}
