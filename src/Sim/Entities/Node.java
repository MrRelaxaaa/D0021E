package Sim.Entities;

// This class implements a node (host) it has an address, a peer that it communicates with
// and it count messages send and received.


import Sim.*;
import Sim.Events.*;
import Sim.Generators.CBR;
import Sim.Generators.Gaussian;
import Sim.Generators.Poisson;

public class Node extends SimEnt {
	private NetworkAddr _id;
	private NetworkAddr _homeID;
	private Router _homeAgent;
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

	public void setID(NetworkAddr _addr){
		this._id = _addr;
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

	public void sendRouterSolicitation(Router router, int time, NetworkAddr desiredAddr){
		send(router, new RouterSolicitation(((Link) _peer), this, desiredAddr), time);
	}

	public void sendReturnToHome(Router router, int time){
		send(router, new UnbindUpdate(((Link) _peer), this), time);
	}
	
	
	public NetworkAddr getAddr()
	{
		return _id;
	}

	public void set_homeAgent(Router ha){
		this._homeAgent = ha;
	}
	
//**********************************************************************************	
	// Just implemented to generate some traffic for demo.
	// In one of the labs you will create some traffic generators
	
	private int _stopSendingAfter = 0; //messages
	private int _toNetwork = 0;
	private int _toHost = 0;
	
	public void StartSending(int network, int node, int number, int startSeq)
	{
		_stopSendingAfter = number;
		_toNetwork = network;
		_toHost = node;
		_seq = startSeq;
		this.constant = new CBR(5);
		this.gauss = new Gaussian(5,2);
		this.poisson = new Poisson(10);
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

				/*
				* TimerEvents for CBR, Gaussian and Poisson Generators
				* */
				send(this, new TimerEvent(), this.constant.next());

				SimEngine.sent();
				System.out.println("Node "+_id.networkId()+ "." + _id.nodeId() +" sent message with seq: "+_seq
						+ " at time "+SimEngine.getTime());
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
			System.out.println("Node "+_id.networkId()+ "." + _id.nodeId() +" receives message with seq: "
					+((Message) ev).seq() + " at time "+SimEngine.getTime()  + " with delay " + delay + "ms");
        }
		if (ev instanceof RouterAdvertisement){
			System.out.println();
			System.out.println("MN " + _id.networkId() + "." + _id.nodeId() +" Received RA from Router...");
			System.out.println();
			this._homeID = _id;
			this._id = ((RouterAdvertisement) ev).get_addr();
			send(_homeAgent, new BindUpdate(_homeID, _id, this), 0);
		}
		if (ev instanceof BindAck){
			System.out.println();
			System.out.println("MN " + _id.networkId() + "." + _id.nodeId() +" Received BindAck from Home Agent...");
			System.out.println();
		}
		if (ev instanceof  UnbindAck){
			System.out.println();
			System.out.println("MN " + _id.networkId() + "." + _id.nodeId() +" Received UnbindAck from Home Agent, welcome home...");
			System.out.println();
			_id = _homeID;
			_homeID = null;
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
