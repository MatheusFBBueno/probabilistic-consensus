package ProbabilisticConsensus;

public interface MessageHandler {
	void handleReceive(ConsensusMessage message);
}
