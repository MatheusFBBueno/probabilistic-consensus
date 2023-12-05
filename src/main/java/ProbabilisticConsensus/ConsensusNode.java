package ProbabilisticConsensus;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static ProbabilisticConsensus.MessageType.*;

public class ConsensusNode implements MessageHandler {
	int nodeId;
	String nodeValue;
	int lastExecutedSequenceNumber;
	List<ConsensusMessage> log;
	ConcurrentHashMap<Integer, ConsensusMessage> prePrepareBuffer;
	ConcurrentHashMap<Integer, ConsensusMessage> prepareBuffer;
	ConcurrentHashMap<Integer, ConsensusMessage> commitBuffer;

	AtomicBoolean alreadyPrepared = new AtomicBoolean(false);
	AtomicBoolean alreadyCommitted = new AtomicBoolean(false);
	private final ConsensusInterface nodeInterface;

	private final NodeSenderInterface nodeSenderInterface;


	public ConsensusNode(int nodeId, ConsensusInterface nodeInterface, NodeSenderInterface nodeSenderInterface) {
		this.nodeId = nodeId;
		this.nodeInterface = nodeInterface;
		this.nodeSenderInterface = nodeSenderInterface;
		this.nodeInterface.messageHandler(this);
		this.nodeValue = "0";   // Pode ter nome melhor, mas corresponde a o valor que o nodo enxerga como correto
		this.lastExecutedSequenceNumber = 0;
		this.log = new ArrayList<>();
		this.prePrepareBuffer = new ConcurrentHashMap<>();
		this.prepareBuffer = new ConcurrentHashMap<>();
		this.commitBuffer = new ConcurrentHashMap<>();
	}

	public void setNodeValue(String nodeValue) {
		this.nodeValue = nodeValue;
	}

	public void getConsensus() throws Exception { // Chama todos os procedimentos e retorna valor
		this.lastExecutedSequenceNumber++;
		this.startConsensusCall();
	}

	private String getAgreedValue() {
		// Count occurrences of each view
		Map<String, Long> viewCounts = this.commitBuffer.values().stream()
				.collect(Collectors.groupingBy(ConsensusMessage::getValue, Collectors.counting()));

		// Find the view with the maximum occurrence
		Optional<Map.Entry<String, Long>> maxEntry = viewCounts.entrySet().stream()
				.max(Map.Entry.comparingByValue());

		if (maxEntry.isPresent()) {
			return maxEntry.get().getKey();
		} else {
			throw new IllegalStateException("Unable to find the most common view");
		}
	}

	private int getBizantineTolerance() {
		return (int) Math.floor((float) (this.nodeInterface.nodeQuantity()-1)/3);
	}

	private void startConsensusCall() throws Exception {
//		this.lastExecutedSequenceNumber++;
		ConsensusMessage msg_request = new ConsensusMessage(this.nodeValue,
				this.lastExecutedSequenceNumber,
				PRE_PREPARE,
				this.nodeId);
		this.nodeInterface.broadcastMessage(msg_request);
	}

	public void handleReceive(ConsensusMessage consensusMessage) throws Exception {
		this.log.add(consensusMessage);
		if (consensusMessage.sequenceNumber < this.lastExecutedSequenceNumber) {
			System.out.println("Pedido recusado por sequenceNumber antigo");
		}

		else {
			this.lastExecutedSequenceNumber = consensusMessage.sequenceNumber;
		}
		switch (consensusMessage.type) {
			case PRE_PREPARE -> {
				if (consensusMessage.senderId != this.nodeId)  {
					this.prepare();
				}
			}
			case PREPARE -> {
				this.updatePrepareBuffer(consensusMessage);
				if (this.prepareBuffer.size() > 2 * this.getBizantineTolerance() && !this.alreadyPrepared.get()) {
					this.alreadyPrepared.set(true);
					this.commit();
				}
			}
			case COMMIT -> {
				this.updateCommitBuffer(consensusMessage);

				if (this.commitBuffer.size() > 2 * this.getBizantineTolerance() && !this.alreadyCommitted.get()) {
					this.nodeValue = this.getAgreedValue();
					this.alreadyCommitted.set(true);
					System.out.println("NODE "+this.nodeId+" entrando em fase de REPLY");
					this.sendReply(this.nodeValue);
				}
			}
			default -> {
			}
		}
	}

	private void prepare() throws Exception {
		System.out.println("NODE "+this.nodeId+" entrando em fase PREPARE");
		this.sendPrepare();
	}

	private void commit() throws Exception {
		if (this.commitBuffer.containsKey(this.nodeId)) {
			return;
		}
		if (this.prepareBuffer.size() > 2*this.getBizantineTolerance()) {
			System.out.println("NODE "+this.nodeId+" entrando em fase COMMIT");
			this.sendCommit();
		}
	}

	private void sendPrepare() throws Exception {
		ConsensusMessage msg_request = new ConsensusMessage(this.nodeValue,
				this.lastExecutedSequenceNumber,
				PREPARE,
				this.nodeId);
		this.nodeInterface.broadcastMessage(msg_request);
	}

    private void sendCommit() throws Exception {
        ConsensusMessage msg_request = new ConsensusMessage(this.nodeValue,
                this.lastExecutedSequenceNumber,
                COMMIT,
                this.nodeId);
        this.nodeInterface.broadcastMessage(msg_request);
    }

	private void sendReply(String value){
		this.nodeSenderInterface.send(value);
	}

	private void updateCommitBuffer(ConsensusMessage msg) {
        this.commitBuffer.putIfAbsent(msg.senderId, msg);
    }

    private void updatePrepareBuffer(ConsensusMessage msg) {
        this.prepareBuffer.putIfAbsent(msg.senderId, msg);
    }

}
