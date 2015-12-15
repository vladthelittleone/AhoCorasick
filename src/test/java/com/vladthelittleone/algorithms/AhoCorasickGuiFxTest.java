package com.vladthelittleone.algorithms;

import com.vladthelittleone.algorithms.ahocorasick.Pattern;
import com.vladthelittleone.algorithms.ahocorasick.gui.GuiFX;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.text.Text;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.loadui.testfx.Assertions.verifyThat;
import static org.loadui.testfx.controls.impl.VisibleNodesMatcher.visible;

/**
 * @author Skurishin Vladislav
 * @since 09.12.15
 */
public class AhoCorasickGuiFxTest extends GuiTest
{
    private static final String INSERT = "Insert:";
    private static final String RESULT = "Result:";
    private static final String HELLO_WORLD = "hello world";
    private static final String NEW_LINE = "\n";
    private static final String EMPTY = "";

    private static Parent root;

    @BeforeClass
    public static void beforeClass()
    {
        GuiFX.onLoad(r -> root = r);
    }

    @Before
    public void before() throws InterruptedException
    {
        Thread.sleep(2000);
        click("#clear");
    }

    @Test
    public void addPattern() throws Exception
    {
        add(HELLO_WORLD);

        verifyText("#output", builder()
                .append(INSERT)
                .append(NEW_LINE)
                .append(HELLO_WORLD)
                .toString());
    }

    @Test
    public void addEmptyPattern() throws Exception
    {
        add(EMPTY);

        verifyText("#output", "Can't insert empty string");
    }

    @Test
    public void addMultiplyPatterns() throws Exception
    {
        add("A");
        add("B");

        verifyText("#output", builder()
                .append(INSERT)
                .append(NEW_LINE)
                .append("A")
                .append(NEW_LINE)
                .append("AB")
                .toString());
    }

    @Test
    public void search() throws Exception
    {
        add(HELLO_WORLD);

        search(HELLO_WORLD);

        verifyText("#output", builder()
                .append(RESULT)
                .append(NEW_LINE)
                .append(new Pattern(1, HELLO_WORLD))
                .toString());

        disableCheck();
    }

    @Test
    public void searchWithoutMatch() throws Exception
    {
        add(HELLO_WORLD);

        search(INSERT);

        verifyText("#output", "Can't find match");

        disableCheck();
    }

    @Test
    public void searchWithEmptyPattern() throws Exception
    {
        search(HELLO_WORLD);

        verifyText("#output", "Can't find match");

        disableCheck();
    }

    @Test
    public void searchWithEmptyString() throws Exception
    {
        search(EMPTY);

        verifyText("#output", "Can't find match");

        disableCheck();
    }

    @Test
    public void searchMultiplyPatterns() throws Exception
    {
        add("A");
        add("B");

        search("AB");

        verifyText("#output", builder()
                .append(RESULT)
                .append(NEW_LINE)
                .append(new Pattern(1, "A"))
                .append(NEW_LINE)
                .append(new Pattern(1, "AB"))
                .toString());

        disableCheck();
    }

    @Test
    public void clear() throws Exception
    {
        add(HELLO_WORLD);

        click("#clear");

        verifyText("#output", EMPTY);

        search(HELLO_WORLD);

        verifyText("#output", "Can't find match");
    }


    @Test
    public void clearWithDisabled() throws Exception
    {
        add(HELLO_WORLD);

        search(HELLO_WORLD);

        click("#clear");

        verifyText("#output", EMPTY);

        search(HELLO_WORLD);

        verifyText("#output", "Can't find match");
    }

    private void verifyText(String node, String string)
    {
        waitUntil(node, is(visible()), 10);

        String text = ((Text) find(node)).getText();

        verifyThat(text, containsString(string));
    }

    private void search(String str)
    {
        click("#string").type(str);
        click("#search");
    }

    private void add(String t)
    {
        click("#pattern").type(t);
        click("#add");
    }

    private StringBuilder builder()
    {
        return new StringBuilder();
    }

    private void disableCheck()
    {
        assertTrue(find("#pattern").isDisable());
        assertTrue(find("#string").isDisable());
        assertTrue(find("#add").isDisable());
        assertTrue(find("#search").isDisable());
    }

    @Override
    public void setupStage() throws Throwable {
        new Thread(() -> Application.launch(GuiFX.class))
                .start();
    }

    @Override
    protected Parent getRootNode()
    {
        return root;
    }
}
