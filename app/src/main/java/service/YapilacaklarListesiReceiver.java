package service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class YapilacaklarListesiReceiver extends BroadcastReceiver {
    private static final long BIR_DAKIKA =50;

    @Override
    public void onReceive(Context context, Intent intent) {

        //region Description
   /*     Intent i = new Intent();
        i.setClassName("com.isakatirci", "com.isakatirci.yapilacaklarlistesi.EmptyActivity");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);*/
        //endregion
     /*   SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String gorevBildirimi = preferences.getString("PREF_GOREV_BILDIRIMI", "0");*/
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent serviceIntent = new Intent(context, YapilacaklarListesiIntentService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 1, serviceIntent, 0);
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, BIR_DAKIKA, BIR_DAKIKA, pendingIntent);

    }
}
