package Sim.Entities;

// This class implements a link without any loss, jitter or delay

import Sim.Event;
import Sim.Events.BindUpdateEv;
import Sim.Events.Message;
import Sim.Events.MobileEv;
import Sim.Events.RouterInterfaceAck;
import Sim.SimEnt;

public class Link extends SimEnt {
	private SimEnt _connectorA=null;
	private SimEnt _connectorB=null;
	private int _now=0;
	
	public Link()
	{
		super();
	}

	// Connects the link to some simulation entity like
	// a node, switch, router etc.
	
	public void setConnector(SimEnt connectTo)
	{
		if (_connectorA == null) 
			_connectorA=connectTo;
		else
			_connectorB=connectTo;
	}

	public void unsetConnector(SimEnt disconnectFrom)
	{
		if (_connectorA == disconnectFrom)
			_connectorA=null;
		else if (_connectorB == disconnectFrom)
			_connectorB=null;
	}


	// Called when a message enters the link
	
	public void recv(SimEnt src, Event ev)
	{
		if (ev instanceof Message || ev instanceof MobileEv || ev instanceof BindUpdateEv || ev instanceof RouterInterfaceAck)
		{
			System.out.println("Link recv msg, passes it through");
			if (src == _connectorA)
			{
				send(_connectorB, ev, _now);
			}
			else
			{
				send(_connectorA, ev, _now);
			}
		}
	}
}