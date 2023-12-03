package ProbabilisticConsensus;

import static ProbabilisticConsensus.MessageType.COMMIT;
import static ProbabilisticConsensus.MessageType.PREPARE;
import static ProbabilisticConsensus.MessageType.PRE_PREPARE;

import java.util.*;
import java.util.stream.Collectors;

public class ConsensusNode implements MessageHandler {
	int nodeId;
	int nodeView;
	int lastExecutedSequenceNumber;
	int nodeQuantity;
	List<ConsensusMessage> log;
	Map<Integer, ConsensusMessage> prePrepareBuffer;
	Map<Integer, ConsensusMessage> prepareBuffer;
	Map<Integer, ConsensusMessage> commitBuffer;

	private final ConsensusInterface nodeInterface;


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
//		this.isMasterNode = false;
		this.nodeQuantity = 4;
	}

	public void setNodeView(int nodeView) {
		this.nodeView = nodeView;
	}

	public int getConsensus() throws Exception { // Chama todos os procedimentos e retorna valor
		this.lastExecutedSequenceNumber++;
		this.startConsensusCall();
		int f = this.getBizantineTolerance();
		while (this.commitBuffer.size() < 2*f+1) {
			System.out.println("Esperando por nodos");
			Thread.sleep(10);
		}

		return this.getAgreedValue();
	}

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
//		this.lastExecutedSequenceNumber++;
		ConsensusMessage msg_request = new ConsensusMessage(this.nodeView,
				this.lastExecutedSequenceNumber,
				PRE_PREPARE,
				this.nodeId);
		this.nodeInterface.broadcastMessage(msg_request);
	}

	public void handleReceive(ConsensusMessage consensusMessage) throws Exception {
		if (consensusMessage.sequenceNumber < this.lastExecutedSequenceNumber) {
			System.out.println("Pedido recusado por sequenceNumber antigo");
		}

		else {
			this.lastExecutedSequenceNumber = consensusMessage.sequenceNumber;
//			System.out.println("Pedido ACEITO");
		}
		switch (consensusMessage.type) {
			case PRE_PREPARE -> {
				if (consensusMessage.senderId != this.nodeId)  {
					this.prepare();
				}

			}
			case PREPARE -> { // Deve esperar 2f recebidos de prepare (nao contando a si mesmo?)
				if (consensusMessage.senderId != this.nodeId)  {
					this.updatePrepareBuffer(consensusMessage);
					this.commit();
				}
			}
			case COMMIT -> this.updateCommitBuffer(consensusMessage);
			default -> {
			}
		}
	}

	private void prepare() throws Exception {
		System.out.println("NODE "+this.nodeId+" entrando em fase PREPARE");
		if (this.prepareBuffer.size() > 2*this.getBizantineTolerance()) {
			this.sendPrepare();
		}
	}

	private void commit() throws Exception {
		System.out.println("NODE "+this.nodeId+" entrando em fase COMMIT");
		this.sendCommit();
	}

	private void sendPrepare() throws Exception {
		ConsensusMessage msg_request = new ConsensusMessage(this.nodeView,
				this.lastExecutedSequenceNumber,
				PREPARE,
				this.nodeId);
		this.nodeInterface.broadcastMessage(msg_request);
	}

    private void sendCommit() throws Exception {
        ConsensusMessage msg_request = new ConsensusMessage(this.nodeView,
                this.lastExecutedSequenceNumber,
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

