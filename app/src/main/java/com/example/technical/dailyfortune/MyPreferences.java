package com.example.technical.dailyfortune;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Technical on 11/25/2015.
 */
public class MyPreferences {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE=0;
    private static final String PREF_NAME="DailyFortune";
    private static final String IS_FIRSTTIME="IsFirstTime";
    public static final String UserName="name";

    public MyPreferences(Context context){
        this.context=context;
        pref=context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor=pref.edit();
        editor.apply();

    }

    public boolean isFirstTime(){
        return pref.getBoolean(IS_FIRSTTIME,true);

    }
    public void setOld(boolean b){
        if(b){
            editor.putBoolean(IS_FIRSTTIME,false);
            editor.commit();
        }
    }
    public String getUserName(){
        return pref.getString(UserName,"");

    }
    public void setUserName(String name){
        editor.putString(UserName,name);
        editor.commit();
    }
}
