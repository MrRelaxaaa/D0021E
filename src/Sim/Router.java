package Sim;

// This class implements a simple router

public class Router extends SimEnt{

	private RouteTableEntry [] _routingTable;
	private int _interfaces;
	private int _now=0;

	// When created, number of interfaces are defined
	
	Router(int interfaces)
	{
		_routingTable = new RouteTableEntry[interfaces];
		_interfaces=interfaces;
	}
	
	// This method connects links to the router and also informs the 
	// router of the host connects to the other end of the link
	
	public void connectInterface(int interfaceNumber, SimEnt link, SimEnt node)
	{
		if (interfaceNumber<_interfaces)
		{
			_routingTable[interfaceNumber] = new RouteTableEntry(link, node);
			System.out.println("Connected to router interface: " + interfaceNumber);
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
				System.out.println("Free interface no : " + interfaceNumber);
				break;
			}
		}
		return interfaceNumber;
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
				System.out.println("No node at specified destination... Dropping packet!");
			}
		}else if(event instanceof MobileEvent){
			/*
			* Change routing table settings
			* */
			System.out.println("Node " + ((MobileEvent) event).source().getAddr().networkId() + "." +
					((MobileEvent) event).source().getAddr().nodeId() + " requesting to move...");
			int newInterface = requestInterface();
			disconnectInterface(((MobileEvent) event).source());
			connectInterface(newInterface, ((MobileEvent) event).getLink(), ((MobileEvent) event).source());
			send(((MobileEvent) event).source(), new routerInterfaceAck(new NetworkAddr(newInterface+1, 1)), 0);

		}else if(event instanceof bindUpdateEv){
			/*
			* Route BindUpdate to CN
			**/
			SimEnt sendNext = getInterface(((bindUpdateEv) event).destination().networkId());
			if(sendNext != null) {
				System.out.println("Router recieved BindUpdate event from node: " + ((bindUpdateEv) event).source().networkId() +
						"." + ((bindUpdateEv) event).source().nodeId() + " Sending it too CN: " +
						((bindUpdateEv) event).destination().networkId() + "." + ((bindUpdateEv) event).destination().nodeId());
				send(sendNext, event, 0);
			}
		}
	}
}
