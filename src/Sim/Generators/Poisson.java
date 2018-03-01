package Sim.Generators;

import java.util.ArrayList;

/**
 * Created by Hultstrand on 2018-02-06.
 */
public class Poisson {
    public ArrayList<Double> time = new ArrayList<>();
    private double lambda;

    public Poisson(double lambda){
        this.lambda = lambda;
    }

    public double next(){
        int k = 0;
        double p = 1.0, L = Math.exp(-lambda);
        do{
            k++;
            p *= Math.random();
        }while(p >= L);
        double poisson = k-1;
        this.time.add(poisson);
        return poisson;
    }

    public ArrayList<Double> getTimes(){
        return time;
    }
}
