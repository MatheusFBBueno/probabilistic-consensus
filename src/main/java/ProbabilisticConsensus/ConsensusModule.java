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

    boolean isMasterNode;

    ConsensusNode(int nodeId, ConsensusInterface nodeInterface) {
        this.nodeId = nodeId;
        this.nodeInterface = nodeInterface;
        this.nodeView = 0;   // Pode ter nome melhor, mas corresponde a o valor que o nodo enxerga como correto
        this.lastExecutedSequenceNumber = 0;
        this.log = new ArrayList<>();
        this.prePrepareBuffer = new HashMap<>();
        this.prepareBuffer = new HashMap<>();
        this.commitBuffer = new HashMap<>();
        this.isMasterNode = false;

    }

    private void receive() {
        while (true) {
            this.log = this.nodeInterface.receiveAllMessages();
        }
    }

    public int getConsensus() { // Chama todos os procedimentos e retorna valor
        this.isMasterNode = true;
        this.startConsensusCall();
        int byzantineTolerance =  this.nodeInterface.numberOfNodes();
        return 0;}

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
    ConsensusNode node;
    ConsensusInterface interface;

    public void receive(String msgString) {
       ConsensusMessage<T> msgObject = MessageParser.parseMessageString(msgString);
       node.receiveMessage(msgObject);
    }
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
