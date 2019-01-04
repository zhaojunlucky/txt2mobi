package com.magicworldz.txt2mobi.util;


import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    private FileUtils() {

    }

    public static String getFileName(Path path) {
        String fileName = path.getFileName().toString();
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    public static List<String> readAllLines(Path path) {
        var lines = new ArrayList<String>();
        String charset = EncodingDetect.getJavaEncode(path.toString());
        try(var reader = new BufferedReader(new InputStreamReader(new FileInputStream(path.toString()), charset))) {
            String line = null;

            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    public static String getJarPath(Class c) {
        String appPath = c.getProtectionDomain().getCodeSource().getLocation().getPath();
        try {
            appPath = URLDecoder.decode(appPath, "UTF-8");
            if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                appPath = appPath.substring(1);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Path p = Paths.get(appPath).getParent();
        return p.toString();
    }
}
