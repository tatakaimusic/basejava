package com.urise.webapp;
public class MainConfig {
    public static void main(String[] args) {
        System.out.println(Config.get().getStorageDir().getAbsolutePath());
    }
}
