package georgia.com.dogapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceHelper {
    private static String PREF_TIME="pref_time";
    private static SharedPreferenceHelper instance;
    private static  SharedPreferences prefs;

    private SharedPreferenceHelper(Context context){
        prefs= PreferenceManager.getDefaultSharedPreferences(context);

    }
    public static SharedPreferenceHelper getInstance(Context context){
        if(instance==null){
            instance=new SharedPreferenceHelper(context);
        }
        return instance;
    }
    public void saveUpdateTime(long time){
        prefs.edit().putLong(PREF_TIME,time).apply();

    }
    public long getUpdateTime(){
        return prefs.getLong(PREF_TIME,0);

    }
    public String getCacheDuration(){
        return prefs.getString("pref_cache_duration","");
    }
}
