package org.lambico.datatest.json;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import sun.net.www.protocol.file.FileURLConnection;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * Utility class to lookup files within a package which can be located in a folder or in a jar.
 * Based on this code: https://stackoverflow.com/a/22462785/379173
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonScanner {

    static final String EXT = "json";
    private static JsonScanner ref = null;

    public static JsonScanner getInstance() {
        if (ref == null) {
            ref = new JsonScanner();
        }
        return ref;
    }

    public List<String> listFiles(String path) throws IOException {
        ArrayList<String> result = new ArrayList<>();
        //define the class-loader
        final ClassLoader loader = Thread.currentThread()
                .getContextClassLoader();

        //switch if folder or jar
        Enumeration<URL> resources = loader.getResources(path
                .replace('.', '/'));
        URLConnection connection;
        for (URL url; resources.hasMoreElements()
                && ((url = resources.nextElement()) != null); ) {
            connection = url.openConnection();

            if (connection instanceof JarURLConnection) {
                checkJarFile((JarURLConnection) connection, path, result);
            } else if (connection instanceof FileURLConnection) {
                checkDirectory(
                        new File(URLDecoder.decode(url.getPath(),
                                "UTF-8")), path, result);
            } else {
                throw new RuntimeException("invalid connection: " + connection.toString());
            }
        }
        return result;
    }

    void checkJarFile(JarURLConnection connection, String path, List<String> list) throws IOException {
        final JarFile jarFile = connection.getJarFile();
        final Enumeration<JarEntry> entries = jarFile.entries();
        ArrayList<JarEntry> jarEntries = Collections.list(entries);
        List<String> candidates = jarEntries.stream()
                .map(JarEntry::getName)
                .filter(s -> s.startsWith(path))
                .filter(s -> s.endsWith(EXT))
                .collect(Collectors.toList());
        list.addAll(candidates);
    }

    void checkDirectory(File directory, String path, List<String> list) throws IOException {
        if (directory.exists() && directory.isDirectory()) {
            List<String> files = Files.list(directory.toPath())
                    .filter(Files::isRegularFile)
                    .filter(p -> !Files.isDirectory(p))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(s -> s.endsWith(EXT))
                    .map(p -> Paths.get(path, p))
                    .map(Path::toString)
                    .collect(Collectors.toList());
            list.addAll(files);
        }
    }

}