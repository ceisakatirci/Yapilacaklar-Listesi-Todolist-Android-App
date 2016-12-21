package com.isakatirci.yapilacaklarlistesi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.widget.Toast;

import model.Gorev;

public class EmptyActivity extends AppCompatActivity {
    private Gorev gorev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        long id = getIntent().getLongExtra("id", -1);
        if (id > 0) {
            gorev = new YapilacaklarListesiDbHelper(this).getirGorev(id);
            builder.setMessage(gorev.getGorevAdi())
                    .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String mesaj = gorev.getGorevAdi() + " Görevini \nSaat: " + gorev.getSonaErmeSaati()
                                    + "\nGün:" + gorev.getSonaErmeSaati() + "\nile yapılması gerekmektedir.\n" +
                                    "Açıklama: " + gorev.getAciklama();
                            if (gorev.getTelefonNumarasi() != null && gorev.getTelefonNumarasi().length() != 0) {
                                gonderSMS(gorev.getTelefonNumarasi(), mesaj);
                            }
                            finish();
                        }
                    })
                    .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(getApplicationContext(), "SMS Gönderilmesini İptal Ettiniz!", Toast.LENGTH_LONG).show();

                            finish();
                        }
                    });
            AlertDialog d = builder.create();
            d.setTitle("Bu Görev için SMS Gönderilsin Mi?");
            d.show();
        }
    }

    protected void gonderSMS(String telefon, String mesaj) {
        //region Description
      /*  Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", telefon);
        smsIntent.putExtra("sms_body", mesaj);
        try {
            startActivity(smsIntent);
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(),
                    "SMS Gönderme Başarısız.", Toast.LENGTH_LONG).show();
        }*/
        //endregion
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefon, null, mesaj, null, null);
            Toast.makeText(getApplicationContext(), "SMS Sent!",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    //region Description
  /*  protected void sendSMSMessage() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(mTelefonNumarasi, null, mMesaj, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }
*/
    //endregion
}
