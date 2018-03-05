package Sim.Tables;

import Sim.NetworkAddr;
import Sim.SimEnt;

/**
 * Created by Hultstrand on 2018-02-28.
 * This class represents a table entry for the Home Agent.
 */
public class AgentTableEntry {
    private NetworkAddr _id;
    private SimEnt _node;

    /**
     * Constructor for an entry in the Home Agent table,
     * takes a NetworkAddr of the node and the node itself.
     * @param id
     * @param node
     */
    public AgentTableEntry(NetworkAddr id, SimEnt node){
        this._id = id;
        this._node = node;
    }
    public SimEnt get_node() { return this._node; }

    public NetworkAddr get_id() { return this._id; }
}
