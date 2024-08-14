package ilya.starter.sqlcheck.verifyer;

import ilya.starter.sqlcheck.userapi.SqlBind;

import java.util.List;
import java.util.Set;

public interface SqlViewLoader {
    /**
     * Загружает все классы из различных пакетов
     * @param directories - список директорий для поиска классов
     * @return все классы аннотированные {@link SqlBind}
     */
    Set<Class<?>> loadSqlViewClasses();
    Set<Class<?>> loadSqlViewClasses(List<String> directories);

}
