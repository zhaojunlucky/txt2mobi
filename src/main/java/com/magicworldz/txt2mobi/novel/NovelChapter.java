package com.magicworldz.txt2mobi.novel;

import java.util.LinkedList;
import java.util.List;

public class NovelChapter {
    private List<String> lines = new LinkedList();
    private String title;

    NovelChapter(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addLine(String line) {
        lines.add(line);
    }

    public List<String> getLines() {
        return lines;
    }
}
