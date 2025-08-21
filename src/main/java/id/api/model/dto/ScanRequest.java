package id.api.model.dto;

/***********************************************************************************************************
 * Author:  Ersa Yuda Pratama
 * Created: 22/07/2025 09:22
 * About this file : 
 *
 ***********************************************************************************************************/
public class ScanRequest {
    private String hasil;
    private String waktu;

    public String getHasil() {
        return hasil;
    }

    public void setHasil(String hasil) {
        this.hasil = hasil;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }
}
