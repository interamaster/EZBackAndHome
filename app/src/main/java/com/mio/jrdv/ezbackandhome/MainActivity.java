package com.mio.jrdv.ezbackandhome;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    //copiado de:https://androidmads.blogspot.com.es/2016/05/how-create-accessibility-service-in.html
    //v001 vesion inicial de la wweb con iconos ok

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Permission for Manifest.permission.SYSTEM_ALERT_WINDOW in Android M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1);
            }
        }

        //TODO ask for ACCESIBILITY Y ADMIN!!


        // Code to start the Service
        startService(new Intent(getApplication(), EZBackHomeService.class));
    }
}
