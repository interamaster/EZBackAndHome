package com.mio.jrdv.ezbackandhome;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

public class EZBackHomeService extends AccessibilityService {

    WindowManager windowManager;
   // ImageView back,home,notification,minimize;
   // ImageView azephyrPanel;    //mio panel zephyr:
    View azephyrPanelView;    //mio panel zephyr:
    WindowManager.LayoutParams params;
    AccessibilityService service;


    //PARA LAS EMDIDAD DE PANTALLA Y CALCULAR OK EL fLINT

    int alturaescala;
    int heigh;//altura de la pantalla en pixels


    //para saber si es un arriba y abajo para atras key

    boolean lock;


    //para el numeor de uso
    int backveces,homeveces;

    //para el maximo en DEDMO

    int maxGestonDemo;



    @SuppressLint("RtlHardcoded")
    @Override
    public void onCreate() {
        super.onCreate();





        Log.d("INFO","ARRANACADO AZEPHYR NEW");


        //guardar  y recvuperar numero de veces home y back


        backveces = Myapplication.preferences.getInt(Myapplication.PREF_INT_VECESBACK,0);//por defecto vale 0){

        Log.d("INFO","NUMERO DE VECES BACK: "+backveces);

        homeveces = Myapplication.preferences.getInt(Myapplication.PREF_INT_VECESHOME,0);//por defecto vale 0){

        Log.d("INFO","NUMERO DE VECES BACK: "+homeveces);





        //para guardar y recuperar el numero maximo en modo demo(aunmenta 100 cada evz que se ve un anuncio)

        maxGestonDemo = Myapplication.preferences.getInt(Myapplication.PREF_INT_NUMmaxpermitidodemo,100);//por defecto vale 100){

        Log.d("INFO","NUMERO MAXIMO DE GESTOS EN DEMO: "+maxGestonDemo);



        lock=false;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);




     //  boolean PANELINBLACK = Myapplication.preferences.getBoolean(Myapplication.PREF_BOOL_PANELINBLACK,false);//por defecto vale 0){

      //  Log.d("INFO ","PREF_BOOL_PANELINBLACK in service: "+PANELINBLACK);





 /*
        back = new ImageView(this);
        home = new ImageView(this);
        minimize = new ImageView(this);
        notification = new ImageView(this);

        back.setImageResource(R.drawable.ic_back);
        home.setImageResource(R.drawable.ic_home);
        minimize.setImageResource(R.drawable.ic_min);
        notification.setImageResource(R.drawable.ic_notification);
*/






        //mio panel zephyr:
/*
        azephyrPanel=new ImageView(this);
        azephyrPanel.setImageResource(R.drawable.azephyrpanel1);
*/

        //mio panel zephyr con View MEJOR
        azephyrPanelView=new View(this);


///////////////////////////////////home//////////////////////////////////////////////
/*
        params= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER_VERTICAL|Gravity.RIGHT;
        params.x = 10;
        params.y = 50;

        windowManager.addView(home, params);

*/
        ///////////////////////////////////minimize//////////////////////////////////////////////
/*
        params= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER_VERTICAL|Gravity.RIGHT;
        params.x = 10;
        params.y = 250;

        windowManager.addView(minimize, params);

 */
        ///////////////////////////////////notification//////////////////////////////////////////////
/*
        params= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER_VERTICAL|Gravity.RIGHT;
        params.x = 10;
        params.y = 450;

        windowManager.addView(notification, params);
*/
        ///////////////////////////////////back//////////////////////////////////////////////
/*


        params= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER_VERTICAL|Gravity.RIGHT;
        params.x = 10;
        params.y = 650;

        windowManager.addView(back, params);

*/
        ////////////////////////////////   //mio panel zephyr://///////////////////////////////////////////
/*
        params= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);




        params.gravity = Gravity.BOTTOM|Gravity.CENTER;
        params.x = 0;
        params.y = 0;

        windowManager.addView(azephyrPanel, params);
*/

        ////////////////////////////////   //mio panel zephyr View!!! en vez de ImageView://///////////////////////////////////////////


        // azephyrPanelView.setLayoutParams(new WindowManager.LayoutParams(780, 50));
/*
//ya no lo uso
        if (PANELINBLACK){
            azephyrPanelView.setBackgroundColor(getResources().getColor(android.R.color.black));//TODO quitar /hacer transparente

        }

        else {

            azephyrPanelView.setBackgroundColor(getResources().getColor(android.R.color.transparent));//TODO quitar /hacer transparente


        }

*/

        //para saber medidas pantalla reales!!:

        int w = windowManager.getDefaultDisplay().getWidth();
        heigh = windowManager.getDefaultDisplay().getHeight();
        Log.d("INFO","heigh= "+heigh+ " weigh= "+w);//s4 D/INFO: heigh= 1920 weigh= 1080

         alturaescala =heigh/25;//TODO poner de azephyr valor

        params= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                alturaescala,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);




        params.gravity = Gravity.BOTTOM|Gravity.CENTER;
        params.x = 0;
        params.y = 0;

        windowManager.addView(azephyrPanelView, params);



        //AÑADIDMOS LE LISTENER

        final GestureDetector gdt = new GestureDetector(new GestureListener());



        azephyrPanelView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                gdt.onTouchEvent(event);
                return true;
            }
        });



/*

//LISTENER...
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_NOTIFICATIONS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        minimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
*/
    }





    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////GESTURE LISTENER PARA EL FLING//////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            /*
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                Log.d("FLING","right to left");
                return false; // Right to left
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                Log.d("FLING","left to right");
                return false; // Left to right
            }

            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {

                Log.d("FLING","botton to top");
                return false; // Bottom to top
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {

                Log.d("FLING","top to botton");
                return false; // Top to bottom
            }
            */
            //copiado de azpehyr mio

            lock = false;

           // Log.d("FLING", "velocidadY: "+velocityY);

            if (velocityY < -heigh / 2) {

                //realmente  da = el valor el caso es que sea negativo(hacia arriba
                lock = false;
                Log.d("FLING", "lock non se va  a hacer");
                PulsarHomeKey();

            }
/*
//NO DETECTAT EL FLING HACIA ABAJO PORQUE EMPIEZA MAS ARRIBA...
            else if (velocityY < heigh/2){

                Log.d("FLING", "fling hacia abajo detectatado");
            }
*/

            return false;
        }


        //idem para el onscroll del back key

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

           // Log.d("scroll", "scroll x: " + distanceX + " scroll y: " + distanceY);

            int Yinicial = (int) e1.getY();
            int Yfinal = (int) e2.getY();


           // Log.d("scroll", "Yinicial " + Yinicial + " Yfinal: " + Yfinal);

            int escala = heigh / 400;
            //Log.d("scroll", "escala del scroll " + escala);
            if (Math.abs(Yfinal) > Yinicial * escala &&!lock) {
                lock = true;
                Log.d("scroll", "1/2 lock vetrical ok!!");
            }

            int YfinalNew = Yinicial + 2 * escala;

           // Log.d("scroll", "YfinalNew " + YfinalNew);
            if (Math.abs(Yfinal) <= YfinalNew && lock) {

               Log.d("scroll", "ciompleto scroll up and down!!");

                lock = false;

                BackKeySImulate();

            }
            ///copiado de azephy mio

            /*

Dim Yinicial As Int

	Dim Yfinal As Int

	Yinicial= ZephyrPanelGesture.getY(MotionEvent1, 0) 'Y1
	Yfinal =ZephyrPanelGesture.getY(MotionEvent2, 0)  'Y2

	'Log("inicial:" & Yinicial & "  y final:"  &Yfinal)

	Dim escala As Int
	escala=heightPixels/100
	'Log("la escala del lockscreen es:"& escala)
	'esto da 480/100= 4 para el icas y dara 1920/100=19 para el S4


	If Abs(Yfinal)>escala*Yinicial Then
	Lock=True
	'Log("1/2 lock verical ok")

	End If



	Dim YfinalNew As Int

	YfinalNew=Yinicial+2*escala

	If Abs(Yfinal)<=YfinalNew And Lock=True Then


	'Log("se va ahcer lock")

	Lock=False

	LockScreen

	End If


             */



            return true;

        }
    }

    private void PulsarHomeKey() {

        // aumentamos el numero

        homeveces++;
        Myapplication.preferences.edit().putInt(Myapplication.PREF_INT_VECESHOME, homeveces).commit();

        //chequeo veriosn DEMO o INFINITE


        if (BuildConfig.FLAVOR == "demo") {
            // add some ads or restrict functionallity


            //recuepramos aqui el valor


            //para guardar y recuperar el numero maximo en modo demo(aunmenta 100 cada evz que se ve un anuncio)

            maxGestonDemo = Myapplication.preferences.getInt(Myapplication.PREF_INT_NUMmaxpermitidodemo,100);//por defecto vale 100){



            Log.d("INFO"," home en MODO DEMO: usada:"+homeveces+" veces y el maximo son :"+maxGestonDemo);

            if (homeveces>maxGestonDemo){

                //TODO hacer algo esat en demo

                demoexpired();

                return;//salimos de la funcion sin hacer nada... ?¿?
            }




        }

        try {
            performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void demoexpired() {






            StyleableToast st = new StyleableToast(this, "DEMO EXPIRED, GET THE INFINITE VERSION OR CLICK PLUS BUTTON!!", Toast.LENGTH_SHORT);
            st.setBackgroundColor(Color.parseColor("#ff5a5f"));
            st.setTextColor(Color.WHITE);
            st.setIcon(R.mipmap.ic_launcher);//TODO poner icono app
            st.spinIcon();
            st.setCornerRadius(20);
            st.setMaxAlpha();
            st.show();





    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        Log.d("INFO","ACCESIBILITY EVENT DETECTED!!! IN  AZEPHYR NEW");

    }

    @Override
    public void onInterrupt() {

        Log.d("INFO","INTERRUMPIDO AZEPHYR NEW");

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d("TAG", "onServiceConnected");
        Log.d("INFO","ACCESIBILITY EVENT DETECTED!!! IN  AZEPHYR NEW");






        //volvemos a Mainactivity

        Intent startIntent = new Intent(this, MainActivity.class);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(startIntent);




    }



    private void BackKeySImulate() {
        // aumentamos el numero

        backveces++;
        Myapplication.preferences.edit().putInt(Myapplication.PREF_INT_VECESBACK, backveces).commit();


        //chequeo veriosn DEMO o INFINITE


        if (BuildConfig.FLAVOR == "demo") {
            // add some ads or restrict functionallity


            //para guardar y recuperar el numero maximo en modo demo(aunmenta 100 cada evz que se ve un anuncio)

            maxGestonDemo = Myapplication.preferences.getInt(Myapplication.PREF_INT_NUMmaxpermitidodemo,100);//por defecto vale 100){


            Log.d("INFO"," back en MODO DEMO:  usada:"+backveces+" veces");

            if (backveces>maxGestonDemo){

                //TODO hacer algo esat en demo

                demoexpired();


                return;//salimos de la funcion sin hacer nada... ?¿?
            }


        }

        try {
            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    }