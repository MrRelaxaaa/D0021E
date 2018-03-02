package Sim.Events;

import Sim.Entities.Router;
import Sim.Event;
import Sim.NetworkAddr;
import Sim.SimEnt;

/**
 * Created by Hultstrand on 2018-03-02.
 */
public class Handoff implements Event{
    private Router _router;
    private NetworkAddr _desiredAddr;

    public Handoff(Router router, NetworkAddr desiredAddr){
        _router = router;
        _desiredAddr = desiredAddr;
    }

    public Router get_router(){ return _router; }

    public NetworkAddr get_desiredAddr() { return _desiredAddr; }

    public void entering(SimEnt locale) {

    }
}
