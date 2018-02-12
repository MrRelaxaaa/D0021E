package Sim;

/**
 * Created by Hultstrand on 2018-02-06.
 */
public class CBR {
    private double period;

    public CBR(double p){
        this.period = p;
    }

    public double next(){
        return this.period;
    }
}
