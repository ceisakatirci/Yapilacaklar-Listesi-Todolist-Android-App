package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isakatirci.yapilacaklarlistesi.R;

import java.io.File;
import java.util.Date;
import java.util.List;

import model.Gorev;

public class GorevArrayAdapter extends ArrayAdapter<Gorev> {
    ImageView imageView1;
    private Context context;
    private List<Gorev> gorevler;

    public GorevArrayAdapter(Context context, int resource, List<Gorev> gorevler) {
        super(context, resource, gorevler);
        this.context = context;
        this.gorevler = gorevler;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //region Renk Kodları
        String[] renkKodlari = {"FF0000",
                "FF1000",
                "FF2000",
                "FF3000",
                "FF4000",
                "FF5000",
                "FF6000",
                "FF7000",
                "FF8000",
                "FF9000",
                "FFA000",
                "FFB000",
                "FFC000",
                "FFD000",
                "FFE000",
                "FFF000",
                "FFFF00",
                "F0FF00",
                "E0FF00",
                "D0FF00",
                "C0FF00",
                "B0FF00",
                "A0FF00",
                "90FF00",
                "80FF00",
                "70FF00",
                "60FF00",
                "50FF00",
                "40FF00",
                "30FF00",
                "20FF00",
                "10FF00"};
        //endregion
        Gorev gorev = gorevler.get(position);
        Date eklenmeZamani = new Date(gorev.getSonaErmeGunu() + " " + gorev.getSonaErmeSaati());
        Date bugun = new Date(System.currentTimeMillis());
        long fark = bugun.getTime() - eklenmeZamani.getTime(); //result in millis
        double y = ((double) fark / (double)(60 * 60 * 1000)) * (double)4;
        String uygunRenk;
        if (y <= -36) {
            uygunRenk = renkKodlari[renkKodlari.length - 1];
        } else if (-36 <= y && y < -32) {
            uygunRenk = renkKodlari[32];
        } else if (-32 <= y && y < -28) {
            uygunRenk = renkKodlari[30];
        } else if (-28 <= y && y < -24) {
            uygunRenk = renkKodlari[26];
        } else if (-24 <= y && y < -20) {
            uygunRenk = renkKodlari[24];
        } else if (-20 <= y && y < -16) {
            uygunRenk = renkKodlari[20];
        } else if (-16 <= y && y < -12) {
            uygunRenk = renkKodlari[16];
        } else if (-12 <= y && y < -8) {
            uygunRenk = renkKodlari[12];
        } else if (-8 <= y && y < -4) {
            uygunRenk = renkKodlari[8];
        } else if (-4 <= y && y <= 0) {
            uygunRenk = renkKodlari[4];
        } else {
            uygunRenk = renkKodlari[0];
        }
        //region Description
 /*       if (yarimSaatler < -renkKodlari.length / 2) {
        } else if (yarimSaatler >= -renkKodlari.length / 2 && yarimSaatler < 0) {
            uygunRenk = renkKodlari[(int) ((renkKodlari.length / 2) - yarimSaatler)];
        } else if (yarimSaatler >= 0 && yarimSaatler < renkKodlari.length / 2) {
            uygunRenk = renkKodlari[(int) yarimSaatler - (renkKodlari.length / 2)];
        } else {
        }*/
        //endregion
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.gorev_layout, null);
        TextView textViewGorevAdi = (TextView) view.findViewById(R.id.textViewGorevAdi);
        TextView textViewAciklana = (TextView) view.findViewById(R.id.textViewAciklana);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearLayoutSatir);
        linearLayout.setBackgroundColor(Color.parseColor("#" + uygunRenk));
        TextView textViewTarih = (TextView) view.findViewById(R.id.textViewTarih);
        TextView textViewSaat = (TextView) view.findViewById(R.id.textViewSaat);
        CheckBox checkBoxGorevBittimi = (CheckBox) view.findViewById(R.id.checkBoxGorevBittimi);
        imageView1 = (ImageView) view.findViewById(R.id.imageView1);
        checkBoxGorevBittimi.setFocusable(false);
        checkBoxGorevBittimi.setClickable(false);
        textViewGorevAdi.setText(gorev.getGorevAdi());
        if (gorev.getAciklama() != null) {
            int aciklamaLength = gorev.getAciklama().length();
            if (aciklamaLength >= 100) {
                String aciklamaTrim = gorev.getAciklama().substring(0, 100) + "...";
                textViewAciklana.setText(aciklamaTrim);
            } else {
                textViewAciklana.setText(gorev.getAciklama());
            }
        }
        textViewTarih.setText(gorev.getSonaErmeGunu());
        textViewSaat.setText(gorev.getSonaErmeSaati());
        checkBoxGorevBittimi.setChecked(gorev.getGorevBittiMi() == 0 ? false : true);
        if (gorev.getIkon() != null) {
            ContextWrapper cw = new ContextWrapper(context);
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            // Create imageDir
            File mypath = new File(directory, gorev.getIkon());
            loadImageFromStorage(mypath);
        }
        return view;
    }

    private void loadImageFromStorage(File file) {
        File f = file;
        int targetW = 350;
        int targetH = 250;
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(f.getPath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(f.getPath(), bmOptions);
        imageView1.setImageBitmap(bitmap);
          /*  imageView1.setImageBitmap(b);*/
    }
}
