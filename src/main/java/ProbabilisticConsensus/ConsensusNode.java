package ProbabilisticConsensus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsensusNode {
	int nodeId;
	int nodeView;
	int lastExecutedSequenceNumber;
	List<ConsensusMessage> log;
	Map<Integer, ConsensusMessage> prePrepareBuffer;
	Map<Integer, List<ConsensusMessage>> prepareBuffer;
	Map<Integer, List<ConsensusMessage>> commitBuffer;

	private final ConsensusInterface nodeInterface;

	boolean isMasterNode;

	public ConsensusNode(int nodeId, ConsensusInterface nodeInterface) {
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

	public void getConsensus() throws Exception { // Chama todos os procedimentos e retorna valor
		this.isMasterNode = true;
		this.startConsensusCall();
//		int byzantineTolerance =  this.nodeInterface.numberOfNodes();
	}

	private void startConsensusCall() throws Exception {
		ConsensusMessage msg_request = new ConsensusMessage(this.nodeView,
				this.lastExecutedSequenceNumber++,
				MessageType.PREPREPARE,
				this.nodeId);
		this.nodeInterface.broadcastMessage(msg_request);
	}

	private void receivePrePrepare() {

	}

	private void respondPrepare() throws Exception {
		ConsensusMessage msg_request = new ConsensusMessage(this.nodeView,
				this.lastExecutedSequenceNumber++,
				MessageType.PREPARE,
				this.nodeId);
		this.nodeInterface.broadcastMessage(msg_request);
	}

	private void respondCommit() throws Exception {
		ConsensusMessage msg_request = new ConsensusMessage(this.nodeView,
				this.lastExecutedSequenceNumber++,
				MessageType.COMMIT,
				this.nodeId);
		this.nodeInterface.broadcastMessage(msg_request);
	}
}
