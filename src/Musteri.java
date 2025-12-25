import com.Model.SavingsAccount;

public class Musteri {
    private String ad;
    private String tcNo;
    private String iban;
    private String sifre;
    private SavingsAccount vadeliHesap;

    public Musteri(String ad, String tcNo, String iban, String sifre) {
        this.ad = ad;
        this.tcNo = tcNo;
        this.iban = iban;
        this.sifre = sifre;
        // Her müşterinin kendine ait, IBAN'ı ile tanımlı bir hesabı olur
        this.vadeliHesap = new SavingsAccount(iban, 0); 
    }

    public String getTcNo() { 
    	return tcNo; }
    public String getAd() { 
    	return ad; }
    public String getSifre() {
    	return sifre; }
    public String getIban() {
    	return iban; }
    public SavingsAccount getHesap() {  
    	return vadeliHesap; }
}