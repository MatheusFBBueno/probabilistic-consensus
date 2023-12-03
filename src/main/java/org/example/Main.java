package org.example;

import ProbabilisticConsensus.ConsensusNode;

public class Main {
	public static void main(String[] args) {
		int numberOfThreads = 5; // Substitua 5 pelo número desejado de threads

		for (int i = 0; i < numberOfThreads; i++) {
			int nodeId = i;
			Thread thread = new Thread(() -> {
				try {
					initConsensus(nodeId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			thread.start();
		}
	}

	public static void initConsensus(int nodeId) throws Exception {
		JGroupsNode jGroupsNode = new JGroupsNode();
		ConsensusNode consensusNode = new ConsensusNode(nodeId, jGroupsNode);
		jGroupsNode.start();
		consensusNode.getConsensus();
	}
}
