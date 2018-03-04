package Sim.Entities;

// This class implements a simple router

import Sim.*;
import Sim.Events.*;
import Sim.Tables.AgentTableEntry;
import Sim.Tables.RouteTableEntry;

/**
 * @_agentTable holds information in the Home Agent about nodes that are mobile
 * @_otherRouter holds the other router so it can be accessed
 * */
public class Router extends SimEnt {

	private RouteTableEntry[] _routingTable;
	private AgentTableEntry[] _agentTable;
	private Message[] _buffer;
	private int _bufferSize;
	private int _interfaces;
	private Router _otherRouter;
	private int _now=0;

	// When created, number of interfaces are defined
	
	public Router(int interfaces, int bufferSize)
	{
		_routingTable = new RouteTableEntry[interfaces];
		_agentTable = new AgentTableEntry[interfaces];
		_buffer = new Message[bufferSize];
		_bufferSize = bufferSize;
		_interfaces=interfaces;
	}
	
	// This method connects links to the router and also informs the 
	// router of the host connects to the other end of the link
	
	public void connectInterface(int interfaceNumber, SimEnt link, SimEnt node)
	{
		if (interfaceNumber<_interfaces)
		{
			_routingTable[interfaceNumber] = new RouteTableEntry(link, node);
		}
		else
			System.out.println("Trying to connect to port not in router");
		
		((Link) link).setConnector(this);
	}

	/* Need this to disconnect from interface */
	public void disconnectInterface(SimEnt node){
		for(int i = 0; i < _interfaces; i++){
			if(_routingTable[i] != null){
				if(node == _routingTable[i].node()){
					_routingTable[i] = null;
				}
			}
		}
	}

	// This method searches for an entry in the agent table that matches
	// the network number in the destination field of a messages, if not found, then check
	// the routing table. The link represents that network number is returned
	private SimEnt getInterface(int networkAddress)
	{
		SimEnt routerInterface=null;
		for (int i = 0; i < _interfaces; i++) {
			if (_agentTable[i] != null){
				if (_agentTable[i].get_oldId().networkId() == networkAddress){
					routerInterface = _agentTable[i].get_node();
					return routerInterface;
				}
			}
		}
		for(int i=0; i<_interfaces; i++)
			if (_routingTable[i] != null)
			{
				if(((Node) _routingTable[i].node()).getAddr() != null){
					if (((Node) _routingTable[i].node()).getAddr().networkId() == networkAddress)
					{
						routerInterface = _routingTable[i].link();
					}
				}
			}
		return routerInterface;
	}

	// This method simply returns the first available interface
	private int requestInterface(){
		int interfaceNumber = 0;
		for(int i=0; i<_interfaces; i++){
			if(_routingTable[i] == null){
				interfaceNumber = i;
				break;
			}
		}
		return interfaceNumber;
	}

	// This method adds an entry into the agent table
	private void insertIntoAgentTable(NetworkAddr addr, SimEnt node, NetworkAddr newID){
		for (int i = 0; i < _interfaces; i++) {
			if(_agentTable[i] == null){
				_agentTable[i] = new AgentTableEntry(addr, node, newID);
				break;
			}
		}
	}

	// This method sets the other router
	public void set_otherRouter(Router router){
		_otherRouter = router;
	}

	// This method deletes the specified node from the agent table
	private void deleteFromAgentTable(SimEnt node){
		for (int i = 0; i < _interfaces; i++) {
			if(_agentTable[i] != null){
				if(_agentTable[i].get_node() == node){
					_agentTable[i] = null;
					break;
				}
			}
		}
	}

	// When messages are received at the router this method is called
	public void recv(SimEnt source, Event event)
	{
		if (event instanceof Message)
		{
			System.out.println("Router handles packet with seq: " + ((Message) event).seq()+" from node: "+
					((Message) event).source().networkId()+"." + ((Message) event).source().nodeId() );
			SimEnt sendNext = getInterface(((Message) event).destination().networkId());

			if(sendNext != null) {
				System.out.println("Router sends to node: " + ((Message) event).destination().networkId() + "." +
						((Message) event).destination().nodeId());
				send(sendNext, event, _now);
			}else{
				// If the event was received from another router,
				// then the destination does not exist. Otherwise
				// check if the destination is on another router network.
				if(source instanceof Router){
					System.out.println("-----------------------------------");
					System.out.println("Router cannot find destination for packet with seq: " + ((Message) event).seq() + "... Buffering it...");
					System.out.println("-----------------------------------");
					for (int i = 0; i < _bufferSize; i++) {
						if(_buffer[i] == null){
							_buffer[i] = ((Message) event);
							return;
						}
					}
					System.out.println("-----------------------------------");
					System.out.println("Buffer full, dropping packet with seq: " + ((Message) event).seq());
					System.out.println("-----------------------------------");
				}else{
					System.out.println("-----------------------------------");
					System.out.println("Router cannot find destination... Sending to other network");
					System.out.println("-----------------------------------");
					send(_otherRouter, event, 0);
				}
			}
			// If the event received is a Router Solicitation,
			// we need to connect the node that sent the request,
			// and reply with a Router Advertisement and deliver an IP.
		} else if ( event instanceof RouterSolicitation){
			System.out.println("-----------------------------------");
			System.out.println("Router received RS from HA ");
			System.out.println("-----------------------------------");
			int assignedInterface = requestInterface();
			connectInterface(assignedInterface, ((RouterSolicitation) event).get_link(), ((RouterSolicitation) event).get_node());
			send(((RouterSolicitation) event).get_link(), new RouterAdvertisement(((RouterSolicitation) event).get_addr()), 0);
			// If the event is a Bind Update, we need to know
			// if the event is being sent to the Home Agent.
			// If it is being sent to Home Agent, set event
			// value @_toHomeAgent to false and send to @_otherRouter.
		} else if (event instanceof BindUpdate){
			if(((BindUpdate) event).get_toWhom() == BindUpdateToWhom.HA){
				((BindUpdate) event).set_toWhom(BindUpdateToWhom.THIS);
				send(_otherRouter, event, 0);
			}else if(((BindUpdate) event).get_toWhom() == BindUpdateToWhom.THIS){
				System.out.println("-----------------------------------");
				System.out.println("HomeAgent received BindUpdate from HA " + ((BindUpdate) event).get_node().get_homeID().networkId() + "." +
						((BindUpdate) event).get_node().get_homeID().nodeId() + " whose new address is: " + ((BindUpdate) event).get_node().getAddr().networkId() + "." +
						((BindUpdate) event).get_node().getAddr().nodeId());
				System.out.println("-----------------------------------");
				for (int i = 0; i < _bufferSize; i++) {
					if(_buffer[i] != null){
						SimEnt dest = getInterface(_buffer[i].destination().networkId());
						if(dest != null){
							System.out.println("-----------------------------------");
							System.out.println("Sending buffered message with seq: " + _buffer[i].seq() + " to node " + _buffer[i].destination().networkId() + _buffer[i].destination().nodeId());
							System.out.println("-----------------------------------");
							send(dest, _buffer[i], 0);
						}else{
							System.out.println("-----------------------------------");
							System.out.println("Sending buffered message with seq: " + _buffer[i].seq() + " to other network...");
							System.out.println("-----------------------------------");
							send(_otherRouter, _buffer[i], 0);
						}
					}
				}
				if(((BindUpdate) event).connectTo() == BindUpdateConnectFlag.CONNECT){
					insertIntoAgentTable(((BindUpdate) event).get_oldAddr(), ((BindUpdate) event).get_node(), ((BindUpdate) event).get_node().getAddr());
					send(((BindUpdate) event).get_node(), new BindAck(true), 0);
				}else{
					deleteFromAgentTable(((BindUpdate) event).get_node());
					connectInterface(requestInterface(),((BindUpdate) event).get_link(), ((BindUpdate) event).get_node());
					send(((BindUpdate) event).get_link(), new BindAck(false), 0);
				}
			}else if(((BindUpdate) event).get_toWhom() == BindUpdateToWhom.CN){
				System.out.println("-----------------------------------");
				System.out.println("Router received BindUpdate destined for CN...");
				System.out.println("-----------------------------------");
				SimEnt sendNext = getInterface(((BindUpdate) event).get_node().get_CN().networkId());
				send(sendNext, event, 0);
			}
		}
	}
}
