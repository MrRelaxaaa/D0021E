package Sim.Tables;

import Sim.NetworkAddr;
import Sim.SimEnt;

/**
 * Created by Hultstrand on 2018-02-28.
 */
public class AgentTableEntry {
    private NetworkAddr _oldId;
    private NetworkAddr _id;
    private SimEnt _node;

    public AgentTableEntry(NetworkAddr oldID, SimEnt node, NetworkAddr id){
        this._oldId = oldID;
        this._node = node;
        this._id = id;
    }
    public SimEnt get_node() { return this._node; }

    public NetworkAddr get_oldId() { return this._oldId; }

    public NetworkAddr get_id() { return this._id; }
}
