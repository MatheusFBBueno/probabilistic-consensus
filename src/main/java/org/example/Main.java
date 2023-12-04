package org.example;

import ProbabilisticConsensus.Client;

public class Main {
	public static void main(String[] args) throws Exception {

		Thread thread = new Thread(() -> {
			try {
				Client client = new Client(1);
				SocketClient socketClient = new SocketClient(client);
				socketClient.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		thread.start();
	}
}
