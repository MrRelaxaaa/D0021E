package Sim.Entities;

import java.util.ArrayList;

/**
 * Created by Hultstrand on 2018-02-13.
 * Hold statistics on the recieving side of the node
 * about the traffic on the simulated network.
 */
public class Sink {
    public ArrayList<Double> jitter = new ArrayList<>();
    public ArrayList<Double> delay = new ArrayList<>();

    public Sink(){}

    public void addJitter(Double j){
        this.jitter.add(j);
    }

    public double getAvgJitter(){
        double average = 0;
        for(int i = 0; i < jitter.size(); i++){
            average+=jitter.get(i);
        }
        return average/(jitter.size());
    }

    public void addDelay(Double d){
        this.delay.add(d);
    }

    public double getAvgDelay(){
        double average = 0;
        for(int i = 0; i < this.delay.size(); i++){
            average+=this.delay.get(i);
        }
        return average;
    }
}
