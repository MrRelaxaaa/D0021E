package Sim.Generators;

import java.util.ArrayList;

/**
 * Created by Hultstrand on 2018-02-06.
 * This class represents a Constant bit rate generator.
 */
public class CBR {
    public ArrayList<Double> time = new ArrayList<>();
    private double period;

    /**
     * Constructor of the CBR generator which takes a period p,
     * in which to send packets.
     * @param p
     */
    public CBR(double p){
        this.period = p;
    }

    /**
     * Method for getting the next period.
     * @return
     */
    public double next(){
        this.time.add(this.period);
        return this.period;
    }

    public ArrayList<Double> getTimes(){
        return time;
    }
}
