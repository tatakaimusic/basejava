package com.urise.webapp.model;


public enum SectionType {
    PERSONAL("ЛИЧНЫЕ КАЧЕСТВА"),
    OBJECTIVE("ПОЗИЦИЯ"),
    ACHIEVEMENT("ДОСТИЖЕНИЯ"),
    QUALIFICATIONS("КВАЛИФИКАЦИЯ"),
    EXPERIENCE("ОПЫТ"),
    EDUCATION("ОБРАЗОВАНИЕ");

    private final String title;

    SectionType(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

}
