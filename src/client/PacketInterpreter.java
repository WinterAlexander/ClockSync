package client;

import javafx.application.Platform;
import shared.packet.Packet;
import shared.packet.PacketOutWelcome;

import java.util.logging.Level;

/**
 * <p>Undocumented :(</p>
 *
 * <p>Created by Alexander Winter on 2016-10-21.</p>
 */
public class PacketInterpreter extends Object
{
	private ClockSyncClient client;

	public PacketInterpreter(ClockSyncClient game)
	{
		this.client = client;
	}

	/**
	 * Receives the packet. Packet should always be received from main thread,
	 * if this method is called from another thread set async to true.
	 * In that case the method will automatically find the task to the main thread
	 * @param packet packet to receive
	 * @param async true if called from another thread
	 */
	public void receive(Packet packet, long delay, boolean async)
	{
		if(async)
		{
			Platform.runLater(() -> receive(packet, delay, false));
			return;
		}

		if(packet instanceof PacketOutWelcome)
		{
			client.getClockScene().setServerTime(((PacketOutWelcome)packet).getCurrentTimeMillis() + delay / 2);
			return;
		}


		client.getLogger().log(Level.INFO, "The packet sent by server isn't appropriate: " + packet.getClass().getName());
	}
}
