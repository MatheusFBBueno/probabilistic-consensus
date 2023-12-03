package ProbabilisticConsensus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Representation of a node in the PBFT network
class ConsensusNode {
    int nodeId;
    int nodeView;
    int lastExecutedSequenceNumber;
    List<ConsensusMessage> log;
    Map<Integer, ConsensusMessage> prePrepareBuffer;
    Map<Integer, List<ConsensusMessage>> prepareBuffer;
    Map<Integer, List<ConsensusMessage>> commitBuffer;

    private final ConsensusInterface nodeInterface;

    ConsensusNode(int nodeId, ConsensusInterface nodeInterface) {
        this.nodeId = nodeId;
        this.nodeInterface = nodeInterface;
        this.nodeView = 0;   // Pode ter nome melhor, mas corresponde a o valor que o nodo enxerga como correto
        this.lastExecutedSequenceNumber = 0;
        this.log = new ArrayList<>();
        this.prePrepareBuffer = new HashMap<>();
        this.prepareBuffer = new HashMap<>();
        this.commitBuffer = new HashMap<>();
    }

    private void listenToMessages() {
        while (true) {
            this.log = this.nodeInterface.receiveAllMessages();
        }
    }

    public int getConsensus() { return 0;} // Chama todos os procedimentos e retorna valor

    private void startConsensusCall() {

        ConsensusMessage msg_request = new ConsensusMessage(this.nodeView,
                this.lastExecutedSequenceNumber++,
                MessageType.PREPREPARE,
                this.nodeId);
        this.nodeInterface.broadcastMessage(msg_request);
    }

    private void receivePrePrepare() {

    }

    private void respondPrepare() {
        ConsensusMessage msg_request = new ConsensusMessage(this.nodeView,
                this.lastExecutedSequenceNumber++,
                MessageType.PREPARE,
                this.nodeId);
        this.nodeInterface.broadcastMessage(msg_request);
    }

    private void respondCommit() {
        ConsensusMessage msg_request = new ConsensusMessage(this.nodeView,
                this.lastExecutedSequenceNumber++,
                MessageType.COMMIT,
                this.nodeId);
        this.nodeInterface.broadcastMessage(msg_request);
    }
}


public class ConsensusModule<T> {
    List<ConsensusNode> nodes;
    List<ConsensusInterface> interfaces;
    T[] replicaStates;
//
//    ConsensusModule(int numNodes) {
//        nodes = new ArrayList<>();
//        replicaStates = (T[]) new Object[numNodes];
//
//        for (int i = 0; i < numNodes; i++) {
//            new_interface = new ConsensusInterface();
//            nodes.add(new ConsensusNode(i, new_interface));
//        }
//    }
}
