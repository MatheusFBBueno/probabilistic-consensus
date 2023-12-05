package org.example;

import ProbabilisticConsensus.ConsensusNode;

public class ReplicaNodesInit {

    public static void main(String[] args) {
        int numberOfThreads = 4;


        for (int i = 1; i < numberOfThreads+1; i++) {
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
        System.out.println("Iniciado nodo "+nodeId);
        JGroupsNode jGroupsNode = new JGroupsNode();
        SocketNode socketNode = new SocketNode();
        ConsensusNode consensusNode = new ConsensusNode(nodeId, jGroupsNode, socketNode);
        consensusNode.setNodeValue("2");
        jGroupsNode.start();
    }

}
