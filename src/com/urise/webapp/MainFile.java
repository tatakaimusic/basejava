package com.urise.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainFile {
    public static String path = "E:\\Рабочий стол\\kurs\\basejava2\\src\\com\\urise\\webapp";

    public static void main(String[] args) {
        File file2 = new File(path);

        String filePath = ".\\.gitignore";

        try (FileInputStream fis = new FileInputStream(filePath)) {
            System.out.println(fis.read());
        } catch (IOException e) {
            throw new RuntimeException("error", e);
        }


        showAllStringDirectory(file2);
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

    public static void showAllStringDirectory(File dir) {
        boolean check = false;
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!check) {
                        System.out.print("  ");
                    }
                    System.out.println("File: " + file.getName());
                } else if (file.isDirectory()) {
                    check = true;
                    System.out.println("Directory: " + file.getName());
                    showAllStringDirectory(file);
                }
            }
        }
    }
}