package Sim.Events;

import Sim.Event;
import Sim.NetworkAddr;
import Sim.SimEnt;

/**
 * Created by Hultstrand on 2018-02-27.
 * This class represents a Router Advertisement event which in MIPv6
 * will be sent to any node that has sent a Router Solicitation informing the node
 * that this router exists.
 */
public class RouterAdvertisement implements Event {
    private NetworkAddr _addr;

    /**
     * Constructor taking the NetworkAddr that the node has requested to have.
     * @param addr
     */
    public RouterAdvertisement(NetworkAddr addr){ this._addr = addr; }

    public NetworkAddr get_addr() { return _addr; }

    public void entering(SimEnt locale) {

    }
}
