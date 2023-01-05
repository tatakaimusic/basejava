package com.urise.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainFile {
    public static String path = "E:\\Рабочий стол\\kurs\\basejava2\\src\\com\\urise\\webapp\\";

    public static void main(String[] args) {
        File file2 = new File(path);

        String filePath = ".\\.gitignore";

        try (FileInputStream fis = new FileInputStream(filePath)) {
            System.out.println(fis.read());
        } catch (IOException e) {
            throw new RuntimeException("error", e);
        }


        try {
            showAllStringDirectory(file2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void showAllFileDirectory(File file) {
        for (File dir : file.listFiles()) {
            if (dir.isDirectory()) {
                System.out.println("Directory");
            }
            System.out.println(dir);
            if (dir.isDirectory()) {
                showAllFileDirectory(dir);
            }
        }
    }

    public static void showAllStringDirectory(File file) throws IOException {
        String[] list = file.list();
        if (list != null) {
            for (String name : list) {
                File dir = new File(path + name);
                System.out.println(name);
                if (dir.isDirectory()) {
                    path += name + "\\";
                    showAllStringDirectory(dir);
                    path = "E:\\Рабочий стол\\kurs\\basejava2\\src\\com\\urise\\webapp\\";
                }
            }
        }
    }
}