package com.magicworldz.txt2mobi.novel;

import java.util.LinkedList;
import java.util.List;

public class Novel {
    private String title;
    private String language;
    private List<NovelChapter> chapters = new LinkedList<>();

    Novel() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void addChapter(NovelChapter novelChapter) {
        chapters.add(novelChapter);
    }

    public List<NovelChapter> getChapters() {
        return chapters;
    }
}
