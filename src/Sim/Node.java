package Sim;

// This class implements a node (host) it has an address, a peer that it communicates with
// and it count messages send and received.

import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.ArrayList;

public class Node extends SimEnt {
	private NetworkAddr _id;
	private SimEnt _peer;
	private double _sentmsg=0;
	private double _recievedmsg=0;
	private int _seq = 0;
	private double prevDelay = 0;
	private double jitter = 0;
	private ArrayList<Double> overallJitter = new ArrayList<>();
	
	public Node (int network, int node)
	{
		super();
		_id = new NetworkAddr(network, node);
	}	
	
	
	// Sets the peer to communicate with. This node is single homed
	
	public void setPeer (SimEnt peer)
	{
		_peer = peer;
		
		if(_peer instanceof Link )
		{
			 ((Link) _peer).setConnector(this);
		}
	}
	
	
	public NetworkAddr getAddr()
	{
		return _id;
	}
	
//**********************************************************************************	
	// Just implemented to generate some traffic for demo.
	// In one of the labs you will create some traffic generators
	
	private int _stopSendingAfter = 0; //messages
	private int _timeBetweenSending = 10; //time between messages
	private int _toNetwork = 0;
	private int _toHost = 0;
	
	public void StartSending(int network, int node, int number, int timeInterval, int startSeq)
	{
		_stopSendingAfter = number;
		_timeBetweenSending = timeInterval;
		_toNetwork = network;
		_toHost = node;
		_seq = startSeq;
		send(this, new TimerEvent(),0);	
	}
	
//**********************************************************************************	
	
	// This method is called upon that an event destined for this node triggers.

	public void recv(SimEnt src, Event ev)
	{
		if (ev instanceof TimerEvent)
		{			
			if (_stopSendingAfter > _sentmsg)
			{
				_sentmsg++;
				send(_peer, new Message(_id, new NetworkAddr(_toNetwork, _toHost),_seq),0);
				send(this, new TimerEvent(),_timeBetweenSending);
				System.out.println("Node "+_id.networkId()+ "." + _id.nodeId() +" sent message with seq: "+_seq + " at time "+SimEngine.getTime());
				_seq++;
			}
		}
		if (ev instanceof Message)
		{
			_recievedmsg++;
            setJitter(ev);
			System.out.println("Node "+_id.networkId()+ "." + _id.nodeId() +" receives message with seq: "+((Message) ev).seq() + " at time "+SimEngine.getTime()  + " with a delay of " + (SimEngine.getTime()-((Message) ev).start_time) + "ms" + " and a Jitter of: " + this.jitter + "ms");
			if(_recievedmsg > 1) {
                overallJitter.add(jitter);
            }
        }
	}

	public void setJitter(Event ev){
        if(_recievedmsg > 1){
            double currDelay = Math.abs(SimEngine.getTime()-((Message) ev).start_time);
            this.jitter = Math.abs(currDelay - this.prevDelay);
            this.prevDelay = currDelay;
        }else {
            this.prevDelay = (SimEngine.getTime() - ((Message) ev).start_time);
        }
    }

	public double avgJitter(){
		double average = 0;
		for(int i = 0; i < overallJitter.size(); i++){
			average += overallJitter.get(i);
		}
		return average = (average/overallJitter.size())-1;
	}

    public double sent(){return _sentmsg;}

    public double recieved(){return _recievedmsg;}
}
