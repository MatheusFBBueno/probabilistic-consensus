package ProbabilisticConsensus;

public class MessageParser {

        // Method to parse a message string into a PbftMessage object
        public static ConsensusMessage parseMessageString(String messageString) {
            String[] parts = messageString.split(",");

            if (parts.length < 4) {
                // Invalid message format
                throw new IllegalArgumentException("Invalid message format: " + messageString);
            }

            int view = Integer.parseInt(parts[0]);
            int sequenceNumber = Integer.parseInt(parts[1]);
            MessageType type = MessageType.valueOf(parts[2]);
            int senderId = Integer.parseInt(parts[3]);

            // Additional parsing for other fields based on the message type

            return new ConsensusMessage(view, sequenceNumber, type, senderId);
        }

        // Example usage
        public static void main(String[] args) {
            String messageString = "1,2,PREPARE,3"; // Example message string
            ConsensusMessage parsedMessage = MessageParser.parseMessageString(messageString);

            // Now you have a PbftMessage object
            System.out.println(parsedMessage.view);
            System.out.println(parsedMessage.sequenceNumber);
            System.out.println(parsedMessage.type);
            System.out.println(parsedMessage.senderId);

            // Additional fields can be printed or used as needed
        }

}
