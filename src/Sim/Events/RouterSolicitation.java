package Sim.Events;

import Sim.Entities.Link;
import Sim.Entities.Node;
import Sim.Entities.Router;
import Sim.Event;
import Sim.NetworkAddr;
import Sim.SimEnt;

/**
 * Created by Hultstrand on 2018-02-27.
 * @_link the link that the node wishes to connect on
 * @_node the node that wishes to connect
 * @_addr the desired network address on the new network
 */
public class RouterSolicitation implements Event {
    private Link _link;
    private Node _node;
    private NetworkAddr _addr;

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
