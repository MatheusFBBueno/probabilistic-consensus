package ProbabilisticConsensus;

import java.util.List;


public interface ConsensusInterface {


    // Method to broadcast a message to all nodes
    void broadcastMessage(ConsensusMessage message);


}
