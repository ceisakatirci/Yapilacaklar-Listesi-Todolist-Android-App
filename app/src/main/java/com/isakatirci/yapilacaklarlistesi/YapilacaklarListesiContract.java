package com.isakatirci.yapilacaklarlistesi;

import android.provider.BaseColumns;

/**
 * Created by isa on 16.12.2016.
 */

public final class YapilacaklarListesiContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private YapilacaklarListesiContract() {
    }

    /* Inner class that defines the table contents */
    public static class GorevEntry implements BaseColumns {
        public static final String TABLO_ADI = " Gorev";
        public static final String SUTUN_ADI_GOREV_ADI = "GorevAdi";
        public static final String SUTUN_ADI_TELEFON_NUMARASI = "TelefonNumarasi";
        public static final String SUTUN_ADI_GOREV_BITTIMI = "GorevBittiMi";
        public static final String SUTUN_ADI_SONA_ERME_TARIHI = "SonaErmeTarihi";
        public static final String SUTUN_ADI_SONA_ERME_SAATI = "SonaErmeSaati";
        public static final String SUTUN_ADI_ACIKLAMA = "Aciklama";
        public static final String SUTUN_ADI_TEKAR_ID = "TekrarId";
        public static final String SUTUN_ADI_ZAMAN_FARKI= "ZamanFarki";
        public static final String SUTUN_ADI_LISTE_ID = "GorevListeId";
        public static final String SUTUN_ADI_IKON = "Ikon";


        public class SUTUN_ADI_IKONADI {
        }
    }
    public static class GorevListeEntry implements BaseColumns {
        public static final String TABLO_ADI = " GorevListe";
        public static final String SUTUN_ADI_LISTE_ADI = "GorevListeAdi";
    }

    public static class TekrarEntry implements BaseColumns {
        public static final String TABLO_ADI = " Tekrar";
        public static final String SUTUN_ADI_TEKRAR_ADI = "TekrarAdi";
    }

}
