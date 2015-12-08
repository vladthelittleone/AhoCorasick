package com.vladthelittleone.algorithms.ahocorasick;

import com.vladthelittleone.algorithms.Preconditions;

/**
 * @author Skurishin Vladislav
 * @since 08.12.15
 */
public class Pattern
{
    private int position;
    private String pattern;

    public Pattern(int position, String pattern)
    {
        Preconditions.checkNotNull(pattern, "Pattern can't be null");
        Preconditions.checkArgument(position > 0, "Position must be more then zero");
        Preconditions.checkArgument(!pattern.isEmpty(), "Pattern can't be empty");

        this.position = position;
        this.pattern = pattern;
    }

    public int getPosition()
    {
        return position;
    }

    public String getPattern()
    {
        return pattern;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Pattern)) return false;

        Pattern pattern1 = (Pattern) o;

        if (position != pattern1.position) return false;
        if (pattern != null ? !pattern.equals(pattern1.pattern) : pattern1.pattern != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = position;
        result = 31 * result + (pattern != null ? pattern.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("Pattern{");
        sb.append("position=").append(position);
        sb.append(", pattern='").append(pattern).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
