package ilya.starter.sqlcheck.verifyer;

import java.util.List;
import java.util.Set;

public interface SqlViewLoader {
    /**
     * Загружает все классы из различных пакетов
     * @param directories - список директорий для поиска классов
     * @return все классы аннотированные {@link ilya.starter.sqlcheck.annotation.SqlBind}
     */
    Set<Class<?>> loadSqlViewClasses(List<String> directories);

}
