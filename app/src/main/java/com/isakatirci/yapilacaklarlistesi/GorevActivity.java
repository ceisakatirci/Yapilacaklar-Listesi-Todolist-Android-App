package com.isakatirci.yapilacaklarlistesi;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import adapter.GorevListeArrayAdapter;
import adapter.TekrarArrayAdapter;
import fragment.DatePickerFragment;
import fragment.TimePickerFragment;
import model.Gorev;
import model.GorevListe;
import model.Tekrar;

public class GorevActivity extends AppCompatActivity {
    public final static int GOREV_SILME = 85;
    public final static int GOREV_EKLEME = 95;
    public static final int GOREV_GUNCELLEME = 105;
    private static final int RESULT_LOAD_IMAGE = 566;
    private static final int RESULT_PICK_CONTACT = 999;
    private static final int REQUEST_IMAGE_CAPTURE = 4775;
    List<Tekrar> tekrarListesi;
    List<GorevListe> gorevListeleri;
   Spinner spinnerTekrarlar;
    Spinner spinnerGorevListesi;
    YapilacaklarListesiDbHelper dbHelper;
    EditText editTextAciklama;
    EditText editTextGorevAdi;
    EditText editTextSonaErmeGunu;
    EditText editTextTelefonNumarasi;
    EditText editTextSonaErmeSaati;
    CheckBox checkBoxGorevBittiMi;
    GorevListe mGorevListe;
    String mCurrentPhotoPath;
    Tekrar mTekrar;
    long mId;
    Intent mIntent;
    ImageView imageViewSecilen;
    Bitmap mBitmap;
    TextView textView1DosyaAdi;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putInt(KEY_COUNTER,mCounter);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        mCounter=savedInstanceState.getInt(KEY_COUNTER);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gorev);
        dbHelper = new YapilacaklarListesiDbHelper(this);
        spinnerTekrarlar = (Spinner) findViewById(R.id.spinnerTekrarlar);
        spinnerGorevListesi = (Spinner) findViewById(R.id.spinnerGorevListesi);
        tekrarListesiniDoldur();
        gorevListeleriDoldur();
        //set the back (up) button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getirViewReferanslar();
        mIntent = getIntent();
        mId = mIntent.getLongExtra("id", -1);
        if (mId > 0) {
            Gorev gorev = dbHelper.getirGorev(mId);
            if (gorev.getGorevId() > 0) {
                editTextAciklama.setText(gorev.getAciklama());
                editTextGorevAdi.setText(gorev.getGorevAdi());
                editTextSonaErmeGunu.setText(gorev.getSonaErmeGunu());
                editTextSonaErmeSaati.setText(gorev.getSonaErmeSaati());
                editTextTelefonNumarasi.setText(gorev.getTelefonNumarasi());
                checkBoxGorevBittiMi.setChecked(gorev.getGorevBittiMi() == 0 ? false : true);
                if (gorev.getIkon() != null) {

                    try {
                        ContextWrapper cw = new ContextWrapper(getApplicationContext());
                        // path to /data/data/yourapp/app_data/imageDir
                        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                        // Create imageDir
                        File mypath = new File(directory, gorev.getIkon());
                        loadImageFromStorage(mypath);
                        textView1DosyaAdi.setText((gorev.getIkon()));
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                for (Tekrar tekrar : tekrarListesi) {
                    if (tekrar.getTekrarId() == gorev.getTekrarId()) {
                        int indeks = tekrarListesi.indexOf(tekrar);
                        spinnerTekrarlar.setSelection(indeks);
                    }
                }
                for (GorevListe liste : gorevListeleri) {
                    if (liste.getGorevListeId() == gorev.getGorevListeId()) {
                        int indeks = gorevListeleri.indexOf(liste);
                        spinnerGorevListesi.setSelection(indeks);
                    }
                }
                //set the title of this activity to be the street name
                getSupportActionBar().setTitle(gorev.getGorevAdi());
            }
        } else {
            CheckBox checkBox = (CheckBox) findViewById(R.id.checkBoxGorevBittiMi);
            checkBox.setClickable(true);
        }
        editTextGorevAdi.requestFocus();


    }

    private void getirViewReferanslar() {
        editTextAciklama = (EditText) findViewById(R.id.editTextAciklama);
        editTextGorevAdi = (EditText) findViewById(R.id.editTextGorevAdi);
        editTextSonaErmeGunu = (EditText) findViewById(R.id.editTextSonaErmeGunu);
        editTextSonaErmeSaati = (EditText) findViewById(R.id.editTextSonaErmeSaati);
        textView1DosyaAdi = (TextView) findViewById(R.id.textView1DosyaAdi);
        checkBoxGorevBittiMi = (CheckBox) findViewById(R.id.checkBoxGorevBittiMi);
        imageViewSecilen = (ImageView) findViewById(R.id.imageViewSecilen);
        editTextTelefonNumarasi = (EditText) findViewById(R.id.editTextTelefonNumarasi);
    }

    private void gorevListeleriDoldur() {
        gorevListeleri = dbHelper.getirButunGorevListeler(true);
        ArrayAdapter<GorevListe> adapter = new GorevListeArrayAdapter(this, android.R.layout.simple_spinner_item, gorevListeleri);
        spinnerGorevListesi.setAdapter(adapter);
        //set the listener to the list view
        spinnerGorevListesi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mGorevListe = gorevListeleri.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void tekrarListesiniDoldur() {
        tekrarListesi = dbHelper.getirButunTekrarlar();
        ArrayAdapter<Tekrar> adapter = new TekrarArrayAdapter(this, android.R.layout.simple_spinner_item, tekrarListesi);
        spinnerTekrarlar.setAdapter(adapter);
        spinnerTekrarlar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mTekrar = tekrarListesi.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

  /*          int targetW = 350;
            int targetH = 250;
            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
                        imageViewSecilen.setImageBitmap(imageBitmap);
.decodeStream(imageBitmap,null,bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;
            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;*/



            final int maxSize = 300;
            int outWidth;
            int outHeight;
            int inWidth = imageBitmap.getWidth();
            int inHeight = imageBitmap.getHeight();
            if(inWidth > inHeight){
                outWidth = maxSize;
                outHeight = (inHeight * maxSize) / inWidth;
            } else {
                outHeight = maxSize;
                outWidth = (inWidth * maxSize) / inHeight;
            }

            Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, outWidth, outHeight, false);





            imageViewSecilen.setImageBitmap(resizedBitmap);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            saveToInternalStorage(imageBitmap, timeStamp);
            textView1DosyaAdi.setText(timeStamp);

            //region Description
        /*    Uri targetUri = data.getData();
            String selectedPath = targetUri.getPath();
            File f = new File(selectedPath);
            textView1DosyaAdi.setText(f.getName());
            try {
//                textViewTargetUri.setText(targetUri.toString());
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                imageViewSecilen.setImageBitmap(bitmap);
                saveToInternalStorage(bitmap, f.getName());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return;*/
            //endregion


        }
        if (requestCode == RESULT_PICK_CONTACT) {

            if (resultCode == Activity.RESULT_OK) {

                Cursor cursor = null;
                try {
                    String telefonNo = null;
                    Uri uri = data.getData();
                    cursor = getContentResolver().query(uri, null, null, null, null);
                    cursor.moveToFirst();
                    int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    telefonNo = cursor.getString(phoneIndex);
                    editTextTelefonNumarasi.setText(telefonNo);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
                return;

            }
        }


        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri targetUri = data.getData();
            String selectedPath = targetUri.getPath();
            File f = new File(selectedPath);
            textView1DosyaAdi.setText(f.getName());
            try {
//                textViewTargetUri.setText(targetUri.toString());
//                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));

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

                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                imageViewSecilen.setImageBitmap(bitmap);

                saveToInternalStorage(bitmap, f.getName());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // Bundle extras = getIntent().getExtras();
        if (mId > 0) {
            // getMenuInflater().inflate(R.menu.main_menu, menu);
            // getMenuInflater().inflate(R.menu.display_contact, menu);
            getMenuInflater().inflate(R.menu.duzenle_sil_gorev, menu);
        } else {
            getMenuInflater().inflate(R.menu.yeni_gorev, menu);


        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.action_ayarlar:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_kaydet:
                if (mId > 0) {
                    Gorev gorev = new Gorev();
                    gorev.setAciklama(editTextAciklama.getText().toString());
                    gorev.setGorevAdi(editTextGorevAdi.getText().toString());
                    gorev.setSonaErmeGunu(editTextSonaErmeGunu.getText().toString());
                    gorev.setSonaErmeSaati(editTextSonaErmeSaati.getText().toString());
                    gorev.setGorevBittiMi(checkBoxGorevBittiMi.isChecked() ? 1 : 0);
                    gorev.setTekrarId(mTekrar.getTekrarId());
                    gorev.setTelefonNumarasi(editTextTelefonNumarasi.getText().toString());
                    gorev.setIkon(textView1DosyaAdi.getText().toString());
                    gorev.setGorevListeId(mGorevListe.getGorevListeId());
                    gorev.setGorevId(mId);
                    dbHelper.guncelleGorev(gorev);
                    mIntent.putExtra("id", mId);
                    setResult(GOREV_GUNCELLEME, mIntent);
                    finish();

                } else {

                    Gorev gorev = new Gorev();
                    gorev.setAciklama(editTextAciklama.getText().toString());
                    gorev.setGorevAdi(editTextGorevAdi.getText().toString());
                    gorev.setSonaErmeGunu(editTextSonaErmeGunu.getText().toString());
                    gorev.setSonaErmeSaati(editTextSonaErmeSaati.getText().toString());
                    gorev.setIkon(textView1DosyaAdi.getText().toString());
                    gorev.setGorevBittiMi(checkBoxGorevBittiMi.isChecked() ? 1 : 0);
                    gorev.setTekrarId(mTekrar.getTekrarId());
                    gorev.setGorevListeId(mGorevListe.getGorevListeId());
                    dbHelper.ekleGorev(gorev);
                    mIntent.putExtra("id", gorev.getGorevId());
                    setResult(GOREV_EKLEME, mIntent);
                    finish();

                }

                return true;
            case R.id.Edit_Contact:
                Button b = (Button) findViewById(R.id.button1);
                b.setVisibility(View.VISIBLE);
       /*         name.setEnabled(true);
                name.setFocusableInTouchMode(true);
                name.setClickable(true);

                phone.setEnabled(true);
                phone.setFocusableInTouchMode(true);
                phone.setClickable(true);

                email.setEnabled(true);
                email.setFocusableInTouchMode(true);
                email.setClickable(true);

                street.setEnabled(true);
                street.setFocusableInTouchMode(true);
                street.setClickable(true);

                place.setEnabled(true);
                place.setFocusableInTouchMode(true);
                place.setClickable(true);*/

                return true;
            case R.id.action_sil:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                Gorev _gorev = dbHelper.getirGorev(mId);
                builder.setMessage("Görev: " + _gorev.getGorevAdi() + " Silinsin Mi?")
                        .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Gorev gorev = new Gorev();
                                gorev.setGorevId(mId);
                                if (dbHelper.silGorev(gorev)) {
                                    Toast.makeText(getApplicationContext(), "Görev Silindi!",
                                            Toast.LENGTH_LONG).show();
                                    setResult(GOREV_SILME, mIntent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Görev Silme Başarısız!",
                                            Toast.LENGTH_LONG).show();
                                    setResult(Activity.RESULT_CANCELED, mIntent);
                                    finish();
                                }


                            }
                        })
                        .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getApplicationContext(), "Görev Silme Başarısız!",
                                        Toast.LENGTH_LONG).show();
                            }
                        });

                AlertDialog d = builder.create();
                d.setTitle("Görevi Silmek İstediğinizden Emin Misiniz?");
                d.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }


    }

    public void gunSec(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void saatSec(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void resimSec(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String dosyaAdi) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, dosyaAdi);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        return directory.getAbsolutePath();
    }

    private void loadImageFromStorage(File file) {

        /* File f = new File(path, dosyaAdi);*/
        File f = file;
            /*Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));*/
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
        ImageView img = (ImageView) findViewById(R.id.imageViewSecilen);
        img.setImageBitmap(bitmap);

    }

    public void telefonNumarasiSec(View view) {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
    }

    public void resimCek(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }


    }


}
