package com.back;

public class WiseSaying implements Comparable<WiseSaying>{
    private Integer id;
    private String wiseSaying;
    private String author;

    public WiseSaying(Integer id, String wiseSaying, String author) {
        this.id = id;
        this.wiseSaying = wiseSaying;
        this.author = author;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWiseSaying() {
        return wiseSaying;
    }

    public void setWiseSaying(String wiseSaying) {
        this.wiseSaying = wiseSaying;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public int compareTo(WiseSaying o) {
        return this.id > o.id ? -1 : this.id < o.id ? 1 : 0;
    }
}
