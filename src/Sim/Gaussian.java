package Sim;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Hultstrand on 2018-02-06.
 */
public class Gaussian {
    public ArrayList<Double> time = new ArrayList<>();
    private double mean, deviation;

    public Gaussian(double mean, double deviation){
        this.mean = mean;
        this.deviation = deviation;
    }

    public double next(){
        Random rand = new Random();
        double nextGauss = (rand.nextGaussian() * this.deviation) + this.mean;
        this.time.add(nextGauss);
        return nextGauss;
    }

    public ArrayList<Double> getTimes(){
        return time;
    }
}