package shared.packet;

import java.io.*;

/**
 * Sends the server time to a client for it to sync
 * Created by Alexander Winter on 2016-03-27.
 */
public class PacketOutWelcome extends Packet
{
	private long currentTimeMillis;

	public PacketOutWelcome()
	{
		currentTimeMillis = 0;
	}

	public PacketOutWelcome(long currentTimeMillis)
	{
		this.currentTimeMillis = currentTimeMillis;
	}

	@Override
	public void readFrom(DataInputStream stream) throws IOException
	{
		currentTimeMillis = stream.readLong();
	}

	@Override
	public void writeTo(DataOutputStream stream) throws IOException
	{
		stream.writeLong(currentTimeMillis);
	}

	public long getCurrentTimeMillis()
	{
		return currentTimeMillis;
	}

	public void setCurrentTimeMillis(long currentTimeMillis)
	{
		this.currentTimeMillis = currentTimeMillis;
	}
}
