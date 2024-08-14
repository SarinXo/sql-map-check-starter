package ilya.starter.sqlcheck.util;

import java.io.File;

public class PathConverterUtils {

    public static String getFullClassName(String mainDirectory, File classFile) {
        var fullPath = classFile.getPath();
        var className = classFile.getName();

        int fullPathLength = fullPath.length();
        int nameLength = className.length();
        int directoryLength = mainDirectory.length();

        int subdirectory = findLengthSubdirectory(fullPath, nameLength, mainDirectory);

        return fullPath
                .substring(fullPathLength - nameLength - 1 - subdirectory - directoryLength, fullPathLength - 6)
                .replace('\\', '.');
    }

    /**
     * Находит длину в символах от главной директории до имени файла
     *
     * @param fullPath      - полный путь C:/dir1/dir2/dir3/dir4/dir5/dir6/JavaClass.class
     * @param nameLength    - длина в символах имени файла JavaClass.class
     * @param mainDirectoty - относительный путь для создания классов dir4/dir5 и возможно, что дальше еще есть символы (их длину мы и ищем)
     * @return длинна участка от относительного пути до класса
     */
    private static int findLengthSubdirectory(String fullPath, int nameLength, String mainDirectoty) {
        int mainDirLength = mainDirectoty.length();
        int size = 0;
        for (int i = fullPath.length() - nameLength - 2; i > 0; i--) {
            int packageSize = 0;
            boolean toNewSycle = false;
            int mainStringIndex = mainDirLength - 1;

            while (fullPath.charAt(i) != '\\' && i != 0) {
                if (!toNewSycle && fullPath.charAt(i) == mainDirectoty.charAt(mainStringIndex)) {
                    mainStringIndex--;
                } else {
                    toNewSycle = true;
                }
                packageSize++;
                i--;
            }
            if (toNewSycle)
                size += packageSize + 1;
            else
                break;
        }
        return size;
    }
}
