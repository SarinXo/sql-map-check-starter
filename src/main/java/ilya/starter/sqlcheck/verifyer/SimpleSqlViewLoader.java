package ilya.starter.sqlcheck.verifyer;

import ilya.starter.sqlcheck.annotation.SqlBind;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SimpleSqlViewLoader implements SqlViewLoader{
    private final List<String> paths;

    public SimpleSqlViewLoader( List<String> paths) {
        this.paths = paths;
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
                .getResource(dir);
        if (packageUrl == null)
            return classesWithAnnotation;

        File directory = new File(URLDecoder.decode(packageUrl.getFile(), java.nio.charset.StandardCharsets.UTF_8));
        if (!directory.exists())
            return classesWithAnnotation;

        for (File file : directory.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                final var className = getFullClassName(packageName, file);
                try {
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isAnnotationPresent(SqlBind.class)) {
                        classesWithAnnotation.add(clazz);
                    }
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

    private String getFullClassName(String packageName, File file) {
        return packageName + '.' + file.getName().replace(".class", "");
    }


}
