package com.urise.webapp.model;

import java.io.Serial;
import java.util.Objects;


public class TextSection extends Section {
    @Serial
    private static final long serialVersionUID = -313169696235072244L;
    private String content;

    public TextSection(String content) {
        Objects.requireNonNull(content, "Content must not be null");
        this.content = content;
    }

    public TextSection() {
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextSection that = (TextSection) o;
        return content.equals(that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
}
