package client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * <p>Undocumented :(</p>
 *
 * <p>Created by Alexander Winter on 2016-10-22.</p>
 */
public class ConnectScene extends Scene
{
	private ClockSyncClient client;

	private Text scenetitle;
	private Label addressLabel;
	private TextField addressTextField;
	private Label portLabel;
	private TextField portTextField;
	private Button connectButton;
	private Text connectingText;

	public ConnectScene(ClockSyncClient client)
	{
		super(new StackPane(), 300, 275);
		this.client = client;

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		scenetitle = new Text("ClockSync");
		scenetitle.setId("title");
		grid.add(scenetitle, 0, 0, 2, 1);

		addressLabel = new Label("IP Address:");
		grid.add(addressLabel, 0, 1);

		addressTextField = new TextField();
		grid.add(addressTextField, 1, 1);

		portLabel = new Label("Port:");
		grid.add(portLabel, 0, 2);

		portTextField = new TextField();
		grid.add(portTextField, 1, 2);

		connectingText = new Text();
		connectingText.setId("connecting-text");
		grid.add(connectingText, 1, 6);

		connectButton = new Button();
		connectButton.setText("Connect");
		connectButton.setOnAction(this::connect);

		HBox buttonContainer = new HBox();
		buttonContainer.setAlignment(Pos.CENTER_RIGHT);

		buttonContainer.getChildren().add(connectButton);
		grid.add(buttonContainer, 1, 4);

		setRoot(grid);
		getStylesheets().add(getClass().getResource("connect.css").toExternalForm());
	}

	private void connect(ActionEvent event)
	{
		//connectingText.setText("Connecting...");

		client.getStage().setScene(client.getClockScene());
	}
}