package server;

import shared.packet.NiceLogFormatter;

import java.net.DatagramPacket;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

/**
 * <p>Undocumented :(</p>
 *
 * <p>Created by Alexander Winter on 2016-10-22.</p>
 */
public class ClockSyncServer extends Object
{
	public static void main(String[] args) throws Exception
	{
		new ClockSyncServer();
	}

	private Logger logger;
	private ServerConnection connection;

	public ClockSyncServer() throws Exception
	{
		this.logger = Logger.getLogger("Server");
		logger.setUseParentHandlers(false);

		ConsoleHandler consoleHandler = new ConsoleHandler()
		{
			{
				setOutputStream(System.out);
			}
		};

		consoleHandler.setFormatter(new NiceLogFormatter());

		logger.addHandler(consoleHandler);

		connection = new ServerConnection(this, 4560);
	}

	public Logger getLogger()
	{
		return logger;
	}
}
