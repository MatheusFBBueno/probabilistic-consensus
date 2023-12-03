package ProbabilisticConsensus;

import static ProbabilisticConsensus.MessageType.COMMIT;
import static ProbabilisticConsensus.MessageType.PREPARE;
import static ProbabilisticConsensus.MessageType.PRE_PREPARE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsensusNode implements MessageHandler {
	int nodeId;
	int nodeView;
	int lastExecutedSequenceNumber;
	List<ConsensusMessage> log;
	Map<Integer, ConsensusMessage> prePrepareBuffer;
	Map<Integer, ConsensusMessage> prepareBuffer;
	Map<Integer, ConsensusMessage> commitBuffer;

	private final ConsensusInterface nodeInterface;

	boolean isMasterNode;

	public ConsensusNode(int nodeId, ConsensusInterface nodeInterface) {
		this.nodeId = nodeId;
		this.nodeInterface = nodeInterface;
		this.nodeInterface.messageHandler(this);
		this.nodeView = 0;   // Pode ter nome melhor, mas corresponde a o valor que o nodo enxerga como correto
		this.lastExecutedSequenceNumber = 0;
		this.log = new ArrayList<>();
		this.prePrepareBuffer = new HashMap<>();
		this.prepareBuffer = new HashMap<>();
		this.commitBuffer = new HashMap<>();
		this.isMasterNode = false;
	}

	public void getConsensus() throws Exception { // Chama todos os procedimentos e retorna valor
		this.isMasterNode = true;
		this.startConsensusCall();
//		int byzantineTolerance =  this.nodeInterface.numberOfNodes();
	}

	private void startConsensusCall() throws Exception {
		ConsensusMessage msg_request = new ConsensusMessage(this.nodeView,
				this.lastExecutedSequenceNumber++,
				PRE_PREPARE,
				this.nodeId);
		this.nodeInterface.broadcastMessage(msg_request);
	}

	public void handleReceive(ConsensusMessage consensusMessage) throws Exception {
		System.out.println("Node " + this.nodeId + ": Received message from " + consensusMessage.getSenderId());
		switch (consensusMessage.getType()) {
		case PRE_PREPARE:
			if (consensusMessage.sequenceNumber > this.lastExecutedSequenceNumber){
				this.respondPrepare();
			} else {
				System.out.println("Recusado pedido de prePrepare");
			}
			break;
		case PREPARE:
			break;
		case COMMIT:
			break;
		default:
			break;
		}
	}

	private void receivePrePrepare() {
		System.out.println("Received PRE_PREPARE ");
	}

	private void respondPrepare() throws Exception {
		ConsensusMessage msg_request = new ConsensusMessage(this.nodeView,
				this.lastExecutedSequenceNumber++,
				PREPARE,
				this.nodeId);
		this.nodeInterface.broadcastMessage(msg_request);
	}

	private void respondCommit() throws Exception {
		ConsensusMessage msg_request = new ConsensusMessage(this.nodeView,
				this.lastExecutedSequenceNumber++,
				COMMIT,
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
