package com.mio.jrdv.ezbackandhome;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    //copiado de:https://androidmads.blogspot.com.es/2016/05/how-create-accessibility-service-in.html
    //v001 vesion inicial de la wweb con iconos ok
    //v04 añadido panel zephyr ok en negro y funcoines de back y home ok..12/4/17





    String apkname4AccesibilityService="com.mio.jrdv.ezbackandhome/.EZBackHomeService";
    String LOGTAG="INFO";

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


        //2ºº)chequeamos si la accesibility esta habilitada:
       boolean acssebilityEnable=  isAccessibilityEnabled();

        Log.d("info", "esta ahabilitgada ya: "+acssebilityEnable);





        if (!acssebilityEnable){
            //1º)si no esta habilitada aun le decimos que lo habilite:


            Toast.makeText(getApplicationContext(), "TIENES QUE HABILITAR SI O SI EL ACESIBILITY!!", Toast.LENGTH_SHORT).show();


            Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
             startActivityForResult(intent, 99);

        }

        else{


            //si se habilito..empieza el service
            startService(new Intent(getApplication(), EZBackHomeService.class));
        }



        // Code to start the Service//OJO solo se puede empezar el service cunado tengamos permisod e Accesibility!!
        //o da crash:  Caused by: android.view.WindowManager$BadTokenException: Unable to add window android.view.ViewRootImpl$W@1232b61ç
        // -- permission denied for this window type

       // startService(new Intent(getApplication(), EZBackHomeService.class));
    }



    public boolean isAccessibilityEnabled(){
        int accessibilityEnabled = 0;
        final String LIGHTFLOW_ACCESSIBILITY_SERVICE = "com.example.test/com.example.text.ccessibilityService";
        boolean accessibilityFound = false;
        try {
            accessibilityEnabled = Settings.Secure.getInt(this.getContentResolver(),android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.d(LOGTAG, "ACCESSIBILITY: " + accessibilityEnabled);
        }
        catch (Settings.SettingNotFoundException e) {
            Log.d(LOGTAG, "Error finding setting, default accessibility to not found: " + e.getMessage());
        } TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
        if (accessibilityEnabled==1){
            Log.d(LOGTAG, "***ACCESSIBILIY IS ENABLED***: ");
            accessibilityFound=true;
            String settingValue = Settings.Secure.getString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            Log.d(LOGTAG, "Setting: " + settingValue);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = mStringColonSplitter; splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessabilityService = splitter.next();
                    Log.d(LOGTAG, "Setting: " + accessabilityService);
                    if (accessabilityService.equalsIgnoreCase(apkname4AccesibilityService)){
                        Log.d(LOGTAG, "We've found the correct setting - accessibility is switched on!");
                        return true; }
                }
            }
            Log.d(LOGTAG, "***END***");
        } else{
            Log.d(LOGTAG, "***ACCESSIBILIY IS DISABLED***");
        }
        return accessibilityFound;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //el reqquestCode es 99(el que llmamamos desde el intent!!!

        if (requestCode==99){

            //volvemos a chequear que se activo:



            //2ºº)chequeamos si la accesibility esta habilitada:
            boolean acssebilityEnable=  isAccessibilityEnabled();

            Log.d("info", "esta ahabilitgada ya: "+acssebilityEnable);


            if (!acssebilityEnable){
                //1º)si no esta habilitada aun le decimos que lo habilite:
                Toast.makeText(getApplicationContext(), "TIENES QUE HABILITAR SI O SI EL ACESIBILITY!!", Toast.LENGTH_SHORT).show();


                 Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
               startActivityForResult(intent, 99);

            }

            else {
                //si se habilito..empieza el service

                startService(new Intent(getApplication(), EZBackHomeService.class));


            }

        }

    }



    }
