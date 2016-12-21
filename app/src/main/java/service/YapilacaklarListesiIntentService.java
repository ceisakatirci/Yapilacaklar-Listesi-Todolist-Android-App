package service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.isakatirci.yapilacaklarlistesi.EmptyActivity;
import com.isakatirci.yapilacaklarlistesi.GorevActivity;
import com.isakatirci.yapilacaklarlistesi.R;
import com.isakatirci.yapilacaklarlistesi.YapilacaklarListesiDbHelper;

import java.util.Date;
import java.util.List;

import model.Gorev;

public class YapilacaklarListesiIntentService extends IntentService {
    private static final String BILDIRI_GOREV_ID = "id";
    private boolean zamaniGelmemis = false;
    private YapilacaklarListesiDbHelper dbHelper = new YapilacaklarListesiDbHelper(this);
    private Gorev mGorev;

    public YapilacaklarListesiIntentService() {
        super("YapilacaklarListesiIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        List<Gorev> gorevler = dbHelper.getirButunGorevlerBitmisGorevSec(true, false);
        for (final Gorev gorev : gorevler) {
            if (GorevBildirimSuresiIcindeMi(gorev)) {
                try {
                    notificationGoster(gorev, false);
                    if (gorev.getTelefonNumarasi() != null) {
                        Intent intent2 = new Intent(YapilacaklarListesiIntentService.this, EmptyActivity.class);
                        intent2.putExtra("id", gorev.getGorevId());
                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent2);
                        Thread.sleep(3000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                if (!zamaniGelmemis) {
                    notificationGoster(gorev, true);
                }
                //region Description
                /*String mesaj = gorev.getGorevAdi() + " Görevi \nSaat: " + gorev.getSonaErmeSaati()
                        + "\nGün:" + gorev.getSonaErmeSaati() + "\n ile yapılma zamanı geçti.";
                Toast.makeText(getApplicationContext(), mesaj,
                        Toast.LENGTH_LONG).show();*/
                //endregion
            }
        }
    }

    private void notificationGoster(Gorev gorev, boolean zamaniGecmisMi) {
        //region Description
    /*    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, GorevListesiActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.bildirim)
                .setContentTitle(gorev.getGorevAdi() + " Görevi")
                .setContentText("Saat: " + gorev.getSonaErmeSaati() + " Gün: " + gorev.getSonaErmeGunu()
                        + " den önce görev yapılmalıdır.")
                .setContentIntent(pendingIntent);
        notificationManager.notify(0, builder.build());*/
        //endregion
        PendingIntent pendingIntent;
        NotificationManager notificationManager;
        NotificationCompat.Builder notificationBuilder;
        Intent gorevActivityIntent = new Intent(this, GorevActivity.class);
        gorevActivityIntent.putExtra(BILDIRI_GOREV_ID, gorev.getGorevId());
        gorevActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //Initialize PendingIntent
        pendingIntent = PendingIntent.getActivity(this, (int) gorev.getGorevId(), gorevActivityIntent, 0);
        //Initialize NotificationManager using Context.NOTIFICATION_SERVICE
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //Prepare Notification Builder
        // .setStyle(new NotificationCompat.BigTextStyle().bigText(""))
        // MainActivity.this this yerine bu yazıyordu..
        notificationBuilder = new NotificationCompat.Builder(this);
        if (zamaniGecmisMi) {
            String mesaj = gorev.getGorevAdi() + " Görevinin Zamanı Geçmiş \nSaat: " + gorev.getSonaErmeSaati()
                    + "\nGün: " + gorev.getSonaErmeGunu() + "\nile yapılması gerekmekteydi.";
            notificationBuilder
                    .setContentTitle(gorev.getGorevAdi() + " Görevi")
                    .setSmallIcon(R.drawable.uyari2)
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(mesaj))
                    .setContentText(" Görev Zamanı Geçmiş");
        } else {
            String mesaj = gorev.getGorevAdi() + " Görevinin Bildirim için Ayarladığınız \nSaat: " + gorev.getSonaErmeSaati()
                    + "\nGün: " + gorev.getSonaErmeGunu() + "\nile yapılması beklenmektedir.";
            notificationBuilder
                    .setContentTitle(gorev.getGorevAdi() + " Görevi")
                    .setSmallIcon(R.drawable.bildirim)
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(mesaj))
                    .setContentText("Görev Yapılma Zamanı Geldi");
        }
        //Bildirim sesi
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setSound(uri);
        //Titreşim
        long[] v = {1000, 1000, 1000, 1000, 1000};
        notificationBuilder.setVibrate(v);
        notificationManager.notify((int) gorev.getGorevId(), notificationBuilder.build());
    }

    private boolean GorevBildirimSuresiIcindeMi(Gorev gorev) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String gorevBildirimi = preferences.getString("PREF_GOREV_BILDIRIMI", "0");
        //region Description
        //        YapilacaklarListesiDbHelper dbHelper = new YapilacaklarListesiDbHelper(this);
/*        String[] g = gorev.getSonaErmeGunu().split("/");
        String[] s = gorev.getSonaErmeSaati().split(":");*/
        //endregion
        Date eklenmeZamani = new Date(gorev.getSonaErmeGunu() + " " + gorev.getSonaErmeSaati());
        Date bugun = new Date(System.currentTimeMillis());
        long fark = bugun.getTime() - eklenmeZamani.getTime(); //result in millis
        gorev.setFark(fark);
        dbHelper.guncelleZamanFarki(gorev);
        if (fark > 0)
            return false;
        fark = -fark;
        double saatler = (double) fark / (double) (60 * 60 * 1000);
        double dakikalar = (double) fark / (double) (60 * 1000);
        double saniyeler = (double) fark / (double) 1000;
        zamaniGelmemis = false;
        switch (Integer.parseInt(gorevBildirimi)) {
            case 1:
                if (saniyeler <= 60) {
                    return true;
                } else {
                    zamaniGelmemis = true;
                }
                break;
            case 2:
                if (dakikalar > 1 && dakikalar <= 5) {
                    return true;
                } else {
                    zamaniGelmemis = true;
                }
                break;
            case 3:
                if (dakikalar > 5 && dakikalar <= 15) {
                    return true;
                } else {
                    zamaniGelmemis = true;
                }
                break;
            case 4:
                if (dakikalar > 15 && dakikalar <= 30) {
                    return true;
                } else {
                    zamaniGelmemis = true;
                }
                break;
            case 5:
                if (dakikalar >= 60 && saatler <= 1) {
                    return true;
                } else {
                    zamaniGelmemis = true;
                }
                break;
            case 6:
                if (saatler > 1 && saatler <= 2) {
                    return true;
                } else {
                    zamaniGelmemis = true;
                }
                break;
            case 7:
                if (saatler > 2 && saatler <= 3) {
                    return true;
                } else {
                    zamaniGelmemis = true;
                }
                break;
        }
        return false;
    }
}
