package com.magicworldz.txt2mobi.kindle;

import com.magicworldz.txt2mobi.novel.Novel;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

public class KindleWritter {
    private static final String[] TEMPLATES = new String[]{"mykindlebook.html", "mykindlebook.opf",
        "style.css", "toc.html", "toc.ncx"};
    KindleWritter() {

    }

    public static KindleWritter newWritter() {
        return new KindleWritter();
    }

    public void write(Path base, Novel novel) throws IOException {
        if (!Files.exists(base)) {
            Files.createDirectories(base);
        }

        VelocityContext context = new VelocityContext();
        context.put("novel", novel);
        context.put("date", LocalDate.now().toString());

        var ve = createEngine();

        for (String template : TEMPLATES) {
            writeTemplate(ve, context, template, base);
        }

    }

    private void writeTemplate(VelocityEngine ve, VelocityContext vc, String template, Path base) throws IOException {
        var t = ve.getTemplate(template, "utf-8");
        Path file = base.resolve(template);
        System.out.println(String.format("Writing template %s to %s", template, file.toAbsolutePath()));
        var writer = Files.newBufferedWriter(file, Charset.forName("utf-8"));
        t.merge(vc, writer);
        writer.close();
    }

    private VelocityEngine createEngine() {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
        return ve;
    }

}
