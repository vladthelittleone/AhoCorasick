package com.vladthelittleone.algorithms;

/**
 * @author Skurishin Vladislav
 * @since 08.12.15
 */
public class TestHelper
{
    protected static final String EMPTY = "";
    protected static final String LINE_FEED = "\n";

    protected static final String X = "x";
    protected static final String XX = "xx";
    protected static final String XXX = "xxx";

    protected String x(int times)
    {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < times; i++)
        {
            builder.append("x");
        }

        return builder.toString();
    }
}
