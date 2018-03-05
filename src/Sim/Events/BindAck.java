package Sim.Events;

import Sim.Event;
import Sim.SimEnt;

/**
 * Created by Hultstrand on 2018-03-01.
 * This class represents a Bind Acknowledgement event.
 */
public class BindAck implements Event{
    private boolean _connectFlag;

    /**
     * Constructor takes a boolean used to determine
     * whether the event is used to connect or disconnect from
     * Home Agent.
     * @param flag
     */
    public BindAck(boolean flag){
        _connectFlag = flag;
    }

    public boolean get_flag(){ return _connectFlag; }

    public void entering(SimEnt locale) {

    }
}
