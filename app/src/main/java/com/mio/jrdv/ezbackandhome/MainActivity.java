package com.mio.jrdv.ezbackandhome;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    //copiado de:https://androidmads.blogspot.com.es/2016/05/how-create-accessibility-service-in.html
    //v001 vesion inicial de la wweb con iconos ok
    //v04 añadido panel zephyr ok en negro y funcoines de back y home ok..12/4/17
    //V055 AÑADIDO CHEQUEO Y OBLIGACION DE ACTIVAR ACESIBILITY SI O SI)
    //V06 añadido en main previsualizar el panel de deslizamoento




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

    //para enselar panel

    View azephyrPanelViewFromMain;    //mio panel zephyr:


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
            ShowHidePanel(true);


        }
        else{
            swtichPANELINBLACK.setChecked(false);

            ShowHidePanel(false);

        }



        //To hide AppBar for fullscreen.
      //  ActionBar ab = getSupportActionBar();
     //   ab.hide();



        //LISTENER DEL PANEL IN BLACK

        swtichPANELINBLACK.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {

                    Log.d("INFO","PREF_BOOL_PANELINBLACK: "+bChecked);


                    Myapplication.preferences.edit().putBoolean(Myapplication.PREF_BOOL_PANELINBLACK, bChecked).commit();

                    ShowHidePanel(bChecked);



                } else {


                    Log.d("INFO","PREF_BOOL_PANELINBLACK: "+bChecked);




                    Myapplication.preferences.edit().putBoolean(Myapplication.PREF_BOOL_PANELINBLACK, bChecked).commit();

                    ShowHidePanel(bChecked);

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

    private void ShowHidePanel(boolean PANELINBLACK) {





        RelativeLayout rLayout = (RelativeLayout)findViewById(R.id.activity_main);
        rLayout.removeView(azephyrPanelViewFromMain);


        if (azephyrPanelViewFromMain==null){
            azephyrPanelViewFromMain= new View(this);
        }


        if (PANELINBLACK){
            azephyrPanelViewFromMain.setBackgroundColor(getResources().getColor(android.R.color.black));//TODO quitar /hacer transparente

        }

        else {

            azephyrPanelViewFromMain.setBackgroundColor(getResources().getColor(android.R.color.transparent));//TODO quitar /hacer transparente


        }


     WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        Display display = windowmanager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        Log.d("INFO","heigh= "+height+ " weigh= "+width);//s4 D/INFO: heigh= 1920 weigh= 1080


        int alturaescala =height/25;//TODO poner de azephyr valor


        RelativeLayout.LayoutParams params =  new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,alturaescala);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);


        azephyrPanelViewFromMain.setLayoutParams(params);

        //azephyrPanelViewFromMain.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, alturaescala));

        /*
         params.gravity = Gravity.BOTTOM|Gravity.CENTER;
        params.x = 0;
        params.y = 0;
         */




        rLayout.addView(azephyrPanelViewFromMain);




/*

        params.gravity = Gravity.BOTTOM|Gravity.CENTER;
        params.x = 0;
        params.y = 0;

        windowManager.addView(azephyrPanelViewFromMain, params);
*/






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
