package Sim.Events;

import Sim.Event;
import Sim.SimEnt;

/**
 * Created by Hultstrand on 2018-03-01.
 */
public class BindAck implements Event{
    private boolean _connectFlag;

    public BindAck(boolean flag){
        _connectFlag = flag;
    }

    public boolean get_flag(){ return _connectFlag; }

    public void entering(SimEnt locale) {

    }
}
