package com.mio.jrdv.ezbackandhome;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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

import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.tomer.fadingtextview.FadingTextView;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import static com.tomer.fadingtextview.FadingTextView.SECONDS;

public class MainActivity extends AppCompatActivity {


    //copiado de:https://androidmads.blogspot.com.es/2016/05/how-create-accessibility-service-in.html
    //v001 vesion inicial de la wweb con iconos ok
    //v04 añadido panel zephyr ok en negro y funcoines de back y home ok..12/4/17
    //V055 AÑADIDO CHEQUEO Y OBLIGACION DE ACTIVAR ACESIBILITY SI O SI)
    //V06 añadido en main previsualizar el panel de deslizamoento
    //V065 añadido intro help
    //V08 AÑADIDOS FLAVORS DE DEMO E INFINITE 18/4/17
    //V085 añadido stadisticas de uso y text annimated para ponerlass
    //V088 AÑADIDO logica de eatadisticas y nuevos Toast y detectada version demo 100 usos
    //V089 new gifs de ins
    //v09 iconos añadidso de demo e infinite pte solo texto español....o no





   // String apkname4AccesibilityService="com.mio.jrdv.ezbackandhome/com.mio.jrdv.ezbackandhome.EZBackHomeService";

    //TODO PARA LA DEMO ES DISTINTO:

    String apkname4AccesibilityService="com.mio.jrdv.ezbackandhomedemo/com.mio.jrdv.ezbackandhome.EZBackHomeService";


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

    //para el numeor de uso
    int backveces,homeveces;

    //para el maximo en DEDMO

    int maxGestonDemo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        chequeasobreescribirpantalla();


        Boolean SERVICEENABLED = Myapplication.preferences.getBoolean(Myapplication.PREF_BOOL_SERVICEENABLED,false);//por defecto vale 0){



        Log.d("INFO","PREF_BOOL_SERVICEENABLED: "+SERVICEENABLED);


        Boolean  PANELINBLACK = Myapplication.preferences.getBoolean(Myapplication.PREF_BOOL_PANELINBLACK,false);//por defecto vale 0){

        Log.d("INFO","PREF_BOOL_PANELINBLACK: "+PANELINBLACK);



        //guardar timepodesde que se instalo y recvuperar numero de veces home y back


          backveces = Myapplication.preferences.getInt(Myapplication.PREF_INT_VECESBACK,0);//por defecto vale 0){

        Log.d("INFO","NUMERO DE VECES BACK: "+backveces);

        homeveces = Myapplication.preferences.getInt(Myapplication.PREF_INT_VECESHOME,0);//por defecto vale 0){

        Log.d("INFO","NUMERO DE VECES HOME: "+homeveces);


        //para la fecha inicio
        long FechaInicio = Myapplication.preferences.getLong(Myapplication.PREF_LONG_TIMEINSTALLED,0);//por defecto vale 0)

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String fechainicio = formatter.format(FechaInicio);
        Log.d("INFO","se instalo el "+fechainicio);

        //dias que se instalo
        //http://stackoverflow.com/questions/23323792/android-days-between-two-dates

        long msDiff = System.currentTimeMillis() - FechaInicio;
        long diasDesdeInstalo = TimeUnit.MILLISECONDS.toDays(msDiff);

        Log.d("INFO","hace "+diasDesdeInstalo+" DIAS");


        if (FechaInicio==0){

            //si es 0 qeu ponga aun no esta lista:
            fechainicio="NOT YET WORKING!";
            diasDesdeInstalo=1;

        }

        if (diasDesdeInstalo<1){
            diasDesdeInstalo=1;
        }
        double backsaldianum=  backveces/diasDesdeInstalo;
        String backsaldia=String.format("%.1f",backsaldianum)+"/DAY";
        double homesaldianum=   homeveces/diasDesdeInstalo;
        String homesaldia=String.format("%.1f",homesaldianum)+"/DAY";


        //chequeo veriosn DEMO o INFINITE


        if (BuildConfig.FLAVOR == "demo") {
            // add some ads or restrict functionallity
            Log.d("INFO","MODO DEMO: ");

            //SI ES DEMO EL STRING CAMBIO
            apkname4AccesibilityService="com.mio.jrdv.ezbackandhomedemo/com.mio.jrdv.ezbackandhome.EZBackHomeService";


            //actualizar estadisticas:

            String[] texts = {"SINCE",String.valueOf(fechainicio),"BACK SIMULATED:", String.valueOf(backveces)+" TIMES","AND","HOME SIMULATED: ",String.valueOf(homeveces)+" TIMES","WHAT MEANS:","BACK USED",backsaldia,"AND","HOME USED:",
                    homesaldia,"FOR LESS THAN A BEER","PLEASE ","CONSIDER GETTING ","THE INFINITE VERSION!!"," "};



            final FadingTextView FTV = (FadingTextView) findViewById(R.id.fadingTextView);

            FTV.setTexts(texts); //You can use an array resource or a string array as the parameter



            //For text change every 1 seconds
            FTV.setTimeout(1, SECONDS);
            FTV.forceRefresh();

            //para guardar y recuperar el numero maximo en modo demo(aunmenta 100 cada evz que se ve un anuncio)

            maxGestonDemo = Myapplication.preferences.getInt(Myapplication.PREF_INT_NUMmaxpermitidodemo,100);//por defecto vale 100){

            Log.d("INFO","NUMERO MAXIMO DE GESTOS EN DEMO: "+maxGestonDemo);


        }
        else  if (BuildConfig.FLAVOR == "infinite") {
            // add some ads or restrict functionallity

            Log.d("INFO","MODO INFINITE: ");

            //SI ES INFINITE EL STRING CAMBIO
            apkname4AccesibilityService="com.mio.jrdv.ezbackandhomeinfinite/com.mio.jrdv.ezbackandhome.EZBackHomeService";


            //actualizar estadisticas:

            String[] texts = {"SINCE",String.valueOf(fechainicio),"BACK SIMULATED:", String.valueOf(backveces)+" TIMES","AND","HOME SIMULATED: ",String.valueOf(homeveces)+" TIMES","WHAT MEANS:","BACK USED",backsaldia,"AND","HOME USED:",
                    homesaldia,"THANKS FOR USING","THE INFINITE VERSION","HOPE U ENJOY IT"," "," "};


            final FadingTextView FTV = (FadingTextView) findViewById(R.id.fadingTextView);

            FTV.setTexts(texts); //You can use an array resource or a string array as the parameter



            //For text change every 1 seconds
            FTV.setTimeout(1, SECONDS);
            FTV.forceRefresh();

        }






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
                    //TODO pero asi lo poene detras del enable servie y da error!!! lo pongo la ppio

                    //chequeasobreescribirpantalla();

                    Log.d("INFO","PREF_BOOL_SERVICEENABLED: "+bChecked);


                    Myapplication.preferences.edit().putBoolean(Myapplication.PREF_BOOL_SERVICEENABLED, true).commit();

                    //Toast.makeText(getApplicationContext(), "YOU HAVE TO ENABLE ME TO WORK!!", Toast.LENGTH_SHORT).show();

                    //TODO en lugar de toast usamos https://github.com/Muddz/StyleableToast

                        showNewToast("NOW ENABLE EZBACKANDHOME AS ACCESIBILITY SERVICE !!" );


                    Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivityForResult(intent, REQUEST_CODE);


                } else {


                    Log.d("INFO","PREF_BOOL_SERVICEENABLED: "+bChecked);


                    Myapplication.preferences.edit().putBoolean(Myapplication.PREF_BOOL_SERVICEENABLED, false).commit();



                    //Toast.makeText(getApplicationContext(), "IF DISABLE THIS WILL OT WORK ANYMORE!!", Toast.LENGTH_SHORT).show();

                    //TODO en lugar de toast usamos https://github.com/Muddz/StyleableToast

                    showNewToast("IF DISABLE THIS WILL NOT WORK ANYMORE!!" );


                    Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
        });






    }

    private void showNewToast(String texto2Toast) {


        StyleableToast st = new StyleableToast(this, texto2Toast, Toast.LENGTH_SHORT);
        st.setBackgroundColor(Color.parseColor("#ff5a5f"));
        st.setTextColor(Color.WHITE);
        st.setIcon(R.mipmap.ic_launcher);//TODO poner icono app
        st.spinIcon();
        st.setCornerRadius(20);
        st.setMaxAlpha();
        st.show();



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

                        //si no estaba guarada ya antes guardamos la fecha de inicio ahora

                        long FechaInicio = Myapplication.preferences.getLong(Myapplication.PREF_LONG_TIMEINSTALLED,0);//por defecto vale 0)

                        if (FechaInicio!=0){

                            //no lo guaradamos ya estaba de antes
                        }

                        else {
                            //guaradmos la fecha:
                            Long ahora = System.currentTimeMillis();

                            /*
                            //http://stackoverflow.com/questions/7953725/how-to-convert-milliseconds-to-date-format-in-android
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            String dateString = formatter.format(new Date(dateInMillis)));
                             */

                            Myapplication.preferences.edit().putLong(Myapplication.PREF_LONG_TIMEINSTALLED, ahora).commit();

                        }
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
               // Toast.makeText(getApplicationContext(), "YOU HAVE TO ENABLE ME TO WORK!!!!", Toast.LENGTH_SHORT).show();

                //TODO en lugar de toast usamos https://github.com/Muddz/StyleableToast

                showNewToast("YOU HAVE TO ENABLE ME TO WORK!!!!" );

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


          //  Toast.makeText(getApplicationContext(), "YOU HAVE TO ENABLE ME TO WORK!!!!", Toast.LENGTH_SHORT).show();

            //TODO en lugar de toast usamos https://github.com/Muddz/StyleableToast

            showNewToast("YOU HAVE TO ENABLE ME TO WORK!!" );


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

    public void HelpPulsado(View view) {

        //pulsado help..vuelve a mostrar wellcome activity


        //ponemos el boolean a no..o no sale:
        Myapplication.preferences.edit().putBoolean(Myapplication.PREF_IS_FIRST_TIME_LAUNCH, false).commit();

        //volvemos a Mainactivity

        Intent startIntent = new Intent(this, WelcomeActivity.class);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(startIntent);

        finish();

    }

    public void AnuncioPulsado(View view) {

        //TODO aqui que salga anuncio!!!

        maxGestonDemo=maxGestonDemo+100;
        Myapplication.preferences.edit().putInt(Myapplication.PREF_INT_NUMmaxpermitidodemo, maxGestonDemo).commit();


        Log.d("INFO","numeromaximo de gestos en demo:"+maxGestonDemo);

    }
}
