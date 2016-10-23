package client;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * <p>Undocumented :(</p>
 *
 * <p>Created by Alexander Winter on 2016-10-22.</p>
 */
public class ClockSceneController
{
	@FXML
	private TextField hours, clienthours;

	@FXML
	private TextField minutes, clientminutes;

	@FXML
	private TextField seconds, clientseconds;

	@FXML
	private TextField millis, clientmillis;

	public ClockSceneController()
	{

	}

	public TextField getHours()
	{
		return hours;
	}

	public TextField getMinutes()
	{
		return minutes;
	}

	public TextField getSeconds()
	{
		return seconds;
	}

	public TextField getMillis()
	{
		return millis;
	}

	public TextField getClienthours()
	{
		return clienthours;
	}

	public TextField getClientminutes()
	{
		return clientminutes;
	}

	public TextField getClientseconds()
	{
		return clientseconds;
	}

	public TextField getClientmillis()
	{
		return clientmillis;
	}
}
