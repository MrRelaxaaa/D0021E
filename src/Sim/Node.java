package Sim;

// This class implements a node (host) it has an address, a peer that it communicates with
// and it count messages send and received.

import com.sun.org.apache.xpath.internal.SourceTree;


public class Node extends SimEnt {
	private NetworkAddr _id;
	private SimEnt _peer;
	private double _sentmsg=0;
	private double _recievedmsg=0;
	private int _seq = 0;
	private double prevDelay = 0;
	private double jitter = 0;
	Gaussian gauss;
	CBR constant;
	Poisson poisson;
	Sink sink;

	public Node (int network, int node, Sink sink)
	{
		super();
		_id = new NetworkAddr(network, node);
		this.sink = sink;
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
	private int _toNetwork = 0;
	private int _toHost = 0;
	
	public void StartSending(int network, int node, int number, int startSeq, CBR c, Gaussian g, Poisson p)
	{
		_stopSendingAfter = number;
		_toNetwork = network;
		_toHost = node;
		_seq = startSeq;
		this.constant = c;
		this.gauss = g;
		this.poisson = p;
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
				//send(this, new TimerEvent(), this.constant.next()); /* CBR */
				//send(this, new TimerEvent(), this.gauss.next());	/* Gaussian */
				send(this, new TimerEvent(), this.poisson.next());	/* Poisson */
				SimEngine.sent();
				System.out.println("Node "+_id.networkId()+ "." + _id.nodeId() +" sent message with seq: "+_seq + " at time "+SimEngine.getTime());
				_seq++;
			}
		}
		if (ev instanceof Message)
		{
			_recievedmsg++;
            setJitter(ev);
            double delay = (SimEngine.getTime()-((Message) ev).start_time);
            this.sink.addDelay(delay);
			SimEngine.received();
			System.out.println("Node "+_id.networkId()+ "." + _id.nodeId() +" receives message with seq: "+((Message) ev).seq() + " at time "+SimEngine.getTime()  + " with delay " + delay + "ms");
        }
	}

	public void setJitter(Event ev){
        if(_recievedmsg > 1){
            double currDelay = Math.abs(SimEngine.getTime()-((Message) ev).start_time);
            this.jitter = Math.abs(currDelay - this.prevDelay);
            this.prevDelay = currDelay;
			sink.addJitter(jitter);
        }else {
            this.prevDelay = (SimEngine.getTime() - ((Message) ev).start_time);
        }
    }
}
