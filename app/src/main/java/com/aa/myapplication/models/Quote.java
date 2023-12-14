package com.aa.myapplication.models;
public class Quote {
    private String content;
    private String author;

    public Quote(String content, String author) {
        this.content = content;
        this.author = author;
    }

    @Override
    public String toString() {
        return author + " ' " + content + " ' ";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
