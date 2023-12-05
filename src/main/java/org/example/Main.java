package org.example;

import ProbabilisticConsensus.Client;
import ProbabilisticConsensus.ConsensusNode;

public class Main {
	public static void main(String[] args) throws Exception {
		startClient();
		startReplica();
//		startByzantine();
		Thread.sleep(2000);
		initMasterNode(0);
	}

	public static void startClient() {

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

	public static void initMasterNode(int nodeId) throws Exception{
		JGroupsNode jGroupsNode = new JGroupsNode();
		SocketNode socketNode = new SocketNode();
		ConsensusNode consensusNode = new ConsensusNode(nodeId, jGroupsNode, socketNode);
		jGroupsNode.start();
		//        System.out.println("CONSENSO ATINGIDO: "+consensusNode.getConsensus());
		consensusNode.getConsensus();
	}

	public static void startReplica() {
		int numberOfThreads = 4;


		for (int i = 1; i < numberOfThreads+1; i++) {
			int nodeId = i;
			Thread thread = new Thread(() -> {
				try {
					initConsensusReplica(nodeId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			thread.start();
		}
	}

	public static void initConsensusReplica(int nodeId) throws Exception {
		System.out.println("Iniciado nodo "+nodeId);
		JGroupsNode jGroupsNode = new JGroupsNode();
		SocketNode socketNode = new SocketNode();
		ConsensusNode consensusNode = new ConsensusNode(nodeId, jGroupsNode, socketNode);
		consensusNode.setNodeValue("2");
		jGroupsNode.start();
	}

	public static void startByzantine() {
		int numberOfThreads = 2;


		for (int i = 1; i <= numberOfThreads; i++) {
			int nodeId = i+4;
			Thread thread = new Thread(() -> {
				try {
					initConsensusByzantine(nodeId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			thread.start();
		}
	}

	public static void initConsensusByzantine(int nodeId) throws Exception {
		System.out.println("Iniciado nodo bizantino "+nodeId);
		JGroupsNode jGroupsNode = new JGroupsNode();
		SocketNode socketNode = new SocketNode();
		ConsensusNode consensusNode = new ConsensusNode(nodeId, jGroupsNode, socketNode);
		consensusNode.setNodeValue("20000");
		jGroupsNode.start();
	}
}
