package Sim;

import java.util.Random;

/**
 * Created by Hultstrand on 2018-02-06.
 */
public class Gaussian {
    private double mean, deviation;

    public Gaussian(double mean, double deviation){
        this.mean = mean;
        this.deviation = deviation;
    }

    public double next(){
        Random rand = new Random();
        return (rand.nextGaussian() * this.deviation) + this.mean;
    }
}