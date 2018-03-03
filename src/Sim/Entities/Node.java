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
	private NetworkAddr _CN;
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

	public NetworkAddr get_homeID() { return _homeID; }

	public NetworkAddr get_CN() {return _CN;}
	
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
		_CN = new NetworkAddr(_toNetwork, _toHost);
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
				if(_id != null){
					_sentmsg++;
					send(_peer, new Message(_id, new NetworkAddr(_toNetwork, _toHost),_seq),0);
					SimEngine.sent();
					System.out.println("Node "+_id.networkId()+ "." + _id.nodeId() +" sent message with seq: "+_seq
							+ " to " + _toNetwork + "." + _toHost + " at time "+SimEngine.getTime());
					_seq++;
				}
				/*
				* TimerEvents for CBR, Gaussian and Poisson Generators
				* */
				send(this, new TimerEvent(), this.constant.next());

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
        /**
		 * Handoff event is triggered and disconnects
		 * from router interfaces and sends Router Solicitation event
		 * to the router it wants to connect to.
		 * */
        if (ev instanceof Handoff){
			System.out.println("-----------------------------------");
			System.out.println("Handoff triggered...");
			System.out.println("-----------------------------------");
			if(((Handoff) ev).is_movingHome()){
				((Handoff) ev).get_disconnectFrom().disconnectInterface(this);
				send(((Handoff) ev).get_connectTo(), new RouterSolicitation(((Link) _peer), this, ((Handoff) ev).get_desiredAddr()), 0);
			}else{
				((Handoff) ev).get_disconnectFrom().disconnectInterface(this);
				_homeID = _id;
				_id = null;
				send(((Handoff) ev).get_connectTo(), new RouterSolicitation(((Link) _peer), this, ((Handoff) ev).get_desiredAddr()), 0);
			}
		}
		/**
		 * Router Advertisement event is received,
		 * set the nodes new _id and send a Bind Update event to Home Agent.
		 * */
		if (ev instanceof RouterAdvertisement){
			System.out.println("-----------------------------------");
			System.out.println("MN Received RA from Router and has id " + ((RouterAdvertisement) ev).get_addr().networkId() + "." + ((RouterAdvertisement) ev).get_addr().nodeId());
			System.out.println("-----------------------------------");
			this._id = ((RouterAdvertisement) ev).get_addr();
			BindUpdate bindUpdate = new BindUpdate(_homeID, this, BindUpdateToWhom.HA);
			bindUpdate.set_connectFlag(BindUpdateConnectFlag.CONNECT);
			send(_peer, bindUpdate, 0);
		}
		/**
		 * Bind Acknowledgement received from Home Agent,
		 * print some messages.
		 * */
		if (ev instanceof BindAck){
			if(((BindAck) ev).get_flag()){
				System.out.println("-----------------------------------");
				System.out.println("MN " + _id.networkId() + "." + _id.nodeId() +" Received BindAck from Home Agent...");
				System.out.println("-----------------------------------");
				//Update CN with MN's new address
				BindUpdate bindUpdate = new BindUpdate(_homeID, this, BindUpdateToWhom.CN);
				send(_peer, bindUpdate, 0);
			}else if(!((BindAck) ev).get_flag()){
				System.out.println("-----------------------------------");
				System.out.println("MN " + _id.networkId() + "." + _id.nodeId() +" Received BindAck from Home Agent, welcome home...");
				System.out.println("-----------------------------------");
			}
		}
		if (ev instanceof BindUpdate){
			System.out.println("-----------------------------------");
			System.out.println("CN Received a BindUpdate, now has new Address of MN...");
			System.out.println("-----------------------------------");
			_toNetwork = ((BindUpdate) ev).get_node()._id.networkId();
			_toHost = ((BindUpdate) ev).get_node()._id.nodeId();
			_CN = ((BindUpdate) ev).get_node()._id;
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
