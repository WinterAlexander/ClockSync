package client;

import com.sun.istack.internal.NotNull;
import shared.packet.Packet;
import shared.packet.PacketInJoin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a connection established by the game and server
 *
 *
 * Created by Alexander Winter on 2016-03-27.
 */
public class ClientConnection
{
	private PacketInterpreter interpreter;
	private Logger logger;

	private DatagramSocket udpSocket;
	private InetAddress address;
	private int port;

	private byte[] inputBuffer;
	private ArrayList<Packet> toSend;

	private volatile boolean welcomed;
	private volatile long sendStart;

	public ClientConnection(PacketInterpreter interpreter, Logger logger) throws SocketException
	{
		this(interpreter, logger, new DatagramSocket());
	}

	public ClientConnection(PacketInterpreter interpreter, Logger logger, @NotNull DatagramSocket socket)
	{
		this.interpreter = interpreter;
		this.logger = logger;
		udpSocket = socket;
		toSend = new ArrayList<>();
		welcomed = true;
		port = 1254;
		inputBuffer = new byte[8 * 1024];

	}

	/**
	 *
	 * @param packet
	 * @param address
	 * @param port
	 * @param timeout millis
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public synchronized void connectTo(PacketInJoin packet, InetAddress address, int port, int timeout) throws IOException, TimeoutException
	{
		if(address == null)
			throw new IllegalArgumentException("Address cannot be null !");

		if(!welcomed || isOpen())
			return;

		this.port = port;
		this.address = address;

		udpSocket.setSoTimeout(30_000);
		sendStart = System.nanoTime();
		sendPacket(packet);

		welcomed = false;
		new Thread(this::acceptInput).start();
		new Thread(this::sendOutput).start();

		try
		{
			synchronized(this)
			{
				wait(timeout);
			}
		}
		catch(InterruptedException ex)
		{
			logger.log(Level.INFO, "An exception occurred while waiting for server's response", ex);
		}

		if(!welcomed)
		{
			welcomed = true;
			close();
			throw new TimeoutException("No answer after " + timeout + " milliseconds.");
		}
	}

	public synchronized void sendPacketLater(Packet packet)
	{
		toSend.add(packet);
		notify();
	}

	public void sendPacket(Packet packet)
	{
		try
		{
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			DataOutputStream dataStream = new DataOutputStream(byteStream);

			dataStream.writeUTF(packet.getClass().getSimpleName());
			packet.writeTo(dataStream);

			DatagramPacket data = new DatagramPacket(byteStream.toByteArray(), byteStream.size(), address, port);


			udpSocket.send(data);
		}
		catch(Exception ex)
		{
			logger.log(Level.SEVERE, "An exception occurred when trying to send packet", ex);
		}
	}

	private void acceptInput()
	{
		while(!isDisposed()) try
		{
			DatagramPacket bufPacket = new DatagramPacket(inputBuffer, inputBuffer.length);

			udpSocket.receive(bufPacket);

			ByteArrayInputStream byteStream = new ByteArrayInputStream(inputBuffer);
			DataInputStream dataStream = new DataInputStream(byteStream);

			String packetName = dataStream.readUTF();

			logger.info("Received " + packetName);

			if(packetName.equals("KeepAlive"))
				continue;

			long delay = -1;

			if(packetName.equals("PacketOutWelcome"))
			{
				if(sendStart != -1)
				{
					delay = (System.nanoTime() - sendStart) / 1_000_000;
				}

				welcomed = true;
				synchronized(this)
				{
					notify();
				}
			}

			Packet packet = (Packet)Class.forName("shared.packet." + packetName).newInstance();
			packet.readFrom(dataStream);

			interpreter.receive(packet, delay, true);
		}
		catch(SocketTimeoutException ex)
		{
			logger.log(Level.SEVERE, "Disconnected from server", ex);
			close();}
		catch(Exception ex)
		{
			logger.log(Level.SEVERE, "An unexpected exception occurred while accepting input", ex);
			close();
		}
	}

	private void sendOutput()
	{
		while(!isDisposed())
		{
			while(toSend.size() != 0)
			{
				Packet packet = toSend.get(0);
				if(packet != null)
					sendPacket(packet);
				toSend.remove(0);
			}

			synchronized(this)
			{
				try
				{
					wait();
				}
				catch(InterruptedException ex)
				{
					logger.log(Level.SEVERE, "An unexpected exception occurred while waiting new output to send in game connection", ex);
				}
			}
		}
	}

	public boolean isOpen()
	{
		return udpSocket.isConnected();
	}

	public void close()
	{
		if(isOpen())
			udpSocket.disconnect();


		synchronized(this)
		{
			notify();
		}
	}

	public boolean isDisposed()
	{
		return udpSocket == null;
	}

	public void dispose()
	{
		if(isOpen())
			close();

		udpSocket = null;
	}

}
