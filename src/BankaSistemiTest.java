import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exception.InsufficientBalanceException;

import static org.junit.jupiter.api.Assertions.*;
 
class BankaSistemiTest {

    private BankaYoneticisi banka;

    @BeforeEach
    void setUp() {
        banka = new BankaYoneticisi();
    }

    // ==========================
    // 1️⃣ MÜŞTERİ KAYIT TESTİ
    // ==========================
    @Test
    void musteriKayitBasarili() {
        int eskiBoyut = banka.getMusteriler().size();

        banka.kayitOl("Ali Veli", "123456789012", "1234");

        assertEquals(eskiBoyut + 1, banka.getMusteriler().size());

        Musteri yeni = banka.getMusteriler().get(eskiBoyut);
        assertEquals("Ali Veli", yeni.getAd());
        assertEquals("123456789012", yeni.getTcNo());
        assertNotNull(yeni.getIban());
        assertEquals(0, yeni.getHesap().getBakiye());
    }

    // ==========================
    // 2️⃣ GİRİŞ TESTLERİ
    // ==========================
    @Test
    void girisBasarili() {
        Musteri m = banka.getMusteriler().get(0);

        boolean sonuc = banka.girisYap(m.getTcNo(), m.getSifre());

        assertTrue(sonuc);
        assertEquals(m, banka.getMevcutMusteri());
    }

    @Test
    void girisHataliSifre() {
        Musteri m = banka.getMusteriler().get(0);

        boolean sonuc = banka.girisYap(m.getTcNo(), "0000");

        assertFalse(sonuc);
        assertNull(banka.getMevcutMusteri());
    }

    @Test
    void girisBasarisizOluncaMevcutMusteriNullKalir() {
        Musteri m = banka.getMusteriler().get(0);

        banka.girisYap(m.getTcNo(), "yanlis");

        assertNull(banka.getMevcutMusteri());
    }

    // ==========================
    // 3️⃣ PARA YATIRMA TESTLERİ
    // ==========================
    @Test
    void paraYatirTest() {
        Musteri m = banka.getMusteriler().get(0);
        banka.setMevcutMusteri(m);

        double eskiBakiye = m.getHesap().getBakiye();
        m.getHesap().paraYatir(500);

        assertEquals(eskiBakiye + 500, m.getHesap().getBakiye());
        assertFalse(m.getHesap().getIslemler().isEmpty());
    }

    
    @Test
    void paraYatirSifirMiktarExceptionFirlatir() {
        Musteri m = banka.getMusteriler().get(0);
        banka.setMevcutMusteri(m);

        assertThrows(IllegalArgumentException.class,
                () -> m.getHesap().paraYatir(0));
    }


    // ==========================
    // 4️⃣ PARA ÇEKME TESTLERİ
    // ==========================
    @Test
    void paraCekBasarili() throws Exception {
        Musteri m = banka.getMusteriler().get(0);
        banka.setMevcutMusteri(m);

        double eskiBakiye = m.getHesap().getBakiye();
        m.getHesap().paraCek(200);

        assertEquals(eskiBakiye - 200, m.getHesap().getBakiye());
    }

    @Test
    void paraCekYetersizBakiye() {
        Musteri m = banka.getMusteriler().get(0);
        // Bakiyenin çok üstünde bir rakam verelim
        double limitUstuMiktar = m.getHesap().getBakiye() + 200000; 

        assertThrows(InsufficientBalanceException.class, () -> {
            m.getHesap().paraCek(limitUstuMiktar);
        }, "Bakiye yetersiz olduğunda exception fırlatılmalıydı");
    }

    // ==========================
    // 5️⃣ TRANSFER TESTLERİ
    // ==========================
    @Test
    void transferAyniBanka() throws Exception {
        Musteri gonderen = banka.getMusteriler().get(0);
        Musteri alici = banka.getMusteriler().get(1);

        banka.setMevcutMusteri(gonderen);

        double bakiyeGonderenEski = gonderen.getHesap().getBakiye();
        double bakiyeAliciEski = alici.getHesap().getBakiye();

        banka.transferYap(alici.getIban(), 500, false);

        assertEquals(bakiyeGonderenEski - 500, gonderen.getHesap().getBakiye());
        assertEquals(bakiyeAliciEski + 500, alici.getHesap().getBakiye());
    }

    @Test
    void transferEFT() throws Exception {
        Musteri gonderen = banka.getMusteriler().get(0);
        Musteri alici = banka.getMusteriler().get(1);

        banka.setMevcutMusteri(gonderen);

        double bakiyeGonderenEski = gonderen.getHesap().getBakiye();
        double bakiyeAliciEski = alici.getHesap().getBakiye();

        banka.transferYap(alici.getIban(), 100, true);

        assertEquals(bakiyeGonderenEski - 105, gonderen.getHesap().getBakiye());
        assertEquals(bakiyeAliciEski + 100, alici.getHesap().getBakiye());
    }

    @Test
    void transferHataliIban() {
        Musteri gonderen = banka.getMusteriler().get(0);
        banka.setMevcutMusteri(gonderen);

        Exception e = assertThrows(Exception.class, () -> {
            banka.transferYap("TR000000000000000000000000", 100, false);
        });

        assertEquals("Alici bulunamadi!", e.getMessage());
    }

    @Test
    void transferYetersizBakiye() {
        Musteri gonderen = banka.getMusteriler().get(0);
        banka.setMevcutMusteri(gonderen);

        Exception e = assertThrows(Exception.class, () -> {
            banka.transferYap(
                    banka.getMusteriler().get(1).getIban(),
                    1_000_000,
                    false);
        });
        assertEquals("Yetersiz bakiye!", e.getMessage());

    }

    // ==========================
    // 6️⃣ HESAP ÖZETİ TESTİ
    // ==========================
    @Test
    void hesapOzetiTest() {
        Musteri yeni = new Musteri(
                "Test Kullanici",
                "123456789012",
                "TR111111111111111111111111",
                "1234"
        );

        banka.getMusteriler().add(yeni);
        banka.setMevcutMusteri(yeni);

        assertNotNull(yeni.getHesap().getIslemler());
        assertTrue(yeni.getHesap().getIslemler().isEmpty());

        yeni.getHesap().paraYatir(500);

        assertFalse(yeni.getHesap().getIslemler().isEmpty());

        yeni.getHesap().islemleriGoster(); // console output
    }

    // ==========================
    // 7️⃣ EXCEPTION SINIFI TESTİ
    // ==========================
    @Test
    void testExceptionMessage() {
        String message = "Yetersiz bakiye";

        InsufficientBalanceException exception =
                new InsufficientBalanceException(message);

        assertEquals(message, exception.getMessage());
    }
}

    
   
	