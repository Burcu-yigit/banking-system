import com.Model.SavingsAccount;

public class Musteri {
    private String ad;
    private String tcNo;
    private String iban;
    private String sifre;
    private Hesap hesap;

    public Musteri(String ad, String tcNo, String iban, String sifre) {
        this.ad = ad;
        this.tcNo = tcNo;
        this.iban = iban;
        this.sifre = sifre;
        this.hesap = new Hesap();
    }

    public String getAd() {
        return ad;
    }

    public String getTcNo() {
        return tcNo;
    }

    public String getIban() {
        return iban;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public Hesap getHesap() {
        return hesap;
    }
}
