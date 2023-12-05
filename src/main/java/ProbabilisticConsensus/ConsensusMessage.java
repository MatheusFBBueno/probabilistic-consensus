package ProbabilisticConsensus;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class ConsensusMessage implements Serializable {

    String value;
    int sequenceNumber;
    MessageType type;
    int senderId;

    // Add other necessary fields based on the message type
    ConsensusMessage(String value, int sequenceNumber, MessageType type, int senderId) {
        this.value = value;
        this.sequenceNumber = sequenceNumber;
        this.type = type;
        this.senderId = senderId;
    }
}
