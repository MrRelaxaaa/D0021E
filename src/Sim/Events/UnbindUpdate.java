package Sim.Events;

import Sim.Entities.Link;
import Sim.Entities.Node;
import Sim.Event;
import Sim.SimEnt;

/**
 * Created by Hultstrand on 2018-03-01.
 */
public class UnbindUpdate implements Event{
    private Link _link;
    private Node _node;

    public UnbindUpdate(Link link, Node node){
        this._link = link;
        this._node = node;
    }

    public Link get_link() { return _link; }

    public Node get_node() { return _node; }

    public void entering(SimEnt locale) {

    }
}
