package com.mio.jrdv.ezbackandhome;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;

public class EZBackHomeService extends AccessibilityService {

    WindowManager windowManager;
    ImageView back,home,notification,minimize;
    ImageView azephyrPanel;    //mio panel zephyr:
    View azephyrPanelView;    //mio panel zephyr:
    WindowManager.LayoutParams params;
    AccessibilityService service;


    //PARA LAS EMDIDAD DE PANTALLA Y CALCULAR OK EL fLINT

    int altura;


    //para saber si es un arriba y abajo para atras key

    boolean lock;



    @SuppressLint("RtlHardcoded")
    @Override
    public void onCreate() {
        super.onCreate();


        Log.d("INFO","ARRANACADO AZEPHYR NEW");


        lock=false;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);



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

        azephyrPanelView.setBackgroundColor(getResources().getColor(android.R.color.black));


        //para saber medidas pantalla reales!!:

        int w = windowManager.getDefaultDisplay().getWidth();
        int h = windowManager.getDefaultDisplay().getHeight();
        Log.d("INFO","heigh= "+h+ " weigh= "+w);//s4 D/INFO: heigh= 1920 weigh= 1080

         altura=h/25;//TODO poner de azephyr valor

        params= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                altura,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);




        params.gravity = Gravity.BOTTOM|Gravity.CENTER;
        params.x = 0;
        params.y = 0;

        windowManager.addView(azephyrPanelView, params);








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
    }

}