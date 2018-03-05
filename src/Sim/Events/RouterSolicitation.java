package Sim.Events;

import Sim.Entities.Link;
import Sim.Entities.Node;
import Sim.Entities.Router;
import Sim.Event;
import Sim.NetworkAddr;
import Sim.SimEnt;

/**
 * Created by Hultstrand on 2018-02-27.
 * This class represents a Router Solicitation event which in MIPv6
 * is send from a node to routers telling them to make themselves known
 * by sending a Router Advertisement event as response.
 */
public class RouterSolicitation implements Event {
    private Link _link;
    private Node _node;
    private NetworkAddr _addr;

    /**
     * Costuctor takes the link the node wishes to use for connection,
     * the node itself and the desired address of the node on the new network.
     * @param link
     * @param node
     * @param addr
     */
    public RouterSolicitation(Link link, Node node, NetworkAddr addr){
        this._link = link;
        this._node = node;
        this._addr = addr;
    }

    public Link get_link() { return _link; }

    public Node get_node() { return _node; }

    public NetworkAddr get_addr() { return _addr; }

    public void entering(SimEnt locale) {

    }
}
