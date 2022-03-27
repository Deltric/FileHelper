package dev.deltric.filehelper.controller;

import dev.deltric.filehelper.FileHelperApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class ToolboxController
{
    @FXML
    private TextField fileNamePatternField;

    @FXML
    private TextField fileNameReplacementField;

    @FXML
    private Label fileNameReplaceLabel;

    @FXML
    private TextField fileContentsPatternField;

    @FXML
    private TextField fileContentsReplacementField;

    @FXML
    private TextField fileContentsExcludeField;

    @FXML
    private Label fileContentsReplaceLabel;

    @FXML
    protected void onReplaceFileNames() {
        String fileNamePattern = fileNamePatternField.getText();
        String fileNameReplacement = fileNameReplacementField.getText();

        // Make sure file name pattern field isn't empty
        if(fileNamePattern.isEmpty())
        {
            fileNameReplaceLabel.setText("File name pattern field is empty!");
            return;
        }

        // Make sure our path isn't null
        if(FileHelperApplication.path == null) {
            fileNameReplaceLabel.setText("Target path is null!");
            return;
        }

        File targetDir = new File(FileHelperApplication.path);

        // Ensures our directory does exist and is a directory
        if(!targetDir.exists() || !targetDir.isDirectory()) {
            fileNameReplaceLabel.setText("Target directory does not exist!!");
            return;
        }

        int filesMatching = 0;
        int filesRenamed = 0;

        // Loops through all files in the directory
        // Checking for file names that match the pattern
        // And renames a file if it matched
        try {
            for(File file : Objects.requireNonNull(targetDir.listFiles())) {
                if(file.getName().contains(fileNamePattern)) {
                    filesMatching++;
                    boolean renamed = file.renameTo(new File(targetDir, file.getName().replaceAll(fileNamePattern, fileNameReplacement)));
                    if(renamed) {
                        filesRenamed++;
                    }
                }
            }
        } catch (Exception exception) {
            fileNameReplaceLabel.setText("Exception: " + exception.getLocalizedMessage());
            return;
        }
        fileNameReplaceLabel.setText(String.format("Successfully replaced the names of %s/%s files!", filesRenamed, filesMatching));
    }

    @FXML
    protected void onReplaceFileContents() {
        String fileContentsPattern = fileContentsPatternField.getText();
        String fileContentsReplacement = fileContentsReplacementField.getText();
        String fileContentsExclude = fileContentsExcludeField.getText();

        // Make sure the file contents pattern isn't empty
        if(fileContentsPattern.isEmpty())
        {
            fileContentsReplaceLabel.setText("File contents pattern field are empty!");
            return;
        }

        // Make sure our path isn't null
        if(FileHelperApplication.path == null) {
            fileContentsReplaceLabel.setText("Target path is null!");
            return;
        }

        File targetDir = new File(FileHelperApplication.path);

        // Ensures our directory does exist and is a directory
        if(!targetDir.exists() || !targetDir.isDirectory()) {
            fileContentsReplaceLabel.setText("Target directory does not exist!!");
            return;
        }

        int filesMatching = 0;
        int filesModified = 0;

        // Loops through all files in the directory
        // Reading them line by line for any matching pattern
        // If matching it replaces with the pattern on the line to the replacement
        try {
            for(File file : Objects.requireNonNull(targetDir.listFiles())) {
                filesMatching++;

                List<String> currentLines = Files.readAllLines(file.toPath());
                ArrayList<String> updatedLines = new ArrayList<>();

                AtomicBoolean modified = new AtomicBoolean(false);

                currentLines.forEach(line ->
                {
                    if(fileContentsExclude.isEmpty() || !line.contains(fileContentsExclude))
                    {
                        updatedLines.add(line.replaceAll(fileContentsPattern, fileContentsReplacement));
                        modified.set(true);
                    } else
                    {
                        updatedLines.add(line);
                    }
                });

                if(modified.get()) {
                    filesModified++;
                }
                Files.write(file.toPath(), updatedLines, Charset.defaultCharset());
            }
        } catch (Exception exception) {
            fileContentsReplaceLabel.setText("Exception: " + exception.getLocalizedMessage());
            return;
        }
        fileContentsReplaceLabel.setText(String.format("Successfully replaced file contents of %s/%s files!", filesModified, filesMatching));
    }

    @FXML
    protected void onBack(ActionEvent event) {
        // Returns to select directory view
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setScene(FileHelperApplication.frontViewScene);
    }
}
