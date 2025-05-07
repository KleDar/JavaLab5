package org.example;

/**
 * Реализация интерфейса {@link SomeOtherInterface}.
 * <p>
 * При вызове метода {@link #doSomeOther()} выводит символ "C".
 *
 * @author Developer
 * @version 1.0
 * @since 2025-04-05
 */
public class SODoer implements SomeOtherInterface {
    /**
     * Выводит символ "C" в консоль.
     */
    public void doSomeOther() {
        System.out.print("C");
    }
}