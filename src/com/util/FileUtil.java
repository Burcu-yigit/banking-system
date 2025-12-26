package com.util;

import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

    public static void dosyayaYaz(String metin) {
        try (FileWriter fw = new FileWriter("islemler.txt", true)) {
            fw.write(metin + "\n");
        } catch (IOException e) {
            System.out.println("Dosya yazma hatası!");
        }
    }
}
