package org.example;

/**
 * Альтернативная реализация интерфейса {@link SomeInterface}.
 * <p>
 * При вызове метода {@link #doSomething()} выводит символ "B".
 *
 * @author Developer
 * @version 1.0
 * @since 2025-04-05
 */
public class OtherImpl implements SomeInterface {
    /**
     * Выводит символ "B" в консоль.
     */
    public void doSomething() {
        System.out.print("B");
    }
}