import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.Model.*;
import static org.junit.jupiter.api.Assertions.*;
import com.exception.*;
import com.Model.SavingsAccount;

public class BankaSistemiTest {

    private BankaYoneticisi banka;
    private SavingsAccount vadeliHesap;

    @BeforeEach
    void setUp() {
        banka = new BankaYoneticisi();
        banka.kayitOl("Ali Veli", "123456789012", "1234");
        banka.kayitOl("Ayşe Fatma", "987654321098", "5678");

        vadeliHesap = new SavingsAccount("VA123", 1000.0);
    }

    // ==========================
    // MÜŞTERİ & GİRİŞ TESTLERİ
    // ==========================

    @Test
    void musteriKayitBasarili() {
        int eskiBoyut = banka.getMusteriler().size();

        banka.kayitOl("Mehmet Can", "111122223333", "9999");

        assertEquals(eskiBoyut + 1, banka.getMusteriler().size());
        Musteri yeni = banka.getMusteriler().get(eskiBoyut);

        assertEquals("Mehmet Can", yeni.getAd());
        assertEquals("111122223333", yeni.getTcNo());
        assertNotNull(yeni.getIban());
        assertEquals(0, yeni.getHesap().getBakiye());
    }

    @Test
    void girisBasarili() {
        Musteri m = banka.getMusteriler().get(0);
        assertTrue(banka.girisYap(m.getTcNo(), m.getSifre()));
        assertEquals(m, banka.getMevcutMusteri());
    }

    @Test
    void girisHataliSifre() {
        Musteri m = banka.getMusteriler().get(0);
        assertFalse(banka.girisYap(m.getTcNo(), "0000"));
        assertNull(banka.getMevcutMusteri());
    }

    // ==========================
    // PARA YATIRMA
    // ==========================

    @Test
    void paraYatirBasarili() {
        Musteri m = banka.getMusteriler().get(0);
        double eski = m.getHesap().getBakiye();

        m.getHesap().paraYatir(500);

        assertEquals(eski + 500, m.getHesap().getBakiye());
        assertFalse(m.getHesap().getIslemler().isEmpty());
    }

    @Test
    void paraYatirSifirMiktarException() {
        Musteri m = banka.getMusteriler().get(0);
        assertThrows(IllegalArgumentException.class,
                () -> m.getHesap().paraYatir(0));
    }

    @Test
    void paraYatirNegatifMiktarException() {
        Musteri m = banka.getMusteriler().get(0);
        assertThrows(IllegalArgumentException.class,
                () -> m.getHesap().paraYatir(-100));
    }

    // ==========================
    // PARA ÇEKME
    // ==========================

    @Test
    void paraCekBasarili() throws Exception {
        Musteri m = banka.getMusteriler().get(0);
        m.getHesap().paraYatir(500); // DÜZELTME

        double eski = m.getHesap().getBakiye();
        m.getHesap().paraCek(200);

        assertEquals(eski - 200, m.getHesap().getBakiye());
    }

   

    @Test
    void paraCekSifirMiktarException() {
        Musteri m = banka.getMusteriler().get(0);
        assertThrows(IllegalArgumentException.class,
                () -> m.getHesap().paraCek(0));
    }

    // ==========================
    // TRANSFER
    // ==========================

    @Test
    void transferAyniBanka() throws Exception {
        Musteri g = banka.getMusteriler().get(0);
        Musteri a = banka.getMusteriler().get(1);
        banka.setMevcutMusteri(g);

        g.getHesap().paraYatir(1000); // DÜZELTME

        double gEski = g.getHesap().getBakiye();
        double aEski = a.getHesap().getBakiye();

        banka.transferYap(a.getIban(), 500, false);

        assertEquals(gEski - 500, g.getHesap().getBakiye());
        assertEquals(aEski + 500, a.getHesap().getBakiye());
    }

    @Test
    void transferEFTKomisyonlu() throws Exception {
        Musteri g = banka.getMusteriler().get(0);
        Musteri a = banka.getMusteriler().get(1);
        banka.setMevcutMusteri(g);

        g.getHesap().paraYatir(500); // DÜZELTME

        double gEski = g.getHesap().getBakiye();
        banka.transferYap(a.getIban(), 100, true);

        assertEquals(gEski - 105, g.getHesap().getBakiye(), 0.01);
    }

    @Test
    void transferHataliIban() {
        banka.setMevcutMusteri(banka.getMusteriler().get(0));

        Exception e = assertThrows(Exception.class, () ->
                banka.transferYap("TR000000000000000000000000", 100, false)
        );

        assertEquals("Alici bulunamadi!", e.getMessage());
    }

   

    // ==========================
    // HESAP ÖZETİ
    // ==========================

    @Test
    void hesapOzetiIslemEklendiktenSonraBosDegil() {
        Musteri m = banka.getMusteriler().get(0);
        m.getHesap().paraYatir(100);

        assertFalse(m.getHesap().getIslemler().isEmpty());
        assertDoesNotThrow(() -> m.getHesap().islemleriGoster());
    }

    // ==========================
    // EXCEPTION SINIFI
    // ==========================

    @Test
    void exceptionMesajiDogru() {
        InsufficientBalanceException e =
                new InsufficientBalanceException("Test Mesaj");
        assertEquals("Test Mesaj", e.getMessage());
    }

    // ==========================
    // VADELİ (SAVINGS ACCOUNT)
    // ==========================

    @Test
    void vadeliParaCekBasarili() throws Exception {
        double eski = vadeliHesap.getBakiye();
        vadeliHesap.paraCek(500);

        assertEquals(eski - 500, vadeliHesap.getBakiye(), 0.01);
    }

    @Test
    void vadeliParaCekYetersiz() {
        InsufficientBalanceException e = assertThrows(
                InsufficientBalanceException.class,
                () -> vadeliHesap.paraCek(5000)
        );

        assertEquals("Yetersiz bakiye!", e.getMessage());
    }

    @Test
    void vadeliFaizHesapla() {
        double eski = vadeliHesap.getBakiye();
        double oran = vadeliHesap.getFaizOrani();

        vadeliHesap.faizHesapla();

        assertEquals(eski + eski * oran, vadeliHesap.getBakiye(), 0.01);
    }

    @Test
    void vadeliFaizUstUste() {
        double oran = vadeliHesap.getFaizOrani();
        double ilk = vadeliHesap.getBakiye();

        vadeliHesap.faizHesapla();
        vadeliHesap.faizHesapla();

        double beklenen = ilk * (1 + oran) * (1 + oran);
        assertEquals(beklenen, vadeliHesap.getBakiye(), 0.01);
    }

    @Test
    void vadeliFaizOraniSetterGetter() {
        vadeliHesap.setFaizOrani(0.1);
        assertEquals(0.1, vadeliHesap.getFaizOrani(), 0.001);
    }
}
