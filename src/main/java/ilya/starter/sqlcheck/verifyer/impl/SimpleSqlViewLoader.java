package ilya.starter.sqlcheck.verifyer.impl;

import ilya.starter.sqlcheck.userapi.SqlBind;
import ilya.starter.sqlcheck.util.PathConverterUtils;
import ilya.starter.sqlcheck.verifyer.SqlViewLoader;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SimpleSqlViewLoader implements SqlViewLoader {
    private final List<String> paths;

    public SimpleSqlViewLoader(List<String> paths) {
        this.paths = paths;
    }

    @Override
    public Set<Class<?>> loadSqlViewClasses() {
        return loadSqlViewClasses(paths);
    }

    @Override
    public Set<Class<?>> loadSqlViewClasses(List<String> directories) {
        Set<Class<?>> sqlView = new HashSet<>();
        directories.forEach( dir -> {
            sqlView.addAll(loadFromDirectory(dir));
        });
        return sqlView;
    }

    private Set<Class<?>> loadFromDirectory(String dir) {
        final var classesWithAnnotation = new HashSet<Class<?>>();
        final var packageName = toPackage(dir);

        URL packageUrl = Thread.currentThread()
                .getContextClassLoader()
                .getResource(packageName);
        if (packageUrl == null)
            return classesWithAnnotation;

        File directory = new File(URLDecoder.decode(packageUrl.getFile(), StandardCharsets.UTF_8));

        return findSqlFiles(classesWithAnnotation, directory, dir);
    }

    private Set<Class<?>> findSqlFiles(Set<Class<?>> classesWithAnnotation, File directory, String baseDirectory) {
        if (!directory.exists() || !directory.isDirectory())
            return classesWithAnnotation;

        var files = directory.listFiles();
        if(files == null)
            return classesWithAnnotation;

        for (File file : files) {
            if(file.isDirectory())
                findSqlFiles(classesWithAnnotation, file, baseDirectory);

            if (file.isFile() && file.getName().endsWith(".class")) {
                final var className = PathConverterUtils.getFullClassName(baseDirectory, file);

                try {
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isAnnotationPresent(SqlBind.class))
                        classesWithAnnotation.add(clazz);

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return classesWithAnnotation;
    }

    private String toPackage(String dir) {
        return dir.replace('.', '/');
    }

}
