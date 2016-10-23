package server;

import shared.packet.Packet;
import shared.packet.PacketOutWelcome;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;

/**
 * Represents a connection from the server side accepting new clients
 * Is an handler for a ServerSocket
 *
 * Created by winter on 25/03/16.
 */
public class ServerConnection
{
	private ClockSyncServer server;

	private DatagramSocket udpSocket;
	private byte[] inputBuffer;

	public ServerConnection(ClockSyncServer server, int port) throws Exception
	{
		this.server = server;

		inputBuffer = new byte[8 * 1024];
		if(port > 0)
			udpSocket = new DatagramSocket(port);
		else
			udpSocket = new DatagramSocket();

		new Thread(this::acceptInput).start();

		server.getLogger().info("The server is listening on " + udpSocket.getLocalPort());
	}

	private void acceptInput()
	{
		String packetName = null;

		while(isOpen()) try
		{
			packetName = null;
			DatagramPacket bufPacket = new DatagramPacket(inputBuffer, inputBuffer.length);

			udpSocket.receive(bufPacket);

			if(!isOpen())
				break;

			ByteArrayInputStream byteStream = new ByteArrayInputStream(inputBuffer);

			packetName = new DataInputStream(byteStream).readUTF();

			if(!packetName.equals("PacketInJoin"))
				continue;

			sendPacketToGuest(new PacketOutWelcome(System.currentTimeMillis()), bufPacket.getAddress(), bufPacket.getPort());


		}
		catch(SocketException ex)
		{

		}
		catch(Exception ex)
		{
			server.getLogger().log(Level.WARNING, "Unexpected exception", ex);
		}
	}

	public void sendPacketToGuest(Packet packet, InetAddress address, int port)
	{
		try
		{
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			new DataOutputStream(byteStream).writeUTF(packet.getClass().getSimpleName());
			packet.writeTo(new DataOutputStream(byteStream));

			DatagramPacket data = new DatagramPacket(byteStream.toByteArray(), byteStream.size(), address, port);

			new Thread(() -> {
				try
				{
					getUdpSocket().send(data);
				}
				catch(Exception ex)
				{
					server.getLogger().log(Level.WARNING, "An exception occurred while sending data to guest", ex);
				}
			}).start();
		}
		catch(Exception ex)
		{
			server.getLogger().log(Level.WARNING, "An exception occurred while packing data to guest", ex);
		}
	}


	public boolean isOpen()
	{
		return !udpSocket.isClosed();
	}

	public synchronized void close()
	{
		getUdpSocket().close();
		notify();
	}

	public DatagramSocket getUdpSocket()
	{
		return udpSocket;
	}
}
