package org.example;

import ProbabilisticConsensus.ConsensusNode;

public class Main {
	public static void main(String[] args) throws Exception {
		int numberOfThreads = 4;


		for (int i = 1; i < numberOfThreads; i++) {
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
		initMasterNode(0);
	}

	public static void initConsensus(int nodeId) throws Exception {
		JGroupsNode jGroupsNode = new JGroupsNode();
		ConsensusNode consensusNode = new ConsensusNode(nodeId, jGroupsNode);
		consensusNode.setNodeView(2);
		jGroupsNode.start();
//		consensusNode.getConsensus();
	}

	public static void initMasterNode(int nodeId) throws Exception{
		JGroupsNode jGroupsNode = new JGroupsNode();
		ConsensusNode consensusNode = new ConsensusNode(nodeId, jGroupsNode);
		jGroupsNode.start();
		System.out.println("CONSENSO ATINGIDO: "+consensusNode.getConsensus());
//		consensusNode.getConsensus();
	}
}
