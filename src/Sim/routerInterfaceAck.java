package Sim;

/**
 * Created by Hultstrand on 2018-02-23.
 */
public class routerInterfaceAck implements Event{
   NetworkAddr _newNetworkAddr;

   routerInterfaceAck(NetworkAddr _newNetworkAddr){
       this._newNetworkAddr = _newNetworkAddr;
   }

   public NetworkAddr getNewAddr(){
       return _newNetworkAddr;
   }

   public void entering(SimEnt locale) {

    }
}
