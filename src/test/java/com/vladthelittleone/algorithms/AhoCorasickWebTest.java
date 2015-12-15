package com.vladthelittleone.algorithms;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.vladthelittleone.algorithms.ahocorasick.Pattern;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * @author Skurishin Vladislav
 * @since 08.12.15
 */
public class AhoCorasickWebTest
{
    private WebClient webClient = new WebClient(BrowserVersion.CHROME);

    private String HELLO_WORLD = "Hello world!";
    private String HI = "HI";
    private String EMPTY = "";

    protected String createXPath(final String tag, final String value)
    {
        return "//" + tag + "[contains(@class,'" + value + "')]";
    }

    @Test
    public void formCheck() throws IOException
    {
        HtmlPage page = webClient.getPage(request());
        HtmlElement element = page.getFirstByXPath(createXPath("div", "form"));

        final Map<String, String> map = new HashMap<>();

        map.put("//input[@type='submit']", "Submit Query");
        map.put("//input[@type='text']", "");

        Set<String> set = map.keySet();

        for (String str : set)
        {
            List<HtmlElement> list = (List<HtmlElement>) element.getByXPath(str);

            Assert.assertEquals(list.get(0).asText(), map.get(str));
        }
    }

    @Test
    public void submitWithEmptyField() throws IOException
    {
        HtmlPage page = webClient.getPage(request());

        HtmlElement element = page.getFirstByXPath(createXPath("div", "form"));
        HtmlElement button = element.getFirstByXPath("//input[@type='submit']");

        HtmlPage page1 = button.click();

        Assert.assertEquals(page, page1);
        Assert.assertEquals(page.asXml(), page1.asXml());
    }

    @Test
    public void submitWithPatternsEmptyField() throws IOException
    {
        HtmlPage page = webClient.getPage(request());

        HtmlElement element = page.getFirstByXPath(createXPath("div", "form"));
        HtmlElement button = element.getFirstByXPath("//input[@type='submit']");

        setSearchString(page, HELLO_WORLD);

        HtmlPage page1 = button.click();

        Assert.assertEquals(page, page1);
        Assert.assertEquals(page.asXml(), page1.asXml());
    }

    @Test
    public void submitWithSearchStringEmptyField() throws IOException
    {
        HtmlPage page = webClient.getPage(request());

        HtmlElement element = page.getFirstByXPath(createXPath("div", "form"));
        HtmlElement button = element.getFirstByXPath("//input[@type='submit']");

        setPatterns(page, HELLO_WORLD);

        HtmlPage page1 = button.click();

        Assert.assertEquals(page, page1);
        Assert.assertEquals(page.asXml(), page1.asXml());
    }

    @Test
    public void submit() throws IOException
    {
        HtmlPage page = webClient.getPage(request());
        HtmlElement element = page.getFirstByXPath(createXPath("div", "form"));

        setPatterns(page, HELLO_WORLD);
        setSearchString(page, HELLO_WORLD);

        HtmlElement button = element.getFirstByXPath("//input[@type='submit']");
        HtmlPage page1 = button.click();

        Assert.assertNotEquals(page, page1);
        Assert.assertNotEquals(page.asXml(), page1.asXml());
    }

    @Test
    public void patternRequest() throws IOException
    {
        HtmlPage page = getPage(HELLO_WORLD, HI);
        HtmlElement element = page.getFirstByXPath(createXPath("div", "patterns"));

        Assert.assertEquals("Паттерны: " + HELLO_WORLD, element.asText());
    }

    @Test
    public void searchStringRequest() throws IOException
    {
        HtmlPage page = getPage(HELLO_WORLD, HI);
        HtmlElement element = page.getFirstByXPath(createXPath("div", "searchString"));

        Assert.assertEquals("Строка, в которой осуществляется поиск: " + HI, element.asText());
    }

    @Test
    public void searchStringPatternRequest() throws IOException
    {
        HtmlPage page = getPage(HELLO_WORLD, HI);
        HtmlElement element1 = page.getFirstByXPath(createXPath("div", "searchString"));
        HtmlElement element2 = page.getFirstByXPath(createXPath("div", "patterns"));

        Assert.assertEquals("Строка, в которой осуществляется поиск: " + HI, element1.asText());
        Assert.assertEquals("Паттерны: " + HELLO_WORLD, element2.asText());
    }

    @Test
    public void failedSearch() throws IOException
    {
        HtmlPage page = getPage(HELLO_WORLD, HI);

        HtmlElement element = page.getFirstByXPath(createXPath("div", "res"));

        Assert.assertEquals("Соответствия не обнаружены", element.asText());
    }

    @Test
    public void successSearch() throws IOException
    {
        Pattern p = new Pattern(1, "asd");
        Pattern p1 = new Pattern(2, "sd");

        HtmlPage page = getPage(p.getPattern() + " " + p1.getPattern(), "asdad");

        HtmlElement element = page.getFirstByXPath(createXPath("div", "res1"));
        HtmlElement element1 = page.getFirstByXPath(createXPath("div", "res2"));

        Assert.assertEquals(p.toString(), element.asText());
        Assert.assertEquals(p1.toString(), element1.asText());
    }

    private WebRequest request() throws MalformedURLException
    {
        return new WebRequest(new URL("http://127.0.0.1:8082/"), HttpMethod.GET);
    }

    private HtmlPage getPage(String patterns, String searchString) throws IOException
    {
        List<NameValuePair> paramList = new ArrayList<>();

        // Формирование листа параметров для пост запроса
        paramList.add(new NameValuePair("patterns", patterns));
        paramList.add(new NameValuePair("searchString", searchString));

        WebRequest request = new WebRequest(new URL("http://127.0.0.1:8082/view"), HttpMethod.GET);
        request.setRequestParameters(paramList);

        return webClient.getPage(request);
    }

    private void setSearchString(HtmlPage page, String str)
    {
        HtmlInput inputString = page.getFirstByXPath("//input[@id='searchString']");
        inputString.setValueAttribute(str);
    }

    private void setPatterns(HtmlPage page, String str)
    {
        HtmlInput inputPattern = page.getFirstByXPath("//input[@id='patterns']");
        inputPattern.setValueAttribute(str);
    }
}

