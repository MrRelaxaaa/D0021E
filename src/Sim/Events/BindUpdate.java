package Sim.Events;

import Sim.*;
import Sim.Entities.Link;
import Sim.Entities.Node;

/**
 * Created by Hultstrand on 2018-02-23.
 * This class represents a Bind Update event.
 */

public class BindUpdate implements Event {
    private NetworkAddr _oldAddr;
    private Node _node;
    private Link _link;
    private BindUpdateToWhom _toWhom;
    private BindUpdateConnectFlag _connectFlag;

    /**
     * Constructor takes the old address of the sender, the sender/node and
     * to whom the bind update should be sent to.
     * @param old
     * @param node
     * @param bindUpdateToWhom
     */
    public BindUpdate(NetworkAddr old, Node node, BindUpdateToWhom bindUpdateToWhom) {
        _oldAddr = old;
        _node = node;
        _toWhom = bindUpdateToWhom;
    }

    public NetworkAddr get_oldAddr() { return _oldAddr; }

    public Node get_node() { return _node; }

    public void set_link(Link link){ _link = link; }

    public Link get_link() { return _link; }

    public BindUpdateConnectFlag connectTo(){ return _connectFlag; }

    public void set_connectFlag(BindUpdateConnectFlag connectFlag) { _connectFlag = connectFlag; }

    public BindUpdateToWhom get_toWhom(){ return _toWhom; }

    public void set_toWhom(BindUpdateToWhom toWhom) { _toWhom = toWhom; }

    public void entering(SimEnt locale) {
    }
}
