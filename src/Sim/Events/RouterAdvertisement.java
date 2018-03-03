package Sim.Events;

import Sim.Event;
import Sim.NetworkAddr;
import Sim.SimEnt;

/**
 * Created by Hultstrand on 2018-02-27.
 * @_addr the supplied network address that the node will receive
 */
public class RouterAdvertisement implements Event {
    private NetworkAddr _addr;

    public RouterAdvertisement(NetworkAddr addr){ this._addr = addr; }

    public NetworkAddr get_addr() { return _addr; }

    public void entering(SimEnt locale) {

    }
}
