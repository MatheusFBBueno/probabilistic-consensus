package ProbabilisticConsensus;

enum MessageType {
    PREPREPARE,
    PREPARE,
    COMMIT

}

class ConsensusMessage {
    int view;
    int sequenceNumber;
    MessageType type;
    int senderId;


    // Add other necessary fields based on the message type
    ConsensusMessage(int view, int sequenceNumber, MessageType type, int senderId) {
        this.view = view;
        this.sequenceNumber = sequenceNumber;
        this.type = type;
        this.senderId = senderId;
    }
}
