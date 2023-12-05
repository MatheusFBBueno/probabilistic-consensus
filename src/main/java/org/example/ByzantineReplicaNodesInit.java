package org.example;

import ProbabilisticConsensus.ConsensusNode;

public class ByzantineReplicaNodesInit {

    public static void main(String[] args) {
        int numberOfThreads = 3;


        for (int i = 1; i <= numberOfThreads; i++) {
            int nodeId = i+4;
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
        System.out.println("Iniciado nodo bizantino "+nodeId);
        JGroupsNode jGroupsNode = new JGroupsNode();
        SocketNode socketNode = new SocketNode();
        ConsensusNode consensusNode = new ConsensusNode(nodeId, jGroupsNode, socketNode);
        consensusNode.setNodeValue("20000");
        jGroupsNode.start();
    }
}
