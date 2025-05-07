package org.example;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Аннотация, помечающая поля, в которые необходимо внедрить зависимости.
 * <p>
 * Используется вместе с классом {@link Injector} для автоматического
 * внедрения зависимостей через рефлексию.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoInjectable {}