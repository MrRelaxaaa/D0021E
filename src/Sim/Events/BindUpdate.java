package Sim.Events;

import Sim.Entities.Link;
import Sim.Entities.Node;
import Sim.Event;
import Sim.NetworkAddr;
import Sim.SimEnt;

/**
 * Created by Hultstrand on 2018-02-23.
 */
public class BindUpdate implements Event {
    private boolean _connectFlag;
    private NetworkAddr _oldAddr;
    private Node _node;
    private Link _link;

    public BindUpdate(NetworkAddr old, Node node, boolean flag) {
        _oldAddr = old;
        _node = node;
        _connectFlag = flag;
    }

    public NetworkAddr get_oldAddr() { return _oldAddr; }

    public Node get_node() { return _node; }

    public void set_link(Link link){ _link = link; }

    public Link get_link() { return _link; }

    public boolean get_flag(){ return _connectFlag; }

    public void entering(SimEnt locale) {
    }
}
