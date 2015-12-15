package com.vladthelittleone.algorithms.ahocorasick.gui;

import com.vladthelittleone.algorithms.ahocorasick.AhoCorasick;
import com.vladthelittleone.algorithms.ahocorasick.Pattern;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Skurishin Vladislav
 * @since 09.12.15
 */
public class GuiSwing extends JFrame
{
    protected JButton patternAddButton = new JButton("Add pattern to search");
    protected JButton stringAddButton = new JButton("Add string for match");
    protected JButton searchButton = new JButton("Search");
    protected JButton clearButton = new JButton("Clear");

    protected JTextField patternInput = new JTextField(10);
    protected JTextField stringInput = new JTextField(10);

    protected JTextArea patternOutput = new JTextArea();
    protected JTextArea resultOutput = new JTextArea();

    protected GridLayout grid = new GridLayout(0, 2);

    protected AhoCorasick algo = new AhoCorasick();
    protected Set<String> inserted = new HashSet<>();
    protected String matchString;

    public GuiSwing()
    {
        setTitle("Aho–Corasick algorithm");

        JPanel panel = new JPanel();
        panel.setLayout(grid);

        add(panel);

        addComponents(panel);
        listenersInit();

        buttonsDisable();

        setSize(600, 400);
    }

    private void addComponents(JPanel panel)
    {
        panel.add(patternInput);
        panel.add(patternAddButton);

        panel.add(stringInput);
        panel.add(stringAddButton);

        panel.add(searchButton);
        panel.add(clearButton);

        panel.add(patternOutput);
        panel.add(resultOutput);
    }

    private void listenersInit()
    {
        patternAddButton.addActionListener(s ->
                onAddButtonClick());

        stringAddButton.addActionListener(s ->
                onStringAddButtonClick());

        searchButton.addActionListener(s->
                onSearchButtonClick());

        clearButton.addActionListener(l ->
                onClearButtonClick());
    }

    private void onSearchButtonClick()
    {
        try
        {
            Set<Pattern> set = algo.findAllPatterns(matchString);
            resultOutput.setText(result(set));
            setEnabledPattern(false);
            setEnableMatcher(false);
        }
        catch (Exception e)
        {
            resultOutput.setText(e.getMessage());
        }
    }

    private String result(Set<Pattern> set)
    {
        StringBuilder builder = new StringBuilder("Result:\n");

        set.forEach(p ->
        {
            builder.append(p);
            builder.append("\n");
        });

        return builder.toString();
    }

    private void onStringAddButtonClick()
    {
        String string = stringInput.getText();

        if (!string.isEmpty())
        {
            matchString = string;

            clearButton.setEnabled(true);
            stringAddButton.setEnabled(true);
            searchButton.setEnabled(true);

            patternOutput.setText(insert("String matcher: " + string));
        }
        else
        {
            searchButton.setEnabled(false);
            stringAddButton.setEnabled(false);
            tryEnableClearButton();
        }
    }

    private void onClearButtonClick()
    {
        patternInput.setText("");
        stringInput.setText("");
        patternOutput.setText("");
        resultOutput.setText("");
        inserted.clear();
        algo = new AhoCorasick();
        enableAll();
    }

    private void onAddButtonClick()
    {
        String pattern = patternInput.getText();

        try
        {
            algo.insertString(pattern);
            patternOutput.setText(insert(pattern));
        }
        catch (Exception e)
        {
            patternOutput.setText(e.getMessage());
        }
    }

    private String insert(String pattern)
    {
        StringBuilder builder = new StringBuilder("Patterns:\n");

        inserted.add(pattern);

        inserted.forEach(i ->
        {
            builder.append(i);
            builder.append("\n");
        });

        return builder.toString();
    }

    private void setEnabledPattern(boolean b)
    {
        patternInput.setEnabled(b);
        patternAddButton.setEnabled(b);
    }

    private void tryEnableClearButton()
    {
        if (stringInput.getText().isEmpty() && patternInput.getText().isEmpty())
        {
            clearButton.setEnabled(false);
        }
    }

    private void buttonsDisable()
    {
        searchButton.setEnabled(false);
        clearButton.setEnabled(false);
    }

    private void enableAll()
    {
        stringInput.setEnabled(true);
        patternInput.setEnabled(true);
        stringAddButton.setEnabled(true);
        patternAddButton.setEnabled(true);
        searchButton.setEnabled(true);
    }

    private void setEnableMatcher(boolean b)
    {
        stringAddButton.setEnabled(b);
        stringInput.setEnabled(b);
    }

    public static void main(String[] args)
    {
        GuiSwing gui = new GuiSwing();
        gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gui.setVisible(true);
    }
}
