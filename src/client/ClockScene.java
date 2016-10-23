package client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

/**
 * <p>Undocumented :(</p>
 *
 * <p>Created by Alexander Winter on 2016-10-22.</p>
 */
public class ClockScene extends Scene
{
	private ClockSyncClient client;

	/**
	 *
	 * @param client
	 * @throws IOException if clock_scene.fxml couldn't be loaded
	 */
	public ClockScene(ClockSyncClient client) throws IOException
	{
		super(FXMLLoader.load(ClockScene.class.getResource("clock.fxml")));
		this.client = client;

		getStylesheets().add(getClass().getResource("clock.css").toExternalForm());
	}
}
