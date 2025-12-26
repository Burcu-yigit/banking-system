

import java.util.Scanner;
import com.Model.CheckingAccount;
import com.Model.SavingsAccount;
import com.exception.InsufficientBalanceException;
import java.util.ArrayList;


import java.util.ArrayList;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.Scanner;

public class BankaYoneticisi {
    private ArrayList<Musteri> musteriler;
    private Musteri mevcutMusteri;
    private Scanner scanner;

    public BankaYoneticisi() {
        this.musteriler = new ArrayList<>();
        this.scanner = new Scanner(System.in);

        // HAZIR KAYITLI MÜŞTERİLER
        Musteri m1 = new Musteri("Burcu Yugit", "480110123456", "TR123456789012345678901234", "1234");
        m1.getHesap().paraYatir(10000); 
        musteriler.add(m1);

        Musteri m2 = new Musteri("Tugberk Kocatekin", "467900200122", "TR123456789009876543456789", "4345");
        m2.getHesap().paraYatir(5000);
        musteriler.add(m2);

        Musteri m3 = new Musteri("Ahmet Yilmaz", "111111111112", "TR999999999999999999999999", "9999");
        m3.getHesap().paraYatir(7500);
        musteriler.add(m3);
    }

    public void baslat() {
        while (true) {
            if (mevcutMusteri == null) {
                girisEkrani();
            } else {
                islemMenusu();
            }
        }
    }

    private void girisEkrani() {
        System.out.println("*** Ana Ekran ***");
        System.out.println("1- Giris Yap\n2- Yeni Kayit Ol\n0- Sistemi Kapat");
        System.out.print("Lütfen gerçekleştirmek istediğiniz işlemi seçiniz: ");
        String secim = scanner.next();

        if (secim.equals("1")) girisYap();
        else if (secim.equals("2")) kayitOl();
        else if (secim.equals("0")) System.exit(0);
        else System.out.println("[!] Gecersiz secim.");
    }

    private void girisYap() {
        System.out.print("TC Numaranizi giriniz: ");
        String tc = scanner.next();
        
        Musteri bulunan = null;
        for (Musteri m : musteriler) {
            if (m.getTcNo().equals(tc)) {
                bulunan = m;
                break;
            }
        }

        if (bulunan == null) {
            System.out.println("[!] Musteri bulunamadi.");
            return;
        }

        int hak = 3;
        while (hak > 0) {
            System.out.print("Sifre (" + hak + " hak): ");
            String sifre = scanner.next();
            if (bulunan.getSifre().equals(sifre)) {
                mevcutMusteri = bulunan;
                System.out.println("\n[+] Basariyla giris yapildi. Hosgeldiniz " + bulunan.getAd());
                return;
            }
            hak--;
            System.out.println("[!] Hatali sifre.");
        }
        System.out.println("[!] 3 kez hatali giris! Menuye donuluyor.");
    }

    private void kayitOl() {
        System.out.println("\n--- YENI KAYIT ---");
        String tc;
        while (true) {
            System.out.print("TC No (12 Hane): ");
            tc = scanner.next();
            if (tc.length() == 12 && tc.matches("[0-9]+")) break;
            System.out.println("[!] HATA: TC 12 haneli rakam olmalidir!");
        }

        System.out.print("Ad Soyad: ");
        scanner.nextLine();
        String ad = scanner.nextLine();

        String iban;
        while (true) {
            System.out.print("IBAN (TR ile baslayan 26 hane): ");
            iban = scanner.next();
            if (iban.startsWith("TR") && iban.length() == 26) break;
            System.out.println("[!] HATA: IBAN gecersiz formatta!");
        }

        System.out.print("Sifre Belirleyin: ");
        String sifre = scanner.next();

        musteriler.add(new Musteri(ad, tc, iban, sifre));
        System.out.println("[+] Kayit basarili! Sayin " + ad + " giris yapabilirsiniz.");
    }

    private void islemMenusu() {
        System.out.println("\n--- MUSTERI PANELI: " + mevcutMusteri.getAd() + " ---");
        System.out.println("1- Para Yatir\n2- Faiz Islet\n3- Para Transferi\n4- Hesap Ozeti\n0- Cikis");
        System.out.print("Secim: ");
        
        String secim = scanner.next();
        try {
            switch (secim) {
                case "1":
                    System.out.print("Miktar: ");
                    double m = scanner.nextDouble();
                    mevcutMusteri.getHesap().paraYatir(m);
                    System.out.println("Yeni Bakiye: " + mevcutMusteri.getHesap().getBakiye());
                    break;
                case "2":
                    mevcutMusteri.getHesap().faizHesapla();
                    System.out.println("Islem tamamlandi.");
                    break;
                case "3":
                    transferYap();
                    break;
                case "4":
                    mevcutMusteri.getHesap().islemleriGoster();
                    break;
                case "0":
                    System.out.println("Oturum kapatılıyor ve sistemden çıkılıyor...");
                    System.exit(0); // Bu satır programı tamamen bitirir, başa döndürmez.
                    break;
                default:
                    System.out.println("[!] Gecersiz secim.");
            }
        } catch (Exception e) {
            System.out.println("[!] Hata: " + e.getMessage());
        }
    }

    private void transferYap() throws Exception {
        System.out.print("Alici IBAN: ");
        String hedefIban = scanner.next();

        Musteri alici = null;
        for (Musteri m : musteriler) {
            if (m.getIban().equals(hedefIban)) {
                alici = m;
                break;
            }
        }

        if (alici == null) {
            System.out.println("[!] Alici bulunamadi. Sadece kayitli müşterilere gonderim yapilabilir.");
            return;
        }

        System.out.print(alici.getAd() + " kisisine miktar: ");
        double miktar = scanner.nextDouble();

        System.out.print("Onayliyor musunuz? (E/H): ");
        if (scanner.next().equalsIgnoreCase("E")) {
            System.out.print("Guvenlik icin sifreniz: ");
            if (mevcutMusteri.getSifre().equals(scanner.next())) {
                mevcutMusteri.getHesap().transfer(alici.getHesap(), miktar);
                System.out.println("[+] Transfer basarili. Aliciya aktarildi.");
            } else {
                System.out.println("[!] Hatali sifre!");
            }
        }
    }
}