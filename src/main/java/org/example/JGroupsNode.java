package org.example;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

import ProbabilisticConsensus.ConsensusInterface;
import ProbabilisticConsensus.ConsensusMessage;

public class JGroupsNode extends ReceiverAdapter implements ConsensusInterface {

	public JChannel channel;

	public void start() throws Exception {
		channel = new JChannel();
		channel.setReceiver(this);
		channel.connect("MyCluster");
//		channel.close();
	}

	public void receive(Message msg) {
		receiveMessage(msg.getObject());
	}

	public void broadcastMessage(ConsensusMessage message) throws Exception {
		System.out.println("Broadcasting message: " + message);
		Message msg = new Message(null, message);
		channel.send(msg);
	}

	public void receiveMessage(ConsensusMessage message) {
		System.out.println("Received message: " + message);
	}
}
