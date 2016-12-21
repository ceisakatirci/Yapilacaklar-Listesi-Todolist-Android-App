package model;

/**
 * Created by isa on 18.12.2016.
 */

public class GorevBildirim {
    public int getGorevBildirimId() {
        return gorevBildirimId;
    }

    public GorevBildirim(int gorevBildirimId, String gorevBigirimAdi) {
        this.gorevBildirimId = gorevBildirimId;
        this.gorevBigirimAdi = gorevBigirimAdi;
    }

    public void setGorevBildirimId(int gorevBildirimId) {
        this.gorevBildirimId = gorevBildirimId;
    }

    private int gorevBildirimId;

    public String getGorevBigirimAdi() {
        return gorevBigirimAdi;
    }

    public void setGorevBigirimAdi(String gorevBigirimAdi) {
        this.gorevBigirimAdi = gorevBigirimAdi;
    }

    private String gorevBigirimAdi;
}
