package com.isakatirci.yapilacaklarlistesi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.List;
import adapter.GorevArrayAdapter;
import adapter.GorevListeArrayAdapter;
import model.Gorev;
import model.GorevListe;
import service.YapilacaklarListesiIntentService;
public class GorevListesiActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    static final int GOREV_ACTIVITY = 5006;
    private static final int RESULT_LOAD_IMAGE = 566;
    private static final int AYARLAR_ACTIVITY = 458;
    private static final long BIR_DAKIKA = 50;
    List<Gorev> mGorevListesi;
    SharedPreferences preferences;
    private YapilacaklarListesiDbHelper dbHelper;
    private Gorev mGorev;
    private ArrayAdapter<Gorev> mGorevArrayAdapter;
    private List<GorevListe> gorevListeleri;
    private boolean bitmisGorevlerListelensinMi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);
        bitmisGorevlerListelensinMi = preferences.getBoolean("PREF_BITMIS_GOREV_GIZLE", false);
     /*   Intent mServiceIntent = new Intent(GorevListesiActivity.this, YapilacaklarListesiIntentService.class);
        GorevListesiActivity.this.startService(mServiceIntent);*/
        Context context = this;
        //BroadcastReceiver içine bunu yeniden yazacağız
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent serviceIntent = new Intent(context, YapilacaklarListesiIntentService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 1, serviceIntent, 0);
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, BIR_DAKIKA, BIR_DAKIKA, pendingIntent);
     /* Intent s = new Intent(GorevListesiActivity.this,Main2Activity.class);
        startActivity(s);
*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gorev_listesi);
        //region Description
   /*     Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);*/
       /* Intent intent = new Intent(getApplicationContext(),EmptyActivity.class);
        startActivity(intent);*/
        //endregion
        dbHelper = new YapilacaklarListesiDbHelper(this);
        //region Description
/*        Gorev gorev = new Gorev();
        gorev.setGorevAdi("görev1");
        dbHelper.ekleGorev(gorev);
        gorev.setGorevAdi("görev2");
        dbHelper.ekleGorev(gorev);
        gorev.setGorevAdi("görev3");
        dbHelper.ekleGorev(gorev);*/
        //endregion
        mGorevListesi = dbHelper.getirButunGorevlerBitmisGorevSec(false, bitmisGorevlerListelensinMi);
        mGorevArrayAdapter = new GorevArrayAdapter(this, 0, mGorevListesi);
        ListView listView = (ListView) findViewById(R.id.listViewGorevListesi);
        listView.setAdapter(mGorevArrayAdapter);
        //region Description
        //add event listener so we can handle clicks
      /*  AdapterView.OnItemClickListener adapterViewListener = new AdapterView.OnItemClickListener() {
            //on click
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        };*/
        //endregion
        //set the listener to the list view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mGorev = mGorevListesi.get(position);
                Intent intent = new Intent(getApplicationContext(), GorevActivity.class);
                intent.putExtra("id", mGorev.getGorevId());
                startActivityForResult(intent, GOREV_ACTIVITY);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GOREV_ACTIVITY) {
            //region Description
/*            if (resultCode == GorevActivity.GOREV_SILME) {
        *//*        String id = Long.toString(data.getLongExtra("id", -1));
                for (Gorev gorev : mGorevListesi) {
                    long gecici = gorev.getGorevId();
                    if (Long.toString(gecici).equals(id)) {
                        mGorevListesi.remove(gorev);
                        mGorevArrayAdapter.notifyDataSetChanged();
                        break;
                    }*//*
                mGorevArrayAdapter.clear();
                mGorevListesi = dbHelper.getirButunGorevlerBitmisGorevSec(true, bitmisGorevlerListelensinMi);
                mGorevArrayAdapter.addAll(mGorevListesi);
                mGorevArrayAdapter.notifyDataSetChanged();
            }*/
//        }
     /*   if (requestCode == GorevListesiActivity.AYARLAR_ACTIVITY) {
            Toast.makeText(getApplicationContext(),"jjjjjjjjjjjjjjjjjjjjj",Toast.LENGTH_LONG);
            return;
        }*/
/*            if (resultCode == GorevActivity.GOREV_EKLEME) {
              *//*  long id = data.getLongExtra("id", -1);
                Gorev yeniGorev = dbHelper.getirGorev(id);
                mGorevListesi.add(yeniGorev);*//*
                mGorevArrayAdapter.clear();
                mGorevListesi = dbHelper.getirButunGorevlerBitmisGorevSec(true, bitmisGorevlerListelensinMi);
                mGorevArrayAdapter.addAll(mGorevListesi);
                mGorevArrayAdapter.notifyDataSetChanged();
            }*/
//            if (resultCode == GorevActivity.GOREV_GUNCELLEME) {
                /*long longId = data.getLongExtra("id", -1);
                String stringId = Long.toString(longId);
                Gorev guncelGorev = dbHelper.getirGorev(longId);
                for (Gorev gorev : mGorevListesi) {
                    long geciciId = gorev.getGorevId();
                    if (Long.toString(geciciId).equals(stringId)) {
                        int indeks = mGorevListesi.indexOf(gorev);
                        mGorevListesi.remove(gorev);
                        mGorevListesi.add(indeks, guncelGorev);
                        mGorevArrayAdapter.notifyDataSetChanged();
                        break;
                    }*/
       ;
            //endregion
        }
        if (requestCode == AYARLAR_ACTIVITY) {
            bitmisGorevlerListelensinMi = preferences.getBoolean("PREF_BITMIS_GOREV_GIZLE", false);
            mGorevArrayAdapter.clear();
            mGorevListesi = dbHelper.getirButunGorevlerBitmisGorevSec(false, bitmisGorevlerListelensinMi);
            mGorevArrayAdapter.addAll(mGorevListesi);
            mGorevArrayAdapter.notifyDataSetChanged();
        }
        mGorevListesi = dbHelper.getirButunGorevlerBitmisGorevSec(false, bitmisGorevlerListelensinMi);
        mGorevArrayAdapter = new GorevArrayAdapter(this, 0, mGorevListesi);
        ListView listView = (ListView) findViewById(R.id.listViewGorevListesi);
        listView.setAdapter(null);
        listView.setAdapter(mGorevArrayAdapter);
    }
    //region Description
   /* public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }*/
    //endregion
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        gorevListeleri = dbHelper.getirButunGorevListeler(true);
        getMenuInflater().inflate(R.menu.gorev_listesi_yeni, menu);
        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        ArrayAdapter<GorevListe> adapter = new GorevListeArrayAdapter(this, android.R.layout.simple_spinner_item, gorevListeleri);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mGorevArrayAdapter.clear();
                GorevListe gorevListe = gorevListeleri.get(position);
                mGorevListesi = dbHelper.getirButunGorevler(gorevListe);
                mGorevArrayAdapter.addAll(mGorevListesi);
                if (mGorevListesi.size() > 0) {
                    String text = mGorevListesi.get(0).getGorevAdi();
//                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                }
                mGorevArrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_yeni_ekle:
                Intent intent = new Intent(getApplicationContext(), GorevActivity.class);
                startActivityForResult(intent, GOREV_ACTIVITY);
                return true;
            case R.id.action_ayarlar:
                Intent intent2 = new Intent(this, PreferenceFrameworkActivity.class);
                startActivityForResult(intent2, AYARLAR_ACTIVITY);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String[] gorevBildirimiArray = getResources().getStringArray(R.array.gorev_bildirimi);
        boolean gizle = preferences.getBoolean("PREF_BITMIS_GOREV_GIZLE", false);
        String gizleMesaj = (gizle) ? "Evet" : "Hayır";
        bitmisGorevlerListelensinMi = preferences.getBoolean("PREF_BITMIS_GOREV_GIZLE", false);
        String gorevBildirimi = preferences.getString("PREF_GOREV_BILDIRIMI", "0");
        Toast.makeText(getApplicationContext(), "Bitmiş Görev Gösterilsin Mi? " + gizleMesaj + "\n" +
                "Görev Bildirimi: " + gorevBildirimiArray[Integer.parseInt(gorevBildirimi) - 1], Toast.LENGTH_LONG).show();
        mGorevListesi = dbHelper.getirButunGorevlerBitmisGorevSec(false, bitmisGorevlerListelensinMi);
        mGorevArrayAdapter = new GorevArrayAdapter(this, 0, mGorevListesi);
        ListView listView = (ListView) findViewById(R.id.listViewGorevListesi);
        listView.setAdapter(null);
        listView.setAdapter(mGorevArrayAdapter);
    }
}
