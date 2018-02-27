package Sim;

import java.util.ArrayList;

/**
 * Created by Hultstrand on 2018-02-06.
 */
public class CBR {
    public ArrayList<Double> time = new ArrayList<>();
    private double period;

    public CBR(double p){
        this.period = p;
    }

    public double next(){
        this.time.add(this.period);
        return this.period;
    }

    public ArrayList<Double> getTimes(){
        return time;
    }
}
