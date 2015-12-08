package com.vladthelittleone.algorithms;

/**
 * @author Skurishin Vladislav
 * @since 08.12.15
 */
public class Preconditions
{
    public static void checkNotNull(Object obj, String message)
    {
        if (obj == null)
        {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkArgument(boolean expression, String message)
    {
        if (!expression)
        {
            throw new IllegalArgumentException(message);
        }
    }
}
