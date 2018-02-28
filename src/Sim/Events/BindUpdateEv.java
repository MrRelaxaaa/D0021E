package Sim.Events;

import Sim.Event;
import Sim.NetworkAddr;
import Sim.SimEnt;

/**
 * Created by Hultstrand on 2018-02-23.
 */
public class BindUpdateEv implements Event {
    private NetworkAddr _old;
    private NetworkAddr _source;

    public BindUpdateEv(NetworkAddr old, NetworkAddr from) {
        _old = old;
        _source = from;
    }

    public NetworkAddr old() { return _old; }

    public NetworkAddr source() { return _source; }

    public void entering(SimEnt locale) {
    }
}
