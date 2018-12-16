package com.magicworldz.txt2mobi.novel;

import com.magicworldz.txt2mobi.Txt2MobiRuntimeException;
import com.magicworldz.txt2mobi.util.EncodingDetect;
import com.magicworldz.txt2mobi.util.FileUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class NovelParser {
    private List<Pattern> titlePaterns = new ArrayList<>();

    NovelParser(List<Pattern> patterns) {
        this.titlePaterns = patterns;
    }

    public static NovelParser newParser(List<String> titlePatternStrs) {
        var patterns = titlePatternStrs.stream().map(Pattern::compile).collect(Collectors.toList());
        return new NovelParser(patterns);
    }

    public Novel parse(Path path) {
        Novel novel = new Novel();
        try {
            novel.setTitle(FileUtils.getFileName(path));
            String charset = EncodingDetect.getJavaEncode(path.toString());
            if (charset.equalsIgnoreCase("GB2312")) {
                charset = "GB18030";
                System.out.println("Promote charset encoding from GB2312 to GB18030");
            }
            System.out.println(charset);
            novel.setLanguage(charset);
            var reader = new BufferedReader(new InputStreamReader(new FileInputStream(path.toString()), charset) );
            String line = null;

            NovelChapter novelChapter = new NovelChapter("Made by MagicWorldZ");
            while((line = reader.readLine()) != null) {
                if (isChapterTitle(line)) {
                    novel.addChapter(novelChapter);
                    novelChapter = new NovelChapter(line);
                } else {
                    novelChapter.addLine(line);
                }
            }
            novel.addChapter(novelChapter);
            reader.close();
        } catch (IOException e) {
            throw new Txt2MobiRuntimeException("fail to detect file encoding", e.getCause());
        }
        return novel;
    }

    private boolean isChapterTitle(String line) {
        if (line.length() >= 60) {
            return false;
        }
        for (var p : titlePaterns) {
            var m = p.matcher(line.trim());
            if (m.find()) {
                return true;
            }
        }
        return false;
    }
}
