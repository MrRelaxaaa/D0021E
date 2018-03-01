package Sim.Entities;

// This class implements a link without any loss, jitter or delay

//@minDelay lower bound for random delay
//@maxDelay upper bound for random delay

import Sim.Event;
import Sim.Events.Message;
import Sim.SimEnt;

import java.util.Random;

public class LossyLink extends Link{
    private SimEnt _connectorA=null;
    private SimEnt _connectorB=null;
    private int _now=0;
    private int minDelay, maxDelay = 0;
    private double packetLossRate;
    private double dropChance;

    public LossyLink(int min, int max, double dropChance)
    {
        super();
        this.minDelay = min;
        this.maxDelay = max;
        this.dropChance = dropChance;
    }

    // Connects the link to some simulation entity like
    // a node, switch, router etc.

    public void setConnector(SimEnt connectTo)
    {
        if (_connectorA == null)
            _connectorA=connectTo;
        else
            _connectorB=connectTo;
    }

    // Called when a message enters the link

    public void recv(SimEnt src, Event ev)
    {
        if (ev instanceof Message) {
            //Generate a random delay for each
            //package that enters the link. See getDelay().
            this.packetLossRate = getPacketLoss();
            int delay = getDelay();
            if(this.packetLossRate < this.dropChance){
                System.out.println("Packet with sequence number " + ((Message) ev).seq() + " was dropped...");
            }else{
                System.out.println("Link recv msg, passes it through");
                if (src == _connectorA){
                    send(_connectorB, ev, delay);
                }
                else {
                    send(_connectorA, ev, delay);
                }
            }

        }
    }

    // Generates a random integer, used to
    // simulate a random delay for each packet sent.
    public int getDelay(){
        Random rand = new Random();
        int delay = rand.nextInt((this.maxDelay-this.minDelay) + 1) + this.minDelay;
        return delay;
    }


    // Generates a random integer, used to
    // simulate a random packet loss rate for each packet sent.
    public double getPacketLoss(){
        Random rand = new Random();
        double packetDropRate = rand.nextDouble();
        return packetDropRate;
    }
}