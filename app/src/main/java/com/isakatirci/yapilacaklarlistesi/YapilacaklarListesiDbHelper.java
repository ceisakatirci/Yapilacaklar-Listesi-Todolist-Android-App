package com.isakatirci.yapilacaklarlistesi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.isakatirci.yapilacaklarlistesi.YapilacaklarListesiContract.GorevEntry;
import com.isakatirci.yapilacaklarlistesi.YapilacaklarListesiContract.GorevListeEntry;
import com.isakatirci.yapilacaklarlistesi.YapilacaklarListesiContract.TekrarEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Gorev;
import model.GorevListe;
import model.Tekrar;

/**
 * Created by isa on 16.12.2016.
 */

public class YapilacaklarListesiDbHelper extends SQLiteOpenHelper {
    public static final int VERITABANI_VERSIYONU = 21;
    public static final String VERITABANI_ADI = "YapilacaklarGorevListesi.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String BLOB_TYPE = " BLOB";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String VIRGUL = ",";
    private static final String SQL_CREATE_GOREV =
            "CREATE TABLE " + GorevEntry.TABLO_ADI + " (" +
                    GorevEntry._ID + " INTEGER PRIMARY KEY," +
                    GorevEntry.SUTUN_ADI_GOREV_ADI + TEXT_TYPE + VIRGUL +
                    GorevEntry.SUTUN_ADI_SONA_ERME_TARIHI + TEXT_TYPE + VIRGUL +
                    GorevEntry.SUTUN_ADI_TELEFON_NUMARASI + TEXT_TYPE + VIRGUL +
                    GorevEntry.SUTUN_ADI_SONA_ERME_SAATI + TEXT_TYPE + VIRGUL +
                    GorevEntry.SUTUN_ADI_GOREV_BITTIMI + INTEGER_TYPE + VIRGUL +
                    GorevEntry.SUTUN_ADI_ZAMAN_FARKI + INTEGER_TYPE + VIRGUL +
                    GorevEntry.SUTUN_ADI_LISTE_ID + INTEGER_TYPE + VIRGUL +
                    GorevEntry.SUTUN_ADI_ACIKLAMA + TEXT_TYPE + VIRGUL +
                    GorevEntry.SUTUN_ADI_IKON + TEXT_TYPE + VIRGUL +
                    GorevEntry.SUTUN_ADI_TEKAR_ID + INTEGER_TYPE + " )";

    private static final String SQL_DELETE_GOREV =
            "DROP TABLE IF EXISTS " + GorevEntry.TABLO_ADI;

    private static final String SQL_CREATE_LISTE =
            "CREATE TABLE " + GorevListeEntry.TABLO_ADI + " (" +
                    GorevListeEntry._ID + " INTEGER PRIMARY KEY," +
                    GorevListeEntry.SUTUN_ADI_LISTE_ADI + TEXT_TYPE + " )";

    private static final String SQL_DELETE_LISTE =
            "DROP TABLE IF EXISTS " + GorevListeEntry.TABLO_ADI;

    private static final String SQL_CREATE_TEKRAR =
            "CREATE TABLE " + TekrarEntry.TABLO_ADI + " (" +
                    TekrarEntry._ID + " INTEGER PRIMARY KEY," +
                    TekrarEntry.SUTUN_ADI_TEKRAR_ADI + TEXT_TYPE + " )";

    private static final String SQL_DELETE_TEKRAR =
            "DROP TABLE IF EXISTS " + TekrarEntry.TABLO_ADI;

    public YapilacaklarListesiDbHelper(Context context) {
        super(context, VERITABANI_ADI, null, VERITABANI_VERSIYONU);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_GOREV);
        db.execSQL(SQL_CREATE_LISTE);
        db.execSQL(SQL_CREATE_TEKRAR);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO " + TekrarEntry.TABLO_ADI + "(" + TekrarEntry.SUTUN_ADI_TEKRAR_ADI + ") VALUES");
        stringBuilder.append("('Tekrar yok'),");
        stringBuilder.append("('Günde bir kez'),");
        stringBuilder.append("('Günde bir kez (Pazartesi-Cuma)'),");
        stringBuilder.append("('Haftada bir kez'),");
        stringBuilder.append("('Ayda bir kez'),");
        stringBuilder.append("('Yılda bir kez'),");
        stringBuilder.append("('Diğer...');");
        db.execSQL(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO " + GorevListeEntry.TABLO_ADI + "(" + GorevListeEntry.SUTUN_ADI_LISTE_ADI + ") VALUES");
        stringBuilder.append("('Varsayılan'),");
        stringBuilder.append("('Alışveriş'),");
        stringBuilder.append("('Beğendiklerim'),");
        stringBuilder.append("('Kişisel'),");
        stringBuilder.append("('İş');");
        db.execSQL(stringBuilder.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_GOREV);
        db.execSQL(SQL_DELETE_LISTE);
        db.execSQL(SQL_DELETE_TEKRAR);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean ekleGorev(Gorev gorev) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GorevEntry.SUTUN_ADI_GOREV_ADI, gorev.getGorevAdi());
        contentValues.put(GorevEntry.SUTUN_ADI_TELEFON_NUMARASI, gorev.getTelefonNumarasi());
        contentValues.put(GorevEntry.SUTUN_ADI_SONA_ERME_SAATI, gorev.getSonaErmeSaati());
        contentValues.put(GorevEntry.SUTUN_ADI_SONA_ERME_TARIHI, gorev.getSonaErmeGunu());
        contentValues.put(GorevEntry.SUTUN_ADI_ACIKLAMA, gorev.getAciklama());
        contentValues.put(GorevEntry.SUTUN_ADI_GOREV_BITTIMI, gorev.getGorevBittiMi());
        contentValues.put(GorevEntry.SUTUN_ADI_IKON, gorev.getIkon());
        contentValues.put(GorevEntry.SUTUN_ADI_TEKAR_ID, gorev.getTekrarId());
        contentValues.put(GorevEntry.SUTUN_ADI_LISTE_ID, gorev.getGorevListeId());
        Date eklenmeZamani = new Date(gorev.getSonaErmeGunu() + " " + gorev.getSonaErmeSaati());
        Date bugun = new Date(System.currentTimeMillis());
        long fark = bugun.getTime() - eklenmeZamani.getTime();// result in millis
        contentValues.put(GorevEntry.SUTUN_ADI_ZAMAN_FARKI, fark);
        long yeniSatirId = db.insert(GorevEntry.TABLO_ADI, null, contentValues);
        gorev.setGorevId(yeniSatirId);
      //  the row ID of the newly inserted row, or -1 if an error occurred
        return yeniSatirId > 0;
    }

    public boolean ekleGorevListe(GorevListe liste) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GorevListeEntry.SUTUN_ADI_LISTE_ADI, liste.getGorevListeAdi());
        long yeniSatirId = db.insert(GorevListeEntry.TABLO_ADI, null, contentValues);
        liste.setGorevListeId(yeniSatirId);
       // the row ID of the newly inserted row, or -1 if an error occurred
        return yeniSatirId > 0;
    }

    public boolean guncelleGorev(Gorev gorev) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GorevEntry.SUTUN_ADI_GOREV_ADI, gorev.getGorevAdi());
        contentValues.put(GorevEntry.SUTUN_ADI_SONA_ERME_SAATI, gorev.getSonaErmeSaati());
        contentValues.put(GorevEntry.SUTUN_ADI_SONA_ERME_TARIHI, gorev.getSonaErmeGunu());
        contentValues.put(GorevEntry.SUTUN_ADI_TELEFON_NUMARASI, gorev.getTelefonNumarasi());
        contentValues.put(GorevEntry.SUTUN_ADI_GOREV_BITTIMI, gorev.getGorevBittiMi());
        contentValues.put(GorevEntry.SUTUN_ADI_ACIKLAMA, gorev.getAciklama());
        contentValues.put(GorevEntry.SUTUN_ADI_IKON, gorev.getIkon());
        contentValues.put(GorevEntry.SUTUN_ADI_TEKAR_ID, gorev.getTekrarId());
        Date eklenmeZamani = new Date(gorev.getSonaErmeGunu() + " " + gorev.getSonaErmeSaati());
        Date bugun = new Date(System.currentTimeMillis());
        long fark = bugun.getTime() - eklenmeZamani.getTime(); //result in millis
        contentValues.put(GorevEntry.SUTUN_ADI_ZAMAN_FARKI, fark);

        contentValues.put(GorevEntry.SUTUN_ADI_LISTE_ID, gorev.getGorevListeId());
        String selection = GorevEntry._ID + " = ?";
        String[] selectionArgs = new String[]{Long.toString(gorev.getGorevId())};
      //  the number of rows affected
        return db.update(GorevEntry.TABLO_ADI, contentValues, selection, selectionArgs) > 0;

    }

    public boolean guncelleGorevListe(GorevListe liste) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GorevListeEntry.SUTUN_ADI_LISTE_ADI, liste.getGorevListeAdi());
        String selection = GorevListeEntry._ID + " = ?";
        String[] selectionArgs = new String[]{Long.toString(liste.getGorevListeId())};
      //  the number of rows affected
        return db.update(GorevListeEntry.TABLO_ADI, contentValues, selection, selectionArgs) > 0;

    }

    public boolean silGorev(Gorev gorev) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = GorevEntry._ID + " = ?";
        String[] selectionArgs = new String[]{Long.toString(gorev.getGorevId())};
      //  the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause.
        return db.delete(GorevEntry.TABLO_ADI, selection, selectionArgs) > 0;

    }

    public boolean silGorevListe(GorevListe liste) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = GorevListeEntry._ID + " = ?";
        String[] selectionArgs = new String[]{Long.toString(liste.getGorevListeId())};
      //  the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause.
        return db.delete(GorevListeEntry.TABLO_ADI, selection, selectionArgs) > 0;

    }

    public List<Gorev> getirButunGorevlerBitmisGorevSec(boolean hepsiMi, boolean bitmisGorevleriListelensinMi) {
        List<Gorev> gorevler = new ArrayList<Gorev>();
        String selectQuery;
        if (hepsiMi) {
            if (bitmisGorevleriListelensinMi) {
                selectQuery = "SELECT  * FROM " + GorevEntry.TABLO_ADI
                        + " ORDER BY " + GorevEntry.SUTUN_ADI_ZAMAN_FARKI + " DESC";
                ;

            } else {
                selectQuery = "SELECT  * FROM " + GorevEntry.TABLO_ADI
                        + " WHERE " + GorevEntry.SUTUN_ADI_GOREV_BITTIMI + " = 0"
                        + " ORDER BY " + GorevEntry.SUTUN_ADI_ZAMAN_FARKI + " DESC";
            }
        } else {
            if (bitmisGorevleriListelensinMi) {
                GorevListe gorevListe = getirButunGorevListeler(true).get(0);
                selectQuery = "SELECT  * FROM " + GorevEntry.TABLO_ADI
                        + " WHERE " + GorevEntry.SUTUN_ADI_LISTE_ID + " = " + gorevListe.getGorevListeId()
                        + " ORDER BY " + GorevEntry.SUTUN_ADI_ZAMAN_FARKI + " DESC";

            } else {
                GorevListe gorevListe = getirButunGorevListeler(true).get(0);
                selectQuery = "SELECT  * FROM " + GorevEntry.TABLO_ADI + " WHERE " + GorevEntry.SUTUN_ADI_LISTE_ID + " = " + gorevListe.getGorevListeId()
                        + " AND " + GorevEntry.SUTUN_ADI_GOREV_BITTIMI + " = 0"
                        + " ORDER BY " + GorevEntry.SUTUN_ADI_ZAMAN_FARKI + " DESC";
            }

        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Gorev gorev = new Gorev();
                gorev.setGorevId(cursor.getLong((cursor.getColumnIndex(GorevEntry._ID))));
                gorev.setGorevAdi(cursor.getString((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_GOREV_ADI))));
                gorev.setFark(cursor.getInt((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_ZAMAN_FARKI))));
                gorev.setTelefonNumarasi(cursor.getString((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_TELEFON_NUMARASI))));
                gorev.setGorevBittiMi(cursor.getInt((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_GOREV_BITTIMI))));
                gorev.setSonaErmeSaati(cursor.getString((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_SONA_ERME_SAATI))));
                gorev.setSonaErmeGunu(cursor.getString((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_SONA_ERME_TARIHI))));
                gorev.setIkon(cursor.getString((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_IKON))));
                gorev.setAciklama(cursor.getString((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_ACIKLAMA))));
                gorev.setGorevListeId(cursor.getLong((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_LISTE_ID))));
                gorev.setTekrarId(cursor.getLong((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_TEKAR_ID))));
                gorevler.add(gorev);
            } while (cursor.moveToNext());
        }
        return gorevler;
    }

    public List<Gorev> getirButunGorevler(boolean hepsiMi) {
        List<Gorev> gorevler = new ArrayList<Gorev>();
        String selectQuery;
        if (hepsiMi) {
            selectQuery = "SELECT  * FROM " + GorevEntry.TABLO_ADI
                    + " ORDER BY " + GorevEntry.SUTUN_ADI_ZAMAN_FARKI + " DESC";
        } else {
            GorevListe gorevListe = getirButunGorevListeler(true).get(0);
            selectQuery = "SELECT  * FROM " + GorevEntry.TABLO_ADI
                    + " WHERE " + GorevEntry.SUTUN_ADI_LISTE_ID + " = " + gorevListe.getGorevListeId()
                    + " ORDER BY " + GorevEntry.SUTUN_ADI_ZAMAN_FARKI + " DESC";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Gorev gorev = new Gorev();
                gorev.setGorevId(cursor.getLong((cursor.getColumnIndex(GorevEntry._ID))));
                gorev.setGorevAdi(cursor.getString((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_GOREV_ADI))));
                gorev.setTelefonNumarasi(cursor.getString((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_TELEFON_NUMARASI))));
                gorev.setGorevBittiMi(cursor.getInt((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_GOREV_BITTIMI))));
                gorev.setFark(cursor.getInt((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_ZAMAN_FARKI))));
                gorev.setSonaErmeSaati(cursor.getString((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_SONA_ERME_SAATI))));
                gorev.setSonaErmeGunu(cursor.getString((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_SONA_ERME_TARIHI))));
                gorev.setIkon(cursor.getString((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_IKON))));
                gorev.setAciklama(cursor.getString((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_ACIKLAMA))));
                gorev.setGorevListeId(cursor.getLong((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_LISTE_ID))));
                gorev.setTekrarId(cursor.getLong((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_TEKAR_ID))));
                gorevler.add(gorev);
            } while (cursor.moveToNext());
        }
        return gorevler;
    }

    public Gorev getirGorev(long id) {
        Gorev gorev = new Gorev();
        String selectQuery = "SELECT  * FROM " + GorevEntry.TABLO_ADI
                + " WHERE " + GorevEntry._ID + " = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            gorev.setGorevId(cursor.getLong((cursor.getColumnIndex(GorevEntry._ID))));
            gorev.setAciklama(cursor.getString((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_ACIKLAMA))));
            gorev.setGorevAdi(cursor.getString((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_GOREV_ADI))));
            gorev.setTelefonNumarasi(cursor.getString((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_TELEFON_NUMARASI))));
            gorev.setIkon(cursor.getString((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_IKON))));
            gorev.setFark(cursor.getInt((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_ZAMAN_FARKI))));
            gorev.setGorevBittiMi(cursor.getInt((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_GOREV_BITTIMI))));
            gorev.setSonaErmeSaati(cursor.getString((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_SONA_ERME_SAATI))));
            gorev.setSonaErmeGunu(cursor.getString((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_SONA_ERME_TARIHI))));
            gorev.setGorevListeId(cursor.getLong((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_LISTE_ID))));
            gorev.setTekrarId(cursor.getLong((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_TEKAR_ID))));
        }
        return gorev;
    }

    public List<GorevListe> getirButunGorevListeler(boolean hepsiMi) {
        List<GorevListe> listeler = new ArrayList<GorevListe>();
        String selectQuery;
        if (hepsiMi) {
            selectQuery = "SELECT  * FROM " + GorevListeEntry.TABLO_ADI;
        } else {
            selectQuery = "SELECT  * FROM " + GorevListeEntry.TABLO_ADI + " LIMIT 1";
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                GorevListe liste = new GorevListe();
                liste.setGorevListeId(cursor.getLong((cursor.getColumnIndex(GorevListeEntry._ID))));
                liste.setGorevListeAdi(cursor.getString((cursor.getColumnIndex(GorevListeEntry.SUTUN_ADI_LISTE_ADI))));
                listeler.add(liste);
            } while (cursor.moveToNext());
        }
        return listeler;
    }

    public boolean guncelleZamanFarki(Gorev gorev) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GorevEntry.SUTUN_ADI_ZAMAN_FARKI, gorev.getFark());
        return db.update(GorevEntry.TABLO_ADI, contentValues, GorevEntry._ID + " = ? ", new String[]{Long.toString(gorev.getGorevId())}) > 0;
    }

    public GorevListe getirGorevListe(long id) {
        GorevListe liste = new GorevListe();
        String selectQuery = "SELECT  * FROM " + GorevListeEntry.TABLO_ADI + " WHERE "
                + GorevListeEntry._ID + " = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            liste.setGorevListeId(cursor.getLong((cursor.getColumnIndex(GorevListeEntry._ID))));
            liste.setGorevListeAdi(cursor.getString((cursor.getColumnIndex(GorevListeEntry.SUTUN_ADI_LISTE_ADI))));
        }
        return liste;
    }

    public List<Tekrar> getirButunTekrarlar() {
        List<Tekrar> tekrarlar = new ArrayList<Tekrar>();
        String selectQuery = "SELECT  * FROM " + TekrarEntry.TABLO_ADI;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Tekrar tekrar = new Tekrar();
                tekrar.setTekrarId(cursor.getLong((cursor.getColumnIndex(TekrarEntry._ID))));
                tekrar.setTekrarAdi(cursor.getString((cursor.getColumnIndex(TekrarEntry.SUTUN_ADI_TEKRAR_ADI))));
                tekrarlar.add(tekrar);
            } while (cursor.moveToNext());
        }
        return tekrarlar;
    }

    public Tekrar getirTekrar(long id) {
        Tekrar tekrar = new Tekrar();
        String selectQuery = "SELECT  * FROM " + TekrarEntry.TABLO_ADI + " WHERE "
                + TekrarEntry._ID + " = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            tekrar.setTekrarId(cursor.getLong((cursor.getColumnIndex(TekrarEntry._ID))));
            tekrar.setTekrarAdi(cursor.getString((cursor.getColumnIndex(TekrarEntry.SUTUN_ADI_TEKRAR_ADI))));
        }
        return tekrar;
    }

    public List<Gorev> getirButunGorevler(GorevListe gorevListe) {
        List<Gorev> gorevler = new ArrayList<Gorev>();
        String selectQuery;
        selectQuery = "SELECT  * FROM " + GorevEntry.TABLO_ADI
                + " WHERE " + GorevEntry.SUTUN_ADI_LISTE_ID + " = " + gorevListe.getGorevListeId()
                + " ORDER BY " + GorevEntry.SUTUN_ADI_ZAMAN_FARKI + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Gorev gorev = new Gorev();
                gorev.setGorevId(cursor.getLong((cursor.getColumnIndex(GorevEntry._ID))));
                gorev.setGorevAdi(cursor.getString((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_GOREV_ADI))));
                gorev.setTelefonNumarasi(cursor.getString((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_TELEFON_NUMARASI))));
                gorev.setGorevBittiMi(cursor.getInt((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_GOREV_BITTIMI))));
                gorev.setFark(cursor.getInt((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_ZAMAN_FARKI))));
                gorev.setSonaErmeSaati(cursor.getString((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_SONA_ERME_SAATI))));
                gorev.setSonaErmeGunu(cursor.getString((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_SONA_ERME_TARIHI))));
                gorev.setIkon(cursor.getString((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_IKON))));
                gorev.setAciklama(cursor.getString((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_ACIKLAMA))));
                gorev.setGorevListeId(cursor.getLong((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_LISTE_ID))));
                gorev.setTekrarId(cursor.getLong((cursor.getColumnIndex(GorevEntry.SUTUN_ADI_TEKAR_ID))));
                gorevler.add(gorev);
            } while (cursor.moveToNext());
        }
        return gorevler;
    }
}
