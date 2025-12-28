

import java.util.Scanner;
import com.Model.CheckingAccount;
import com.Model.SavingsAccount;
import com.exception.InsufficientBalanceException;
import java.util.ArrayList;


import java.util.ArrayList;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.Scanner;

public class BankaYoneticisi {

    private ArrayList<Musteri> musteriler;
    private Musteri mevcutMusteri;
    private Scanner scanner;

    public BankaYoneticisi() {
        musteriler = new ArrayList<>();
        scanner = new Scanner(System.in);

        Musteri m1 = new Musteri("Burcu Yigit", "480110123456",
                "TR100000000000000000000001", "1234");
        m1.getHesap().paraYatir(10000);

        Musteri m2 = new Musteri("Tugberk Kocatekin", "467900200122",
                "TR100000000000000000000002", "4345");
        m2.getHesap().paraYatir(5000);

        musteriler.add(m1);
        musteriler.add(m2);
    }

    public void baslat() {
        while (true) {
            if (mevcutMusteri == null)
                girisEkrani();
            else
                islemMenusu();
        }
    }

    // ================= ANA MENU =================
    private void girisEkrani() {
        System.out.println("\n*** ANA MENU ***");
        System.out.println("1- Giris Yap");
        System.out.println("2- Kayit Ol");
        System.out.println("0- Cikis");
        System.out.print("Seciminiz: ");

        String secim = scanner.next();

        if (secim.equals("1")) girisYap();
        else if (secim.equals("2")) kayitOl();
        else if (secim.equals("0")) System.exit(0);
        else System.out.println("Gecersiz secim!");
    }

    // ================= GIRIS =================
    private void girisYap() {
        System.out.print("TC No'nuzu giriniz: ");
        String tc = scanner.next();

        for (Musteri m : musteriler) {
            if (m.getTcNo().equals(tc)) {
                int hak = 3;
                while (hak-- > 0) {
                    System.out.print("Sifrenizi giriniz: ");
                    if (m.getSifre().equals(scanner.next())) {
                        mevcutMusteri = m;
                        System.out.println("Hosgeldiniz " + m.getAd());
                        return;
                    }
                    System.out.println("Hatali sifre!");
                }
                return;
            }
        }
        System.out.println("Musteri bulunamadi!");
    }

    // ================= KAYIT =================
    private void kayitOl() {
        System.out.print("Ad Soyad giriniz: ");
        scanner.nextLine();
        String ad = scanner.nextLine();

        System.out.print("TC No giriniz: ");
        String tc = scanner.next();

        System.out.print("Sifre giriniz: ");
        String sifre = scanner.next();

        String iban = rastgeleIbanUret();

        musteriler.add(new Musteri(ad, tc, iban, sifre));

        System.out.println("\n[+] Kayit basarili!");
        System.out.println("Size atanan IBAN: " + iban);

        kayitSonuMenu();
    }

    // ================= MUSTERI MENU =================
    private void islemMenusu() {
        System.out.println("\n--- MUSTERI MENU ---");
        System.out.println("1- Para Yatir");
        System.out.println("2- Para Cek");
        System.out.println("3- Para Transferi");
        System.out.println("4- Hesap Ozeti");
        System.out.println("5- Sifre Degistir");
        System.out.println("0- Oturum Kapat");
        System.out.print("Seciminiz: ");

        try {
            String secim = scanner.next();

            switch (secim) {
                case "1":
                    System.out.print("Miktar: ");
                    mevcutMusteri.getHesap().paraYatir(scanner.nextDouble());
                    break;

                case "2":
                    System.out.print("Miktar: ");
                    mevcutMusteri.getHesap().paraCek(scanner.nextDouble());
                    break;

                case "3":
                    transferYap();
                    break;

                case "4":
                    mevcutMusteri.getHesap().islemleriGoster();
                    break;

                case "5":
                    sifreDegistir();
                    break;

                case "0":
                    mevcutMusteri = null;
                    System.out.println("Oturum kapatildi.");
                    break;

                default:
                    System.out.println("Gecersiz secim!");
            }
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    // ================= SIFRE DEGISTIR =================
    private void sifreDegistir() {
        System.out.print("Eski sifre: ");
        if (!mevcutMusteri.getSifre().equals(scanner.next())) {
            System.out.println("Hatali sifre!");
            return;
        }

        System.out.print("Yeni sifre: ");
        String s1 = scanner.next();
        System.out.print("Yeni sifre tekrar: ");
        String s2 = scanner.next();

        if (!s1.equals(s2)) {
            System.out.println("Sifreler uyusmuyor!");
            return;
        }

        mevcutMusteri.setSifre(s1);
        System.out.println("Sifre degistirildi.");
    }

    // ================= TRANSFER =================
    private void transferYap() throws Exception {
        System.out.print("Alici IBAN: ");
        String iban = scanner.next();

        for (Musteri m : musteriler) {
            if (m.getIban().equals(iban)) {
                System.out.print("Miktar: ");
                double miktar = scanner.nextDouble();

                boolean eftMi = !m.getIban().substring(2, 5)
                        .equals(mevcutMusteri.getIban().substring(2, 5));

                mevcutMusteri.getHesap().transfer(m.getHesap(), miktar, eftMi);

                System.out.println("Transfer basarili.");
                return;
            }
        }
        System.out.println("Alici bulunamadi!");
    }

    // ================= IBAN URET =================
    private String rastgeleIbanUret() {
        StringBuilder iban = new StringBuilder("TR");
        for (int i = 0; i < 24; i++) {
            iban.append((int) (Math.random() * 10));
        }
        return iban.toString();
    }

    // ================= KAYIT SONU =================
    private void kayitSonuMenu() {
        System.out.print("\nAna menuye donmek ister misiniz? (E/H): ");
        String secim = scanner.next();

        if (!secim.equalsIgnoreCase("E")) {
            System.out.println("Program kapatiliyor...");
            System.exit(0);
        }
    }

    // ==================== TEST VE OTOMATİK KULLANIM İÇİN EKLENEN METODLAR ====================
    public ArrayList<Musteri> getMusteriler() { return musteriler; }
    public Musteri getMevcutMusteri() { return mevcutMusteri; }
    public void setMevcutMusteri(Musteri m) { mevcutMusteri = m; }

    public void kayitOl(String ad, String tc, String iban) {
        musteriler.add(new Musteri(ad, tc, iban, "1234")); // test için sabit sifre
    }

    public boolean girisYap(String tc, String sifre) {
        for (Musteri m : musteriler) {
            if (m.getTcNo().equals(tc) && m.getSifre().equals(sifre)) {
                mevcutMusteri = m;
                return true;
            }
        }
        return false;
    }

    public void transferYap(String iban, double miktar, boolean eft) throws Exception {
        Musteri alici = null;
        for (Musteri m : musteriler) {
            if (m.getIban().equals(iban)) {
                alici = m;
                break;
            }
        }
        if (alici == null) throw new Exception("Alici bulunamadi!");
        mevcutMusteri.getHesap().transfer(alici.getHesap(), miktar, eft);
    }
}
