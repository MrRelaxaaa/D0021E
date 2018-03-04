package Sim.Events;

import Sim.Entities.Router;
import Sim.Event;
import Sim.NetworkAddr;
import Sim.SimEnt;

/**
 * Created by Hultstrand on 2018-03-02.
 * @_movingHome used to determine if the Handoff event is used to get back to Home Agent
 * @_router used to determine which router to connect to
 * @_disconnectFrom used to determine which router to disconnect from
 * @_desiredAddr the desired address on the new network
 */
public class Handoff implements Event{
    private boolean _movingHome;
    private Router _connectTo;
    private Router _disconnectFrom;
    private NetworkAddr _desiredAddr;

    public Handoff(Router connectTo, NetworkAddr desiredAddr, boolean movingHome, Router disconnectFrom){
        _connectTo = connectTo;
        _desiredAddr = desiredAddr;
        _movingHome = movingHome;
        _disconnectFrom = disconnectFrom;
    }

    public Router get_connectTo(){ return _connectTo; }

    public Router get_disconnectFrom() { return _disconnectFrom; }

    public boolean is_movingHome(){ return _movingHome; }

    public NetworkAddr get_desiredAddr() { return _desiredAddr; }

    public void entering(SimEnt locale) {

    }
}
