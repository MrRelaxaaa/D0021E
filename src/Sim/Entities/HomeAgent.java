package Sim.Entities;

import Sim.Event;
import Sim.Events.*;
import Sim.NetworkAddr;
import Sim.SimEnt;
import Sim.Tables.AgentTableEntry;

import java.util.ArrayList;

/**
 * Created by Hultstrand on 2018-02-27.
 */
public class HomeAgent extends Node {
    private SimEnt _nodeLink;
    private SimEnt _routerLink;
    ArrayList<AgentTableEntry> _agenttable = new ArrayList<>();


    public HomeAgent(int network, int node, Sink sink) {
        super(network, node, sink);
        AgentTableEntry entry = new AgentTableEntry(new NetworkAddr(network, node), null);
        _agenttable.add(entry);
    }

    public void connectNode(SimEnt _peer) {
        this._nodeLink = _peer;
        ((Link) _nodeLink).setConnector(this);

    }

    public void connectRouter(SimEnt _peer) {
        this._routerLink = _peer;
        ((Link) _routerLink).setConnector(this);
    }

    public void disconnectNode() {
        ((Link) _nodeLink).unsetConnector(this);
    }

    public void set_careOfAddr(NetworkAddr homeAddr, NetworkAddr careOfAddr) {
        for (AgentTableEntry agentTableEntry : _agenttable) {
            if (agentTableEntry.get_homeAddr() == homeAddr) {
                agentTableEntry.set_careOfAddr(careOfAddr);
                break;
            }
        }
    }

    public void recv(SimEnt source, Event ev) {
        if (ev instanceof Message) {
            if (source == _nodeLink) {
                send(_routerLink, ev, 0);
            } else {
                send(_nodeLink, ev, 0);
            }
        }
        if (ev instanceof MobileEv) {
            send(_routerLink, ev, 0);
        }

        if (ev instanceof RouterInterfaceAck) {
            send(_nodeLink, ev, 0);
            disconnectNode();
        }

        if (ev instanceof BindUpdateEv) {
            System.out.println("Received a bind update from node: " + ((BindUpdateEv) ev).source().networkId() + "." + ((BindUpdateEv) ev).source().nodeId());
            set_careOfAddr(((BindUpdateEv) ev).old(), ((BindUpdateEv) ev).source());
            //SEND BIND ACK TO MN HERE
        }

        if (ev instanceof RouterSolEv) {
            send(_routerLink, new RouterSolEv(), 0);
        }
    }
}
