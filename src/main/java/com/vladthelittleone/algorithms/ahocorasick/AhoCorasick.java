package com.vladthelittleone.algorithms.ahocorasick;

import com.vladthelittleone.algorithms.Preconditions;

import java.util.*;

/**
 * @author Skurishin Vladislav
 * @since 06.12.15
 */
public class AhoCorasick
{
    private Node root = new Node();

    public void insertString(String s)
    {
        Preconditions.checkNotNull(s, "Can't insert null string");
        Preconditions.checkArgument(!s.isEmpty(), "Can't insert empty string");

        Node v = root;

        for (char ch : s.toCharArray())
        {
            Node next = v.children.get(ch);

            if (next == null)
            {
                next = new Node(v, ch);
                v.children.put(ch, next);
            }

            v = next;
        }

        v.leaf = true;
        v.pattern = s;
    }

    public boolean contains(String s)
    {
        if (s != null)
        {
            Node v = root;

            for (char ch : s.toCharArray())
            {
                Node next = v.children.get(ch);

                if (next == null)
                {
                    return false;
                }

                v = next;
            }

            /**
             * Проверка на то, что
             * последний символ корень.
             */
            if (v.leaf)
            {
                return !s.isEmpty();
            }
        }

        return false;
    }

    public Set<Pattern> findAllPatterns(String string)
    {
        Preconditions.checkNotNull(string, "String can't be null");
        Set<Pattern> patterns = new HashSet<>();

        Node u = root;
        int i = 1;

        for (char c : string.toCharArray())
        {
            u = getGo(u, c);
            patterns.addAll(check(u, i++));
        }

        return patterns;
    };

    private Node getSuffix(Node n)
    {
        if (n.suffix == null) //если еще не считали
        {
            boolean eq = Objects.equals(n, root) || Objects.equals(n.parent, root);

            if (eq) //если v - корень или предок v - корень
            {
                n.suffix = root;
            }
            else
            {
                n.suffix = getGo(getSuffix(n.parent), n.symbol);
            }
        }

        return n.suffix;
    }

    private Node getCompressedSuffix(Node n)
    {
        if (n.compressedSuffix == null)
        {
            Node suffix = getSuffix(n);

            if (Objects.equals(suffix, root)) //либо v - корень, либо суф. ссылка v указывает на корень
            {
                n.compressedSuffix = root;
            }
            else
            {
                n.compressedSuffix = (n.leaf) ? suffix : getCompressedSuffix(suffix);
            }
        }

        return n.compressedSuffix;
    }

    private Set<Pattern> check(Node n, int i)
    {
        Set<Pattern> res = new HashSet<>();

        Node u = n;

        while (!Objects.equals(u, root))
        {
            if (u.leaf)
            {
                int pos = i - u.pattern.length() + 1;
                String pattern = u.pattern;

                res.add(new Pattern(pos, pattern));
            }

            u = getCompressedSuffix(u);
        }

        return res;
    }

    private Node getGo(Node n, char ch)
    {
        if (n.go.get(ch) == null)
        {
            Node child = n.children.get(ch);

            if (child != null)
            {
                n.go.put(ch, child);
            }
            else
            {
                if (Objects.equals(n, root))
                {
                    n.go.put(ch, root);
                }
                else
                {
                    n.go.put(ch, getGo(getSuffix(n), ch));
                }
            }
        }

        return n.go.get(ch);
    }

    private class Node
    {
        Map<Character, Node> children = new HashMap<>();
        Map<Character, Node> go = new HashMap<>();

        Node parent;
        Node suffix;
        Node compressedSuffix;

        String pattern;
        char symbol;
        boolean leaf;

        Node()
        {
        }

        Node(Node parent, char symbol)
        {
            this.parent = parent;
            this.symbol = symbol;
        }
    }
}
