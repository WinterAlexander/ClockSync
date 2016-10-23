package client;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;

/**
 * <p>Undocumented :(</p>
 *
 * <p>Created by Alexander Winter on 2016-10-22.</p>
 */
public class ClockScene extends Scene
{
	private ClockSyncClient client;
	private ClockSceneController controller;

	private long serverOffset;

	/**
	 *
	 * @param client
	 * @throws IOException if clock_scene.fxml couldn't be loaded
	 */
	public ClockScene(ClockSyncClient client) throws IOException
	{
		super(new StackPane(), 300, 275);
		this.client = client;

		FXMLLoader loader = new FXMLLoader(ClockScene.class.getResource("clock.fxml"));
		setRoot(loader.load());

		getStylesheets().add(getClass().getResource("clock.css").toExternalForm());

		controller = loader.getController();

		Timeline updateTimer = new Timeline(new KeyFrame(Duration.millis(1), event -> setDisplayTime(System.currentTimeMillis() - serverOffset, System.currentTimeMillis())));

		updateTimer.setCycleCount(Timeline.INDEFINITE);
		updateTimer.play();
	}

	public void setDisplayTime(long serverrtime, long clienttime)
	{
		int millis = (int)(serverrtime % 1000);
		int seconds = (int)(serverrtime / 1000 % 60);
		int minutes = (int)(serverrtime / 1000 / 60 % 60);
		int hours = (int)(serverrtime / 1000 / 60 / 60 % 24);
		controller.getHours().setText(hours + "");
		controller.getMinutes().setText(minutes + "");
		controller.getSeconds().setText(seconds + "");
		controller.getMillis().setText(millis + "");

		int cmillis = (int)(clienttime % 1000);
		int cseconds = (int)(clienttime / 1000 % 60);
		int cminutes = (int)(clienttime / 1000 / 60 % 60);
		int chours = (int)(clienttime / 1000 / 60 / 60 % 24);
		controller.getClienthours().setText(chours + "");
		controller.getClientminutes().setText(cminutes + "");
		controller.getClientseconds().setText(cseconds + "");
		controller.getClientmillis().setText(cmillis + "");
	}

	public void setServerTime(long serverTime)
	{
		serverOffset = System.currentTimeMillis() - serverTime;
	}
}
