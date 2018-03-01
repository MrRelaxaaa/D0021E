package Sim.Events;

import Sim.Event;
import Sim.NetworkAddr;
import Sim.SimEnt;

/**
 * Created by Hultstrand on 2018-02-23.
 */
public class RouterInterfaceAck implements Event {
   NetworkAddr _newNetworkAddr;

   public RouterInterfaceAck(NetworkAddr _newNetworkAddr){
       this._newNetworkAddr = _newNetworkAddr;
   }

   public NetworkAddr getNewAddr(){
       return _newNetworkAddr;
   }

   public void entering(SimEnt locale) {

    }
}
