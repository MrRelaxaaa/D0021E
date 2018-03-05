package Sim.Events;

import Sim.Entities.Router;
import Sim.Event;
import Sim.NetworkAddr;
import Sim.SimEnt;

/**
 * Created by Hultstrand on 2018-03-02.
 * This class represents a handoff event which will be triggered
 * when a node wishes to make a handoff to another network.
 */
public class Handoff implements Event{
    private boolean _movingHome;
    private Router _connectTo;
    private Router _disconnectFrom;
    private NetworkAddr _desiredAddr;

    /**
     * Constructor takes the router in which the node wishes to connect to,
     * the desired address on the new network, a boolean to determine whether it is
     * moving back to home network or not and lastly the router to disconnect from.
     * @param connectTo
     * @param desiredAddr
     * @param movingHome
     * @param disconnectFrom
     */
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
