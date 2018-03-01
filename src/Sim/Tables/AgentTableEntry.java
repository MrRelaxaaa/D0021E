package Sim.Tables;

import Sim.Entities.Link;
import Sim.NetworkAddr;
import Sim.SimEnt;

/**
 * Created by Hultstrand on 2018-02-28.
 */
public class AgentTableEntry {
    private NetworkAddr _id;
    private SimEnt _node;

    public AgentTableEntry(NetworkAddr id, SimEnt node){
        this._id = id;
        this._node = node;
    }

    public SimEnt get_node() { return _node; }

    public NetworkAddr get_id() { return this._id; }
}
