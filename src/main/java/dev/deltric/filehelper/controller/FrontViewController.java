package dev.deltric.filehelper.controller;

import dev.deltric.filehelper.FileHelperApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class FrontViewController
{
    @FXML
    private Label selectedDirectory;

    @FXML
    private Button openToolbox;

    @FXML
    protected void onChooseDirectory(ActionEvent event)
    {
        // Reset current path
        FileHelperApplication.path = null;

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File directory = directoryChooser.showDialog(stage);

        // If the resulting directory is null, then disable next button.
        // Otherwise, the button is enabled and the label is updated with the path.
        if(directory == null)
        {
            selectedDirectory.setText("No Directory selected");
            openToolbox.setDisable(true);
        } else
        {
            selectedDirectory.setText(directory.getAbsolutePath());
            openToolbox.setDisable(false);
            FileHelperApplication.path = directory.getAbsolutePath();
        }
    }

    @FXML
    protected void onOpenToolbox(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setScene(FileHelperApplication.toolboxScene);
    }
}