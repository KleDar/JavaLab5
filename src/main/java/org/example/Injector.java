package org.example;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Простой внедритель зависимостей (Dependency Injector).
 * <p>
 * Класс {@code Injector} анализирует поля объектов через рефлексию,
 * находит помеченные аннотацией {@link AutoInjectable}, и внедряет в них
 * зависимости, основываясь на конфигурации из файла {@code application.properties}.
 * </p>
 *
 * <p><b>Пример использования:</b></p>
 * <pre>
 * SomeBean bean = new Injector().inject(new SomeBean());
 * bean.foo(); // вызывает внедрённые зависимости
 * </pre>
 *
 */
public class Injector {

    private final Properties properties;

    /**
     * Конструктор по умолчанию.
     * <p>
     * Загружает свойства из файла {@code application.properties},
     * который должен находиться в ресурсах проекта.
     * </p>
     *
     * @throws RuntimeException если файл не найден или произошла ошибка при загрузке
     */
    public Injector() {
        this(loadDefaultProperties());
    }

    /**
     * Конструктор, принимающий пользовательские настройки.
     * <p>
     * Используется в тестах или для динамической настройки зависимостей.
     * Позволяет указать свои {@link Properties}, вместо чтения из файла.
     * </p>
     *
     * @param properties настройки, где указаны соответствия "интерфейс=реализация"
     */
    public Injector(Properties properties) {
        this.properties = properties;
    }

    /**
     * Внедряет зависимости в указанный объект.
     * <p>
     * Метод проходит по всем полям объекта, ищет аннотацию {@link AutoInjectable},
     * определяет нужную реализацию из {@link #properties} и создаёт её через рефлексию.
     * </p>
     *
     * @param target объект, в который нужно внедрить зависимости
     * @param <T> тип целевого объекта
     * @return объект с внедрёнными зависимостями
     * @throws RuntimeException если внедрение невозможно (например, реализация не найдена)
     */
    public <T> T inject(T target) {
        Class<?> clazz = target.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(AutoInjectable.class)) {
                Class<?> interfaceType = field.getType();
                String implClassName = properties.getProperty(interfaceType.getName());

                if (implClassName == null) {
                    throw new RuntimeException("Нет реализации для интерфейса: " + interfaceType.getName());
                }

                try {
                    Class<?> implClass = Class.forName(implClassName);
                    Object instance = implClass.getDeclaredConstructor().newInstance();
                    field.setAccessible(true);
                    field.set(target, instance);
                } catch (Exception e) {
                    throw new RuntimeException("Ошибка при внедрении " + interfaceType.getName(), e);
                }
            }
        }

        return target;
    }

    /**
     * Загружает свойства из файла {@code application.properties}.
     * <p>
     * Используется в конструкторе по умолчанию.
     * </p>
     *
     * @return загруженные свойства
     * @throws RuntimeException если файл не найден или произошла ошибка при чтении
     */
    private static Properties loadDefaultProperties() {
        Properties props = new Properties();
        try (InputStream input = Injector.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("Файл application.properties не найден!");
            }
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки properties", e);
        }
        return props;
    }
}