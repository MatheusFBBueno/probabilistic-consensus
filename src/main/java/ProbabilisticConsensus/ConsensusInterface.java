package ProbabilisticConsensus;

public interface ConsensusInterface {

    // Method to broadcast a message to all nodes
    void broadcastMessage(ConsensusMessage message) throws Exception;
    
    void receiveMessage(ConsensusMessage message);
}
