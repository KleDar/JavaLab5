package org.example;

/**
 * Стандартная реализация интерфейса {@link SomeInterface}.
 * <p>
 * При вызове метода {@link #doSomething()} выводит символ "A".
 *
 * @author Developer
 * @version 1.0
 * @since 2025-04-05
 */
public class SomeImpl implements SomeInterface {
    /**
     * Выводит символ "A" в консоль.
     */
    public void doSomething() {
        System.out.print("A");
    }
}