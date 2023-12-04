package org.example;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

import ProbabilisticConsensus.Client;

public class SocketClient {

	private final Client client;
	SocketClient(Client client) {
		this.client = client;
	}

	public void start() {
		final int PORT = 12345;

		try {
			ServerSocket serverSocket = new ServerSocket(PORT);
			System.out.println("Servidor aguardando conexões na porta " + PORT);
			AtomicInteger counter = new AtomicInteger(0);

			while (counter.get() < client.getNumberOfRequests()) {
				counter.incrementAndGet();
				Socket clientSocket = serverSocket.accept();

				ClientHandler clientHandler = new ClientHandler(clientSocket, client);
				Thread thread = new Thread(clientHandler);
				thread.start();
			}

			client.getConsensus();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			throw new RuntimeException(e);
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
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
