package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class ClockSyncClient extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}

	private Stage stage;

	private ConnectScene connectScene;
	private ClockScene clockScene;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
	    this.stage = primaryStage;
        primaryStage.setTitle("ClockSync");

	    connectScene = new ConnectScene(this);
	    clockScene = new ClockScene(this);

	    primaryStage.setMinWidth(250);
	    primaryStage.setMinHeight(200);

        primaryStage.setScene(connectScene);
        primaryStage.show();
    }

	public Stage getStage()
	{
		return stage;
	}

	public ConnectScene getConnectScene()
	{
		return connectScene;
	}

	public ClockScene getClockScene()
	{
		return clockScene;
	}
}
