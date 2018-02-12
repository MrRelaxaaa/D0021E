package Sim;

/**
 * Created by Hultstrand on 2018-02-06.
 */
public class Poisson {
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
        return k-1;
    }
}
