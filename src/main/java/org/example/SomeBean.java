package org.example;

/**
 * Пример класса, использующего внедряемые зависимости.
 * <p>
 * Содержит два поля с аннотацией {@link AutoInjectable}, которые будут
 * заполнены через механизм DI, реализованный в {@link Injector}.
 *
 */
public class SomeBean {
    @AutoInjectable
    private SomeInterface field1;

    @AutoInjectable
    private SomeOtherInterface field2;

    /**
     * Вызывает методы из внедрённых зависимостей.
     * <p>
     * Ожидается вывод "AC" или "BC", в зависимости от настроек в
     * файле {@code application.properties}.
     */
    public void foo() {
        field1.doSomething();
        field2.doSomeOther();
    }
}