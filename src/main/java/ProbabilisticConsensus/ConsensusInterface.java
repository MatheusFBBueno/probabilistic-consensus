package ProbabilisticConsensus;

import java.util.List;


public interface ConsensusInterface {

    // Method to send a message to a specific node
    void sendMessage(int nodeId, ConsensusMessage message);

    // Method to broadcast a message to all nodes
    void broadcastMessage(ConsensusMessage message);

    // Method to receive messages for a specific node
    List<ConsensusMessage> receiveMessages(int nodeId);

    // Method to receive messages for all nodes
    List<ConsensusMessage> receiveAllMessages();

}
