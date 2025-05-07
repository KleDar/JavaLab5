package org.example;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Простой внедритель зависимостей (Dependency Injector).
 * <p>
 * Загружает конфигурацию из файла {@code application.properties},
 * анализирует объекты через рефлексию и внедряет зависимости,
 * помеченные аннотацией {@link AutoInjectable}.
 *
 * @author Developer
 * @version 1.0
 * @since 2025-04-05
 */
public class Injector {

    private final Properties properties = new Properties();

    /**
     * Конструктор по умолчанию.
     * <p>
     * Загружает свойства из файла {@code application.properties}.
     */
    public Injector() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("Файл application.properties не найден!");
            }
            properties.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки properties", e);
        }
    }

    /**
     * Внедряет зависимости в указанный объект.
     *
     * @param target Объект, в который нужно внедрить зависимости.
     * @param <T>    Тип целевого объекта.
     * @return Целевой объект с внедрёнными зависимостями.
     * @throws RuntimeException Если внедрение невозможно.
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
}