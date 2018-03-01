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
    private NetworkAddr _oldAddr;
    private NetworkAddr _newAddr;
    private Node _node;

    public BindUpdate(NetworkAddr old, NetworkAddr from, Node node) {
        _oldAddr = old;
        _newAddr = from;
        _node = node;
    }

    public NetworkAddr get_oldAddr() { return _oldAddr; }

    public Node get_node() { return _node; }

    public void entering(SimEnt locale) {
    }
}
