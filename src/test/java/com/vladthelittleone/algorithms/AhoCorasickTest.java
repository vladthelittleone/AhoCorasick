package com.vladthelittleone.algorithms;

import com.vladthelittleone.algorithms.ahocorasick.AhoCorasick;
import com.vladthelittleone.algorithms.ahocorasick.Pattern;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Skurishin Vladislav
 * @since 08.12.15
 */
public class AhoCorasickTest extends TestHelper
{
    private AhoCorasick algo;

    @Before
    public void before()
    {
        // Инициализируем алгоритм перед каждым тестом
        algo = new AhoCorasick();
    }

    @Test
    public void insertString()
    {
        String str = "helloWorld";

        // Проверка на включение паттернов
        algo.insertString(str);

        assertTrue(algo.contains(str));
    }

    @Test
    public void insertSpecString()
    {
        String str = "!@#";

        // Проверка на включение паттернов
        algo.insertString(str);

        assertTrue(algo.contains(str));
    }

    @Test
    public void insertNumberString()
    {
        String str = "12345";

        // Проверка на включение паттернов
        algo.insertString(str);

        assertTrue(algo.contains(str));
    }

    /**
     * Проерка на корректность при null
     */
    @Test(expected = IllegalArgumentException.class)
    public void insertNullString()
    {
        algo.insertString(null);
    }

    /**
     * Проерка на корректность при включении пустой строки
     */
    @Test(expected = IllegalArgumentException.class)
    public void insertEmptyString()
    {
        algo.insertString(EMPTY);
    }

    @Test
    public void contains()
    {
        String hello = "hello";
        String ollo = "ollo";

        algo.insertString(hello);
        algo.insertString(ollo);

        // Проверка на содержание паттернов
        assertTrue(algo.contains(hello));
        assertTrue(algo.contains(ollo));
    }

    @Test
    public void containsSpec()
    {
        String spec = "!@#$%^&*()-=+_";

        algo.insertString(spec);

        // Проверка на содержание паттернов
        assertTrue(algo.contains(spec));
    }

    @Test
    public void containsNum()
    {
        String num = "123";

        algo.insertString(num);

        // Проверка на содержание паттернов
        assertTrue(algo.contains(num));
    }

    @Test
    public void containsNullString()
    {
        // Проверка на содержание пустой строки
        assertFalse(algo.contains(null));
    }

    @Test
    public void containsEmptyString()
    {
        // Проверка на содержание пустой строки
        assertFalse(algo.contains(EMPTY));
    }

    @Test
    public void containsNotInsertedString()
    {
        String hello = "hello";
        String ollo = "ollo";
        String world = "world";

        algo.insertString(hello);
        algo.insertString(ollo);

        // Проверка на содержание пустой строки
        assertFalse(algo.contains(world));
    }

    @Test
    public void containsUpperSubstring()
    {
        String hello = "hello";
        String ollo = "ollo";
        String oll = "oll";

        algo.insertString(hello);
        algo.insertString(ollo);

        // Проверка на содержание пустой строки
        assertFalse(algo.contains(oll));
    }

    @Test
    public void containsDownerSubstring()
    {
        String hello = "hello";
        String ollo = "ollo";
        String llo = "llo";

        algo.insertString(hello);
        algo.insertString(ollo);

        // Проверка на содержание пустой строки
        assertFalse(algo.contains(llo));
    }

    @Test
    public void findAllPatterns()
    {
        Set<Pattern> t = new HashSet<>();
        t.add(new Pattern(1, "hello"));
        t.add(new Pattern(5, "ollo"));
        t.add(new Pattern(9, "world"));

        findAllAssertTrue(t, "hellolloworld");
    }

    /**
     * Проверка множественных вхождений
     * X = "x"
     * XX = "xx"
     * XXX = "xxx"
     */
    @Test
    public void findMultiplyPatterns()
    {
        Set<Pattern> t = new HashSet<>();
        t.add(new Pattern(1, X));
        t.add(new Pattern(2, X));
        t.add(new Pattern(3, X));
        t.add(new Pattern(1, XX));
        t.add(new Pattern(2, XX));

        findAllAssertTrue(t, XXX);
    }

    /**
     * Поиск строк с одинаковым началом (см. Тесты реализации https://hkn.eecs.berkeley.edu/~DYOO/java/index.html)
     */
    @Test
    public void findWithAdjacent()
    {
        Set<Pattern> t = new HashSet<>();
        t.add(new Pattern(1, "jane"));
        t.add(new Pattern(5, "jin"));
        t.add(new Pattern(4, "ejin"));

        findAllAssertTrue(t, "janejin");
    }

    /**
     * Поиск строк с одинаковым началом (см. Тесты реализации https://hkn.eecs.berkeley.edu/~DYOO/java/index.html)
     */
    @Test
    public void findSingle()
    {
        assertFind(5, "jin", "janejin");
    }

    /**
     * Проверка работы с переносами
     */
    @Test
    public void findWithLineFeed()
    {
        assertFind(6, "jin", "jane" + LINE_FEED + "jin");
    }

    /**
     * Проверка работы с русским языком
     */
    @Test
    public void findRussianLetters()
    {
        assertFind(5, "йцык", "asdfйцык");
    }

    /**
     * Проверка работы с числами
     */
    @Test
    public void findNumbers()
    {
        assertFind(9, "43321", "asdfйцык43321");
    }

    /**
     * Проверка работы с спец. знаками
     */
    @Test
    public void findSpecLetters()
    {
        Pattern num = new Pattern(9, "!@#$%^&*()_+-=");

        algo.insertString(num.getPattern());

        assertTrue(algo.findAllPatterns("asdfйцык!@#$%^&*()_+-=").contains(num));
    }

    @Test
    public void findOnEmpty()
    {
        String hello = "hello";

        algo.insertString(hello);

        assertTrue(algo.findAllPatterns("").isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void findOnNull()
    {
        algo.findAllPatterns(null);
    }

    private void assertFind(int pos, String pattern, String findPattern)
    {
        Pattern p = new Pattern(pos, pattern);

        algo.insertString(p.getPattern());

        assertTrue(algo.findAllPatterns(findPattern).contains(p));
    }

    private void findAllAssertTrue(Set<Pattern> t, String findPattern)
    {
        /**
         * Добавляем все строки из t множества
         */
        t.forEach(p -> algo.insertString(p.getPattern()));
        algo.findAllPatterns(findPattern).forEach(p -> assertTrue(t.contains(p)));
    }
}
