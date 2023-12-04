package org.example;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import ProbabilisticConsensus.Client;

public class SocketClient {

	private final Client client;
	SocketClient(Client client) {
		this.client = client;
	}

	public void start() {
		final int PORT = 12345;

		try {
			// Cria um ServerSocket que irá aguardar conexões na porta especificada
			ServerSocket serverSocket = new ServerSocket(PORT);
			System.out.println("Servidor aguardando conexões na porta " + PORT);

			while (true) {
				// Aguarda uma conexão de um cliente
				Socket clientSocket = serverSocket.accept();
				System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

				// Cria uma thread para lidar com o cliente
				ClientHandler clientHandler = new ClientHandler(clientSocket, client);
				Thread thread = new Thread(clientHandler);
				thread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static class ClientHandler implements Runnable {
		private final Socket clientSocket;
		private final Client client;

		public ClientHandler(Socket clientSocket, Client client) {
			this.clientSocket = clientSocket;
			this.client = client;
		}

		@Override
		public void run() {
			try {
				// Cria streams de entrada e saída para o cliente
				DataInputStream dis = new DataInputStream(clientSocket.getInputStream());

				while (true) {
					// Aguarda e lê um número inteiro do cliente
					int number = dis.readInt();
					client.addResult(number);
					System.out.println("Número recebido do cliente " + clientSocket.getInetAddress().getHostAddress() + ": " + number);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
