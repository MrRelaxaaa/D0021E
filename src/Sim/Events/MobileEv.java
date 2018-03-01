package Sim.Events;

import Sim.Event;
import Sim.Entities.Node;
import Sim.SimEnt;

/**
 * Created by Hultstrand on 2018-02-20.
 */
public class MobileEv implements Event {
    private SimEnt _link;
    private Node _source;

    public MobileEv(SimEnt _link, Node _source){
        this._link = _link;
        this._source = _source;
    }

    public SimEnt getLink(){
        return _link;
    }

    public Node source(){ return _source; }

    public void entering(SimEnt locale){}
}
