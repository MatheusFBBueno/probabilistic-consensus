package ProbabilisticConsensus;

import java.util.*;
import java.util.stream.Collectors;

// Representation of a node in the PBFT network
class ConsensusNode {
    int nodeId;
    int nodeView;
    int lastExecutedSequenceNumber;
    int nodeQuantity;
    List<ConsensusMessage> log;
    Map<Integer, ConsensusMessage> prePrepareBuffer;
    Map<Integer, ConsensusMessage> prepareBuffer;
    Map<Integer, ConsensusMessage> commitBuffer;

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
        this.nodeQuantity = 4;

    }

    public void receiveMessage(ConsensusMessage msg) throws Exception {
        switch (msg.type) {
            case PRE_PREPARE -> {
                if (msg.sequenceNumber > this.lastExecutedSequenceNumber){
                    this.prepare();
                } else {
                    System.out.println("Recusado pedido de prePrepare");
                }
            }
            case PREPARE -> {
                this.updatePrepareBuffer(msg);
                this.commit();
            }
            case COMMIT -> {
                this.updateCommitBuffer(msg);
            }
        }
    }

    private void prepare() throws Exception {
        this.sendPrepare();
    }

    private void commit() throws Exception {
        this.sendCommit();
    }

    public int getConsensus() throws Exception { // Chama todos os procedimentos e retorna valor
        this.isMasterNode = true;
        this.startConsensusCall();
        int f = this.getBizantineTolerance();
        while (this.commitBuffer.size() < 2*f) {
            Thread.sleep(10);
        }

        return this.getAgreedValue();}

    private int getAgreedValue() {
        // Count occurrences of each view
        Map<Integer, Long> viewCounts = this.commitBuffer.values().stream()
                .collect(Collectors.groupingBy(ConsensusMessage::getView, Collectors.counting()));

        // Find the view with the maximum occurrence
        Optional<Map.Entry<Integer, Long>> maxEntry = viewCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        if (maxEntry.isPresent()) {
            return maxEntry.get().getKey();
        } else {
            throw new IllegalStateException("Unable to find the most common view");
        }

    }

    private int getBizantineTolerance() {
        return (int) Math.floor((float) (this.nodeQuantity-1)/3);
    }

    private void startConsensusCall() throws Exception {
        this.lastExecutedSequenceNumber++;
        ConsensusMessage msg_request = new ConsensusMessage(this.nodeView,
                this.lastExecutedSequenceNumber,
                MessageType.PRE_PREPARE,
                this.nodeId);
        this.nodeInterface.broadcastMessage(msg_request);
    }

    private void sendPrepare() throws Exception {
        ConsensusMessage msg_request = new ConsensusMessage(this.nodeView,
                this.lastExecutedSequenceNumber,
                MessageType.PREPARE,
                this.nodeId);
        this.nodeInterface.broadcastMessage(msg_request);
    }

    private void sendCommit() throws Exception {
        ConsensusMessage msg_request = new ConsensusMessage(this.nodeView,
                this.lastExecutedSequenceNumber,
                MessageType.COMMIT,
                this.nodeId);
        this.nodeInterface.broadcastMessage(msg_request);
    }

    private void updateCommitBuffer(ConsensusMessage msg) {
        this.commitBuffer.put(msg.senderId, msg);
    }

    private void updatePrepareBuffer(ConsensusMessage msg) {
        this.prepareBuffer.put(msg.senderId, msg);
    }

}

