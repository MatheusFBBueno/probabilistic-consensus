package org.example;

import ProbabilisticConsensus.ConsensusNode;

public class MainMasterInit {

    public static void main(String[] args) throws Exception {

        initMasterNode(0);
    }
    public static void initMasterNode(int nodeId) throws Exception{
        JGroupsNode jGroupsNode = new JGroupsNode();
        SocketNode socketNode = new SocketNode();
        ConsensusNode consensusNode = new ConsensusNode(nodeId, jGroupsNode, socketNode);
        jGroupsNode.start();
        System.out.println("CONSENSO ATINGIDO: "+consensusNode.getConsensus());
//		consensusNode.getConsensus();
    }

}
