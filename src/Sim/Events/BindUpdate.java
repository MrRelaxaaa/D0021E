package Sim.Events;

import Sim.*;
import Sim.Entities.Link;
import Sim.Entities.Node;

/**
 * Created by Hultstrand on 2018-02-23.
 * @_connectFlag says whether we are connecting to another network or returning home
 * @_toHomeAgent says whether message should go to Home Agent or not
 * @_oldAddr the nodes old network address
 * @_node the node
 * @_link the link
 */

public class BindUpdate implements Event {
    private NetworkAddr _oldAddr;
    private Node _node;
    private Link _link;
    private BindUpdateToWhom _toWhom;
    private BindUpdateConnectFlag _connectFlag;

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
