package model;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by isa on 16.12.2016.
 */

public class Gorev {
    private long gorevId;
    private String gorevAdi;
    private int gorevBittiMi;
    private String sonaErmeGunu;
    private String sonaErmeSaati;
    private String telefonNumarasi;
    private String aciklama;
    private long tekrarId;
    private long gorevListeId;
    private String ikon;
    private long fark;

    public long getFark() {
        return fark;
    }

    public void setFark(long fark) {
        this.fark = fark;
    }


    public String getTelefonNumarasi() {
        return telefonNumarasi;
    }

    public void setTelefonNumarasi(String telefonNumarasi) {
        this.telefonNumarasi = telefonNumarasi;
    }

    public String getIkon() {
        return ikon;
    }

    public void setIkon(String ikon) {
        this.ikon = ikon;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public long getGorevId() {
        return gorevId;
    }

    public void setGorevId(long gorevId) {
        this.gorevId = gorevId;
    }

    public String getGorevAdi() {
        return gorevAdi;
    }

    public void setGorevAdi(String gorevAdi) {
        this.gorevAdi = gorevAdi;
    }

    public int getGorevBittiMi() {
        return gorevBittiMi;
    }

    public void setGorevBittiMi(int gorevBittiMi) {

        this.gorevBittiMi = gorevBittiMi;
    }

    public String getSonaErmeGunu() {
        return sonaErmeGunu;
    }

    public void setSonaErmeGunu(String sonaErmeGunu) {
        if (sonaErmeGunu != null && sonaErmeGunu.length() != 0) {
            this.sonaErmeGunu = sonaErmeGunu;
        }
        else  {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            this.sonaErmeGunu = sdf.format(cal.getTime());

        }

    }

    public String getSonaErmeSaati() {
        return sonaErmeSaati;
    }

    public void setSonaErmeSaati(String sonaErmeSaati) {
        if (sonaErmeSaati != null && sonaErmeSaati.length() != 0) {

            this.sonaErmeSaati=sonaErmeSaati;
        }
        else
        {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            this.sonaErmeSaati = sdf.format(cal.getTime());
        }

    }

    public long getTekrarId() {
        return tekrarId;
    }

    public void setTekrarId(long tekrarId) {
        this.tekrarId = tekrarId;
    }

    public long getGorevListeId() {
        return gorevListeId;
    }

    public void setGorevListeId(long gorevListeId) {
        this.gorevListeId = gorevListeId;
    }

}
