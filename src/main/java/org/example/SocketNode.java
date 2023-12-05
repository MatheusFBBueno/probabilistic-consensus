package org.example;

import java.io.DataOutputStream;
import java.net.ConnectException;
import java.net.Socket;

import ProbabilisticConsensus.NodeSenderInterface;

public class SocketNode implements NodeSenderInterface {

	public void send(String message) {
		final String SERVER_IP = "127.0.0.1"; // Altere para o IP do servidor
		final int SERVER_PORT = 12345;

		try {
			Socket socket = new Socket(SERVER_IP, SERVER_PORT);

			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

			dos.writeUTF(message);

			socket.close();
		} catch (ConnectException e) {
			System.out.println("Cliente já obteve o número de respostas para chegar em um consenso");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
