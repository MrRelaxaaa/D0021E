package Sim.Entities;

// This class implements a simple router

import Sim.Event;
import Sim.Events.*;
import Sim.NetworkAddr;
import Sim.SimEnt;
import Sim.Tables.AgentTableEntry;
import Sim.Tables.RouteTableEntry;

public class Router extends SimEnt {

	private RouteTableEntry[] _routingTable;
	private AgentTableEntry[] _agentTable;
	private Message[] _buffer;
	private Router _otherRouter;
	private int _interfaces;
	private int _bufferSize;

	// When created, number of interfaces are defined
	
	public Router(int interfaces, int bufferSize)
	{
		_routingTable = new RouteTableEntry[interfaces];
		_agentTable = new AgentTableEntry[interfaces];
		_buffer = new Message[bufferSize];
		_interfaces=interfaces;
		_bufferSize = bufferSize;
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

	// This method searches for an entry in the routing table that matches
	// the network number in the destination field of a messages. The link
	// represents that network number is returned
	private SimEnt getInterface(int networkAddress)
	{
		SimEnt routerInterface=null;
		for (int i = 0; i < _interfaces; i++) {
			if (_agentTable[i] != null){
				if (_agentTable[i].get_id().networkId() == networkAddress){
					routerInterface = _agentTable[i].get_node();
					return routerInterface;
				}
			}
		}
		for(int i=0; i<_interfaces; i++)
			if (_routingTable[i] != null)
			{
				if (((Node) _routingTable[i].node()).getAddr().networkId() == networkAddress)
				{
					routerInterface = _routingTable[i].link();
				}
			}
		return routerInterface;
	}

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

	private void insertIntoAgentTable(NetworkAddr addr, SimEnt node){
		for (int i = 0; i < _interfaces; i++) {
			if(_agentTable[i] == null){
				_agentTable[i] = new AgentTableEntry(addr, node);
				break;
			}
		}
	}

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

	public void set_otherRouter(Router router){
		_otherRouter = router;
	}
	// When messages are received at the router this method is called
	public void recv(SimEnt source, Event event)
	{
		if (event instanceof Message)
		{
			if(((Message) event).get_ttl() == 0){
				for (int i = 0; i < _bufferSize; i++) {
					if(_buffer[i] == null){
						System.out.println("------------------------------------------------------");
						System.out.println("Cannot find destination, adding message with sequence: " + ((Message) event).seq() + " to buffer");
						System.out.println("------------------------------------------------------");
						_buffer[i] = ((Message) event);
						break;
					}else if(i == _bufferSize-1){
						System.out.println("------------------------------------------------------");
						System.out.println("Buffer is full, dropping packet with sequence: " + ((Message) event).seq());
						System.out.println("------------------------------------------------------");
						return;
					}
				}
			}else {
				((Message) event).decrementTimeToLive();
				System.out.println("Router handles packet with seq: " + ((Message) event).seq() + " from node: " +
						((Message) event).source().networkId() + "." + ((Message) event).source().nodeId());
				SimEnt sendNext = getInterface(((Message) event).destination().networkId());

				if (sendNext != null) {
					System.out.println("Router sends to node: " + ((Message) event).destination().networkId() + "." +
							((Message) event).destination().nodeId());
					send(sendNext, event, 0);
				} else {
					send(_otherRouter, event, 0);
				}
			}
		}
		if ( event instanceof RouterSolicitation){
			System.out.println("------------------------------------------------------");
			System.out.println("Router received RS from MN " + ((RouterSolicitation) event).get_node().getAddr().networkId() + "." + ((RouterSolicitation) event).get_node().getAddr().nodeId());
			System.out.println("------------------------------------------------------");
			int assignedInterface = requestInterface();
			connectInterface(assignedInterface, ((RouterSolicitation) event).get_link(), ((RouterSolicitation) event).get_node());
			send(((RouterSolicitation) event).get_link(), new RouterAdvertisement(((RouterSolicitation) event).get_addr()), 0);
		}
		if (event instanceof BindUpdate){
			System.out.println("------------------------------------------------------");
			System.out.println("HomeAgent received BindUpdate from MN " + ((BindUpdate) event).get_node().getAddr().networkId() + "." + ((BindUpdate) event).get_node().getAddr().nodeId());
			System.out.println("------------------------------------------------------");
			disconnectInterface(((BindUpdate) event).get_node());
			insertIntoAgentTable(((BindUpdate) event).get_oldAddr(), ((BindUpdate) event).get_node());
			send(((BindUpdate) event).get_node(), new BindAck(), 0);
			for (int i = 0; i < _bufferSize; i++) {
				if(_buffer[i] != null){
					System.out.println(_buffer[i].seq() + " buffer SEQ");
					send(((BindUpdate) event).get_node(), _buffer[i], 0);
					_buffer[i] = null;
				}
			}
		}
		if (event instanceof UnbindUpdate){
			System.out.println("------------------------------------------------------");
			System.out.println("HomeAgent received UnbindUpdate from MN " + ((UnbindUpdate) event).get_node().getAddr().networkId() + "." + ((UnbindUpdate) event).get_node().getAddr().nodeId());
			System.out.println("------------------------------------------------------");
			deleteFromAgentTable(((UnbindUpdate) event).get_node());
			connectInterface(requestInterface(),((UnbindUpdate) event).get_link(), ((UnbindUpdate) event).get_node());
			send(((UnbindUpdate) event).get_link(), new UnbindAck(), 0);
		}
	}
}
