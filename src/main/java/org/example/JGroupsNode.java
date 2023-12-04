package org.example;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

import ProbabilisticConsensus.ConsensusInterface;
import ProbabilisticConsensus.ConsensusMessage;
import ProbabilisticConsensus.MessageHandler;


public class JGroupsNode extends ReceiverAdapter implements ConsensusInterface {

	private JChannel channel;

	private MessageHandler messageHandler;

	public void start() throws Exception {
		channel = new JChannel();
		channel.setReceiver(this);
		channel.connect("MyCluster");
//		channel.close();
	}

	public synchronized void receive(Message msg) {
		ConsensusMessage consensusMessage = msg.getObject();
		if (messageHandler != null) {
			try {
				messageHandler.handleReceive(consensusMessage);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void broadcastMessage(ConsensusMessage message) throws Exception {
		Message msg = new Message(null, message);
		channel.send(msg);
	}

	public void messageHandler(MessageHandler handler) {
		this.messageHandler = handler;
	}

	public int nodeQuantity() {
		return channel.getView().getMembers().size();
	}
}
