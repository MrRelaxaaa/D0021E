package Sim.Tables;

import Sim.NetworkAddr;

/**
 * Created by Hultstrand on 2018-02-28.
 */
public class AgentTableEntry {
    private NetworkAddr _homeAddr;
    private NetworkAddr _careOfAddr;

    public AgentTableEntry(NetworkAddr _homeAddr, NetworkAddr _careOfAddr){
        this._homeAddr = _homeAddr;
        this._careOfAddr = _careOfAddr;
    }

    public void set_homeAddr(NetworkAddr _homeAddr){
        this._homeAddr = _homeAddr;
    }

    public void set_careOfAddr(NetworkAddr _careOfAddr){
        this._careOfAddr = _careOfAddr;
    }

    public NetworkAddr get_homeAddr(){
        return this._homeAddr;
    }

    public NetworkAddr get_careOfAddr(){
        return this._careOfAddr;
    }
}
