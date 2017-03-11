package com.rmiralles.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Example Using BradcastReceiver Capturing BatteryManager
 *
 * @since 2016
 * @author rmiralles
 */
public class MainActivity extends Activity {

    private TextView mPlugged;
    private TextView mStatus;
    private TextView mLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlugged = (TextView) findViewById(R.id.plugged_text);
        mStatus  = (TextView) findViewById(R.id.status_text);
        mLevel   = (TextView) findViewById(R.id.level_text);

        registerReceiver(batterReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private BroadcastReceiver batterReceiver = new BroadcastReceiver() {
        Intent intent = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            this.intent = intent;

            mPlugged.setText(getPlugged());
            mStatus .setText(getStatus());
            mLevel  .setText(getLevel());
        }

        /**
         * Class to capture EXTRA_PLUGGED from BatteryManager
         * @return plugged
         */
        public String getPlugged() {
            int pluggedExtra = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            String  plugged  = "";

            switch (pluggedExtra) {
                case BatteryManager.BATTERY_PLUGGED_AC:
                    plugged  = "Conectado a la corriente";
                    break;
                case BatteryManager.BATTERY_PLUGGED_USB:
                    plugged  = "Conectado por usb";
                    break;
                default:
                    plugged  = "Desconectado";
                    break;
            }

            return plugged;
        }

        /**
         * Class to capture EXTRA_STATUS from BatteryManager
         * @return status
         */
        public String getStatus() {
            int    statusExtra = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            String status      = "";

            switch (statusExtra) {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    status = "Cargando";
                    break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    status = "Desconectado";
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:
                    status = "Completo";
                    break;
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                    status = "No esta cargando";
                    break;
                case BatteryManager.BATTERY_STATUS_UNKNOWN:
                    status = "Desconocido";
                    break;
                default:
                    break;
            }
            return status;
        }

        /**
         * Level: Battery charge
         * scale: Total battery
         * @return Battery percentage
         */
        public String getLevel() {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            if (level == -1 ||
                scale == -1) {
                return "0%";
            }

            return (((float)level /
                     (float)scale) * 100.0f) + "%";
        }
    };
}