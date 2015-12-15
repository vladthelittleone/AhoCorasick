package com.vladthelittleone.algorithms.ahocorasick.web;

import com.vladthelittleone.algorithms.ahocorasick.AhoCorasick;
import com.vladthelittleone.algorithms.ahocorasick.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

/**
 * @author Skurishin Vladislav
 * @since 06.12.15
 */
public class ViewResultServlet extends HttpServlet
{
    private AhoCorasick ahoCorasick = new AhoCorasick();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.setContentType("text/html;charset=UTF-8");

        PrintWriter out = resp.getWriter();

        String patterns = req.getParameter("patterns");
        String searchString = req.getParameter("searchString");

        title(out, patterns, searchString);

        String[] array = patterns.split("\\s+");

        try
        {
            executeAlgorithm(out, searchString, array);
        }
        catch (Exception e)
        {
            out.print("<div>Ошибка</div>");
        }
        finally
        {
            out.close();
        }
    }

    private void title(PrintWriter out, String patterns, String searchString)
    {
        out.print("<div class='patterns'>Паттерны: " + patterns + "</div>");
        out.print("<div class='searchString'>Строка, в которой осуществляется поиск: " + searchString + "</div>");
    }

    private void executeAlgorithm(PrintWriter out, String searchString, String[] array)
    {
        if (array.length != 0)
        {
            for (String s : array)
            {
                ahoCorasick.insertString(s);
            }

            search(out, searchString);
        }
        else
        {
            out.print("<div>Некоректные строки</div>");
        }
    }

    private void search(PrintWriter out, String searchString)
    {
        if (!searchString.isEmpty())
        {
            out.print("<div>Результаты: </div>");

            Set<Pattern> set = ahoCorasick.findAllPatterns(searchString);

            if (set.isEmpty())
            {
                out.print("<div class='res'>Соответствия не обнаружены</div>");
            }
            else
            {
                printPatterns(out, set);
            }
        }
        else
        {
            out.print("<div class='res'>Некоректные паттерны</div>");
        }
    }

    private void printPatterns(PrintWriter out, Set<Pattern> set)
    {
        int i = 0;

        for (Pattern p : set)
        {
            i++;
            out.print("<div class='res" + i + "'>" + p + "</div>");
        }
    }
}
