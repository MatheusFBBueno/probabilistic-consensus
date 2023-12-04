package org.example;

import java.io.DataOutputStream;
import java.net.ConnectException;
import java.net.Socket;

import ProbabilisticConsensus.NodeSenderInterface;

public class SocketNode implements NodeSenderInterface {

	public void send(int message) {
		final String SERVER_IP = "127.0.0.1"; // Altere para o IP do servidor
		final int SERVER_PORT = 12345;

		try {
			// Cria um socket para se conectar ao servidor
			Socket socket = new Socket(SERVER_IP, SERVER_PORT);
//			System.out.println("Conectado ao servidor");

			// Cria streams de entrada e saída para o servidor
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

			// Envia um número inteiro para o servidor
			dos.writeInt(message);

			// Fecha o socket após o envio
			socket.close();
//			System.out.println("Conexão fechada");
		} catch (ConnectException e) {
			System.out.println("Cliente já obteve o número de respostas para chegar em um consenso");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
