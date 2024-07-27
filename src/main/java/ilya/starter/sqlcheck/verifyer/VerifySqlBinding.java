package ilya.starter.sqlcheck.verifyer;

import ilya.starter.sqlcheck.exception.SqlBindingAppException;

import java.util.Set;

public interface VerifySqlBinding {
    /**
     * Проверяет 1 класс sqlView на соответствие полей. Может выбрасывать исключение в зависимости от стратегии
     * @param sqlView класс аннотированный {@link ilya.starter.sqlcheck.annotation.SqlBind}
     */
    void verify(Class<?> sqlView) throws SqlBindingAppException;

    /**
     * Проверяет все классы sqlView на соответствие полей. Может выбрасывать исключение в зависимости от стратегии
     * @param sqlViews классы аннотированный {@link ilya.starter.sqlcheck.annotation.SqlBind}
     */
    void verifyAll(Set<Class<?>> sqlViews) throws SqlBindingAppException;

    /**
     * Проверяет после проверки есть ли классы не удовлетворяющие условиям и если они оказываются, то выкидывает исключение
     * @throws SqlBindingAppException должно обязательно выкидываться, если были найдены ошибки до этого
     */
    void throwIfExistsProblem() throws SqlBindingAppException;
}
