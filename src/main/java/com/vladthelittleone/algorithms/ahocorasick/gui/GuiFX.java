package com.vladthelittleone.algorithms.ahocorasick.gui;

import com.vladthelittleone.algorithms.Callback;
import com.vladthelittleone.algorithms.ahocorasick.AhoCorasick;
import com.vladthelittleone.algorithms.ahocorasick.Pattern;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Skurishin Vladislav
 * @since 09.12.15
 */
public class GuiFX extends Application
{
    private static Optional<Callback<Parent>> callback = Optional.empty();

    private AhoCorasick ahoCorasick = new AhoCorasick();
    private Set<String> inserted = new HashSet<>();

    private TextField patternInput;
    private TextField stringInput;
    private Text outputText;

    private Button addButton;
    private Button searchButton;

    @Override
    public void start(Stage primaryStage) throws IOException
    {
        initialize();

        primaryStage.setTitle("Aho–Corasick algorithm");

        GridPane grid = createGrid();

        callback.ifPresent(o -> o.call(grid));

        primaryStage.setScene(createScene(grid));
        primaryStage.show();
    }

    public static void onLoad(Callback<Parent> r)
    {
        callback = Optional.of(r);
    }

    private Scene createScene(GridPane grid)
    {
        createFirstRow(grid);
        createSecondRow(grid);
        createThirdRow(grid);
        createFourthRow(grid);
        createFifthRow(grid);

        return new Scene(grid, 600, 400);
    }

    private void initialize()
    {
        outputText = new Text();
        patternInput = new TextField();
        stringInput = new TextField();

        outputText.setId("output");
        patternInput.setId("pattern");
        stringInput.setId("string");
    }

    private void createFirstRow(GridPane grid)
    {
        Text sceneTitle = new Text("Add parameters to launch algorithm");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 4, 1);
    }

    private void createSecondRow(GridPane grid)
    {
        int row = 1;

        grid.add(new Label("Add patterns to search:"), 0, row);
        grid.add(patternInput, 1, row, 2, 1);

        addButton = createButton("Add", grid, 3, row);
        addButton.setId("add");
        addButton.setOnAction(e -> onAddAction());
    }

    private void createThirdRow(GridPane grid)
    {
        int row = 2;

        grid.add(new Label("In string:"), 0, row);
        grid.add(stringInput, 1, row, 3, 1);
    }

    private void createFourthRow(GridPane grid)
    {
        int row = 3;

        searchButton = createButton("Search", grid, 1, row);
        searchButton.setId("search");
        searchButton.setOnAction(e -> onSearchAction());

        Button b =createButton("Clear", grid, 2, row);
        b.setId("clear");
        b.setOnAction(e -> onClearAction());
    }

    private void createFifthRow(GridPane grid)
    {
        int row = 4;

        grid.add(outputText, 0, row, 4, 1);
    }

    private Button createButton(String title, GridPane grid, int col, int row)
    {
        Button btn = new Button(title);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER_LEFT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, col, row, 1, 1);

        return btn;
    }

    private GridPane createGrid()
    {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        return grid;
    }

    private void onAddAction()
    {
        try
        {
            String pattern = patternInput.getText();

            ahoCorasick.insertString(pattern);

            print("Insert:\n" + insert(pattern));
        }
        catch (Exception e)
        {
            print(e.getMessage());
        }
    }

    private void onSearchAction()
    {
        try
        {
            String string = stringInput.getText();
            StringBuilder builder = new StringBuilder();

            Set<Pattern> s = ahoCorasick.findAllPatterns(string);

            if (!s.isEmpty())
            {
                s.forEach(p -> {
                    builder.append(p);
                    builder.append("\n");
                });

                setDisable(true);

                print("Result:\n" + builder.toString());
            }
            else
            {
                setDisable(true);

                print("Can't find match");
            }
        }
        catch (Exception e)
        {
            print(e.getMessage());
        }
    }

    private void onClearAction()
    {
        ahoCorasick = new AhoCorasick();

        patternInput.clear();
        stringInput.clear();
        inserted.clear();

        setDisable(false);

        outputText.setText("");
    }

    private String insert(String pattern)
    {
        StringBuilder builder = new StringBuilder();

        inserted.add(pattern);
        inserted.forEach(i ->
        {
            builder.append(i);
            builder.append("\n");
        });

        return builder.toString();
    }

    private void print(String pattern)
    {
        outputText.setFill(Color.FIREBRICK);
        outputText.setText(pattern);
    }

    private void setDisable(boolean b)
    {
        patternInput.setDisable(b);
        stringInput.setDisable(b);
        addButton.setDisable(b);
        searchButton.setDisable(b);
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
