package Sim.Generators;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Hultstrand on 2018-02-06.
 * This class represents a Gaussian distribution generator.
 */
public class Gaussian {
    public ArrayList<Double> time = new ArrayList<>();
    private double mean, deviation;

    /**
     * Constructor takes a mean and deviation.
     * @param mean
     * @param deviation
     */
    public Gaussian(double mean, double deviation){
        this.mean = mean;
        this.deviation = deviation;
    }

    /**
     * Method returns the next Gaussian distributed number
     * @return
     */
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
