package org.example;

import ProbabilisticConsensus.ConsensusNode;

public class Main {
	public static void main(String[] args) throws Exception {
		JGroupsNode jGroupsNode = new JGroupsNode();
		ConsensusNode consensusNode = new ConsensusNode(1, jGroupsNode);
		jGroupsNode.start();
		consensusNode.getConsensus();
	}
}
