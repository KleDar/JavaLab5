package org.example;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

public class Injector {

    private final Properties properties = new Properties();

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
                    field.setAccessible(true); // если поле private
                    field.set(target, instance);
                } catch (Exception e) {
                    throw new RuntimeException("Ошибка при внедрении " + interfaceType.getName(), e);
                }
            }
        }

        return target;
    }
}
