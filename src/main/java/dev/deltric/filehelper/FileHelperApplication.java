package dev.deltric.filehelper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FileHelperApplication extends Application
{
    public static Scene frontViewScene;
    public static Scene toolboxScene;

    public static String path;

    @Override
    public void start(Stage stage) throws IOException
    {
        frontViewScene = loadScene("front-view", 320, 240);
        toolboxScene = loadScene("toolbox", 640, 670);

        stage.setTitle("File Helper");
        stage.setScene(frontViewScene);

        stage.show();
    }

    public Scene loadScene(String fileName, int width, int height) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(FileHelperApplication.class.getResource(fileName + ".fxml"));
        return new Scene(fxmlLoader.load(), width, height);
    }

    public static void main(String[] args)
    {
        launch();
    }
}