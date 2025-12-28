
	import java.util.ArrayList;

public class Hesap {
    private double bakiye;
    private double gunlukCekilen;
    private ArrayList<String> islemler;

   
    public Hesap() {
        this.bakiye = 0;
        this.gunlukCekilen = 0;
        this.islemler = new ArrayList<>();
    }

    public double getBakiye() {
        return bakiye;
    }

    public ArrayList<String> getIslemler() {
        return islemler;
    }

   
    public void paraYatir(double miktar) {
        if (miktar <= 0)
            throw new IllegalArgumentException("Miktar pozitif olmali!");
        bakiye += miktar;
        islemler.add("Para yatirildi: +" + miktar);
    }

   
    public void paraCek(double miktar) throws Exception {
        if (miktar <= 0)
            throw new Exception("Miktar pozitif olmali!");
        if (miktar > bakiye)
            throw new Exception("Yetersiz bakiye!");
        if (gunlukCekilen + miktar > 5000)
            throw new Exception("Gunluk 5000 TL cekme limitini astiniz!");

        bakiye -= miktar;
        gunlukCekilen += miktar;
        islemler.add("Para cekildi: -" + miktar);
    }

   //Transfer işlemi
    public void transfer(Hesap alici, double miktar, boolean eftMi) throws Exception {
        double komisyon = eftMi ? 5 : 0;

        if (miktar <= 0)
            throw new Exception("Miktar pozitif olmali!");
        if (miktar + komisyon > bakiye)
            throw new Exception("Yetersiz bakiye (komisyon dahil)");

        bakiye -= (miktar + komisyon);
        alici.bakiye += miktar;

        islemler.add("Transfer gonderildi: -" + miktar +
                (eftMi ? " (EFT komisyonu: 5 TL)" : ""));
        alici.islemler.add("Transfer alindi: +" + miktar);
    }
//Günlük limit sıfırlama
    public void gunlukLimitSifirla() {
        gunlukCekilen = 0;
    }

  //Hesap özeti
    public void islemleriGoster() {
        System.out.println("\n---- HESAP OZETI ----");
        for (String i : islemler) {
            System.out.println(i);
        }
        System.out.println("Guncel Bakiye: " + bakiye);
    }
}

