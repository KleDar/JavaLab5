package org.example;

import org.junit.jupiter.api.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class InjectorTest {

    // Для перехвата вывода в консоль
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Тест 1: Проверка внедрения SomeImpl -> ожидаем "A"
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void testInject_SomeInterface_WithSomeImpl() {
        Properties props = new Properties();
        props.put("org.example.SomeInterface", "org.example.SomeImpl");
        props.put("org.example.SomeOtherInterface", "org.example.SODoer");

        SomeBean bean = new Injector(props).inject(new SomeBean());

        bean.foo();
        assertEquals("AC", outContent.toString());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Тест 2: Проверка внедрения OtherImpl -> ожидаем "B"
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void testInject_SomeInterface_WithOtherImpl() {
        Properties props = new Properties();
        props.put("org.example.SomeInterface", "org.example.OtherImpl");
        props.put("org.example.SomeOtherInterface", "org.example.SODoer");

        SomeBean bean = new Injector(props).inject(new SomeBean());

        bean.foo();

        assertEquals("BC", outContent.toString());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Тест 3: Проверка внедрения нескольких зависимостей -> ожидаем "AC"
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void testInject_MultipleDependencies() {
        Properties props = new Properties();
        props.put("org.example.SomeInterface", "org.example.SomeImpl");
        props.put("org.example.SomeOtherInterface", "org.example.SODoer");

        SomeBean bean = new Injector(props).inject(new SomeBean());

        bean.foo();

        assertEquals("AC", outContent.toString());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Тест 4: Если нет реализации — должно выбросить RuntimeException
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void testInject_NoImplementation_ShouldThrowException() {
        Properties props = new Properties(); // пустой properties

        SomeBean bean = new SomeBean();

        Exception exception = assertThrows(RuntimeException.class, () ->
                new Injector(props).inject(bean));

        assertTrue(exception.getMessage().contains("Нет реализации для интерфейса"));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Тест 5: Проверка, что инъекция работает только на полях с аннотацией @AutoInjectable
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void testInject_OtherFields_NotModified() {
        class TestBean {
            @AutoInjectable
            private SomeInterface autoField;

            private SomeInterface normalField;
        }

        Properties props = new Properties();
        props.put("org.example.SomeInterface", "org.example.SomeImpl");

        TestBean bean = new Injector(props).inject(new TestBean());

        assertNotNull(bean.autoField);   // должно быть внедрено
        assertNull(bean.normalField);     // должно остаться null
    }
}
