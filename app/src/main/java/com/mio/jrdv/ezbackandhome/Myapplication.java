package com.mio.jrdv.ezbackandhome;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by joseramondelgado on 02/11/16.
 *
 * http://stackoverflow.com/questions/13558550/can-i-get-data-from-shared-preferences-inside-a-service
 *
 * For example, if you need access to your preferences somewhere else, you may call this to read preferences:

 String str = MyApplication.preferences.getString( KEY, DEFAULT );


 Or you may call this to save something to the preferences:

 MyApplication.preferences.edit().putString( KEY, VALUE ).commit();


 (don't forget to call commit() after adding or changing preferences!)
 */

public class Myapplication extends Application {
    public static SharedPreferences preferences;

    public static final String PREF_BOOL_SERVICEENABLED="ServiceEnable";
    public static final String PREF_BOOL_PANELINBLACK="PanelBlack";

    //para ña intro
    public static final String PREF_IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    //PARA EL TIMEPO DESDE QUE SE INSTALO Y ESTADISTICAS USO
    public static final String PREF_LONG_TIMEINSTALLED ="TimeInstalled";
    public static final String PREF_INT_VECESHOME="VecesHome";
    public static final String PREF_INT_VECESBACK="VecesBack";

    //PARA DEMO VERIOSN AÑADIR CLICK CON ANUNCIOS

    public static final String PREF_INT_NUMmaxpermitidodemo="MaxGesturesDemo";

    @Override
    public void onCreate() {
        super.onCreate();

        preferences = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
    }
}

