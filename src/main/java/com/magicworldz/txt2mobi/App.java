package com.magicworldz.txt2mobi;

import com.magicworldz.txt2mobi.kindle.KindleWritter;
import com.magicworldz.txt2mobi.novel.NovelParser;
import com.magicworldz.txt2mobi.util.FileUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length == 0) {
            System.out.println("usage: java -jar <app.jar> <txt file>");
            System.exit(1);
        }
        String appPath = FileUtils.getJarPath(App.class);
        System.out.println(String.format("app path: %s", appPath));
        Path configFile = Paths.get(appPath, "chapters-setting.txt");

        Set<String> patterns = new HashSet<>();
        patterns.add("^第\\d+章.*");

        loadPatterns(configFile, patterns);
        System.out.println(String.format("load %d chapter patterns", patterns.size()));

        var path = Paths.get(args[0]);
        var outPath = Paths.get(path.getParent().toString(), FileUtils.getFileName(path));
        var novel = NovelParser.newParser(patterns.stream().collect(Collectors.toList())).parse(path);
        System.out.println(String.format("Total found %d chapters", novel.getChapters().size()));
        for(int i = 0; i < Math.min(8, novel.getChapters().size()); i++) {
            System.out.println(String.format("    %s", novel.getChapters().get(i).getTitle()));
        }
        System.out.println("Press enter to continue, Ctrl+C to exit...");
        System.in.read();
        var kw = KindleWritter.newWritter();
        kw.write(outPath, novel);
        kindlegen(appPath, outPath, novel.getTitle());
    }

    private static void kindlegen(String appPath, Path outPath, String name) throws IOException, InterruptedException {
        String kindlegenPath;

        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            kindlegenPath = "kindlegen/kindlegen.exe";
        } else {
            kindlegenPath = "kindlegen/kindlegen";
        }

        Path kindle = Paths.get(appPath, kindlegenPath);
        if (kindle.toFile().exists()) {
            System.out.println(String.format("%s exists, starting convert to mobi", kindle.toAbsolutePath()));
            Process process = Runtime.getRuntime().exec(String.format("%s %s -o %s.mobi -locale en", kindle.toAbsolutePath(),
                    outPath.resolve("mykindlebook.opf").toString(), name));
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String line;

            while((line = br.readLine()) != null ) {
                System.out.println(line);
            }

            isr.close();
            br.close();
            is.close();
            process.waitFor();

        } else {
            System.out.println(String.format("%s doesn't exist", kindle.toAbsolutePath()));
        }
    }

    private static void loadPatterns(Path configFile, Set<String> patterns) {
        if (configFile.toFile().exists()) {
            System.out.println(String.format("load chapters setting from %s", configFile.toAbsolutePath()));
            var lines = FileUtils.readAllLines(configFile);
            lines.forEach(line -> {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    patterns.add(line);
                }
            });

        } else {
            System.out.println(String.format("no chapters setting found %s", configFile.toAbsolutePath()));
        }
    }
}
