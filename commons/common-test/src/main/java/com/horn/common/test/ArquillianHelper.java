package com.horn.common.test;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

/**
 * @author lesinsa
 */
public final class ArquillianHelper {
    private ArquillianHelper() {
        // nothing to do
    }

    public static File[] getMavenLibraries(String pom) {
        return getMavenLibraries(new File(pom));
    }

    public static File[] getMavenLibraries(File pom) {
        return Maven.resolver()
                .loadPomFromFile(pom)
                .importCompileAndRuntimeDependencies()
                .resolve()
                .withoutTransitivity()
                .asFile();
    }

    public static File[] getCommonLibs() {
        String version = getProjectVersion();
        return Maven.resolver()
                .resolve("com.horn.commons:common-config:" + version, "com.horn.commons:common-jee:" + version)
                .withoutTransitivity()
                .asFile();
    }

    public static File[] getSecurityLib() {
        String version = getProjectVersion();
        return Maven.resolver()
                .resolve("com.horn.commons:auth:" + version)
                .withoutTransitivity()
                .asFile();
    }

    public static File[] getCommonLib(String name) {
        String version = getProjectVersion();
        return Maven.resolver()
                .resolve(String.format("com.horn.commons:%s:%s", name, version))
                .withTransitivity()
                .asFile();
    }

    public static String getProjectVersion() {
        Properties properties = new Properties();
        try {
            properties.load(ClassLoader.getSystemResourceAsStream("app.properties"));
        } catch (IOException e) {
            throw new IllegalStateException("error load properties", e);
        }
        return properties.getProperty("version");
    }

    public static WebArchive addWebResourcesTo(WebArchive archive, String webappSrc) {
        final File webAppDirectory = new File(webappSrc);
        Collection<File> files = FileUtils.listFiles(webAppDirectory, new IOFileFilter() {
                    @Override
                    public boolean accept(File file) {
                        return true;
                    }

                    @Override
                    public boolean accept(File file, String s) {
                        return false;
                    }
                }, new IOFileFilter() {
                    @Override
                    public boolean accept(File file) {
                        return !"WEB-INF".equals(file.getName());
                    }

                    @Override
                    public boolean accept(File file, String s) {
                        return false;
                    }
                }
        );


        for (File file : files) {
            if (!file.isDirectory()) {
                archive.addAsWebResource(file, file.getPath().substring(webappSrc.length()));
            }
        }
        return archive;
    }
}
