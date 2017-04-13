package com.mio.jrdv.ezbackandhome;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    //copiado de:https://androidmads.blogspot.com.es/2016/05/how-create-accessibility-service-in.html
    //v001 vesion inicial de la wweb con iconos ok
    //v04 añadido panel zephyr ok en negro y funcoines de back y home ok..12/4/17
    //V055 AÑADIDO CHEQUEO Y OBLIGACION DE ACTIVAR ACESIBILITY SI O SI)




    String apkname4AccesibilityService="com.mio.jrdv.ezbackandhome/com.mio.jrdv.ezbackandhome.EZBackHomeService";

    /*
    To find out the ID of your and all the installed accessibility services, you can run the following code snippet:

    public static void logInstalledAccessiblityServices(Context context) {

    AccessibilityManager am = (AccessibilityManager) context
            .getSystemService(Context.ACCESSIBILITY_SERVICE);

    List<AccessibilityServiceInfo> runningServices = am
            .getInstalledAccessibilityServiceList();
    for (AccessibilityServiceInfo service : runningServices) {
        Log.i(TAG, service.getId());
    }
}
     */
    String LOGTAG="INFO";

    //para los switchs

    Switch switcSERVICEENABLEButton,swtichPANELINBLACK;

    //para el accesibility

    private static final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Boolean SERVICEENABLED = Myapplication.preferences.getBoolean(Myapplication.PREF_BOOL_SERVICEENABLED,false);//por defecto vale 0){



        Log.d("INFO","PREF_BOOL_SERVICEENABLED: "+SERVICEENABLED);


        Boolean  PANELINBLACK = Myapplication.preferences.getBoolean(Myapplication.PREF_BOOL_PANELINBLACK,false);//por defecto vale 0){

        Log.d("INFO","PREF_BOOL_PANELINBLACK: "+PANELINBLACK);




        setContentView(R.layout.activity_main);
        // For first switch button
        switcSERVICEENABLEButton = (Switch) findViewById(R.id.switch1);

        swtichPANELINBLACK = (Switch) findViewById(R.id.switch2);



        if ( SERVICEENABLED) {




                //1ºº)chequeamos si la accesibility esta habilitada de verdad
                boolean acssebilityEnable=  isAccessibilityEnabled();

                Log.d("info", "esta ahabilitgada ya: "+acssebilityEnable);

            if (acssebilityEnable) {



                switcSERVICEENABLEButton.setChecked(true);
               // switcSERVICEENABLEButton.setClickable(false);
            }
        }

        else {


            //en relaidad no estaba!!
            Myapplication.preferences.edit().putBoolean(Myapplication.PREF_BOOL_SERVICEENABLED, false).commit();

            switcSERVICEENABLEButton.setChecked(false);
        }




        //1ºB)si se leigio panel in Black

        if ( PANELINBLACK) {

            swtichPANELINBLACK.setChecked(true);


        }
        else{
            swtichPANELINBLACK.setChecked(false);


        }



        //To hide AppBar for fullscreen.
        ActionBar ab = getSupportActionBar();
        ab.hide();



        //LISTENER DEL PANEL IN BLACK

        swtichPANELINBLACK.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {

                    Log.d("INFO","PREF_BOOL_PANELINBLACK: "+bChecked);


                    Myapplication.preferences.edit().putBoolean(Myapplication.PREF_BOOL_PANELINBLACK, bChecked).commit();



                } else {


                    Log.d("INFO","PREF_BOOL_PANELINBLACK: "+bChecked);




                    Myapplication.preferences.edit().putBoolean(Myapplication.PREF_BOOL_PANELINBLACK, bChecked).commit();

                }
            }
        });


        //LISTENER DEL ACCESIBILITY SERVICE ENABLE
        //NO HAY O ESA O NO HABILITADO


        switcSERVICEENABLEButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {


                    //antes de habilitarlo hayq ue conseguir permiso de sobreescribir pantalla
                    chequeasobreescribirpantalla();

                    Log.d("INFO","PREF_BOOL_SERVICEENABLED: "+bChecked);


                    Myapplication.preferences.edit().putBoolean(Myapplication.PREF_BOOL_SERVICEENABLED, true).commit();

                    Toast.makeText(getApplicationContext(), "YOU HAVE TO ENABLE ME TO WORK!!", Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivityForResult(intent, REQUEST_CODE);


                } else {


                    Log.d("INFO","PREF_BOOL_SERVICEENABLED: "+bChecked);


                    Myapplication.preferences.edit().putBoolean(Myapplication.PREF_BOOL_SERVICEENABLED, false).commit();



                    Toast.makeText(getApplicationContext(), "IF DISABLE THIS WILL OT WORK ANYMORE!!", Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
        });






    }

    private void chequeasobreescribirpantalla() {

        // Permission for Manifest.permission.SYSTEM_ALERT_WINDOW in Android M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1);
            }
        }
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
            //accessibilityFound=true;
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

        //el reqquestCode es 0(el que llmamamos desde el intent!!!




            if(REQUEST_CODE == requestCode)
            {

         //volvemos a chequear que se activo:

          //2ºº)chequeamos si la accesibility esta habilitada:
            boolean acssebilityEnable=  isAccessibilityEnabled();

            Log.d("info", "esta ahabilitgada ya: "+acssebilityEnable);


            if (!acssebilityEnable){
                //1º)si no esta habilitada aun le decimos que lo habilite:
                Toast.makeText(getApplicationContext(), "YOU HAVE TO ENABLE ME TO WORK!!!!", Toast.LENGTH_SHORT).show();

                    switcSERVICEENABLEButton.setChecked(false);

                Myapplication.preferences.edit().putBoolean(Myapplication.PREF_BOOL_SERVICEENABLED, false).commit();

               //  Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
             //  startActivityForResult(intent, REQUEST_CODE);

            }

            else {
                //si se habilito..empieza el service

               // switcSERVICEENABLEButton.setChecked(true);

                //startService(new Intent(getApplication(), EZBackHomeService.class));

               // Toast.makeText(getApplicationContext(), "OK READY CLICK START WHEN READY!!", Toast.LENGTH_SHORT).show();

                //NO!!!! SIMPLEMENTE SALIMOS

                Myapplication.preferences.edit().putBoolean(Myapplication.PREF_BOOL_SERVICEENABLED, true).commit();
                finish();



            }

        }

    }


    public void start(View view) {



        //TODO ask for ACCESIBILITY


        //2ºº)chequeamos si la accesibility esta habilitada:
        boolean acssebilityEnable=  isAccessibilityEnabled();

        Log.d("info", "esta ahabilitgada ya: "+acssebilityEnable);





        if (!acssebilityEnable){
            //1º)si no esta habilitada aun le decimos que lo habilite:


            Toast.makeText(getApplicationContext(), "YOU HAVE TO ENABLE ME TO WORK!!!!", Toast.LENGTH_SHORT).show();


           // Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
          //  startActivityForResult(intent, REQUEST_CODE);




        }

        else{



            chequeasobreescribirpantalla();


            //si se habilito..empieza el service
            startService(new Intent(getApplication(), EZBackHomeService.class));





            finish();
        }



        // Code to start the Service//OJO solo se puede empezar el service cunado tengamos permisod e Accesibility!!
        //o da crash:  Caused by: android.view.WindowManager$BadTokenException: Unable to add window android.view.ViewRootImpl$W@1232b61ç
        // -- permission denied for this window type

        // startService(new Intent(getApplication(), EZBackHomeService.class));

    }
}
