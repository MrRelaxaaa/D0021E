package Sim;

/**
 * Created by Hultstrand on 2018-02-20.
 */
public class MobileEvent implements Event{
    private SimEnt _link;
    private Node _source;

    public MobileEvent(SimEnt _link, Node _source){
        this._link = _link;
        this._source = _source;
    }

    public SimEnt getLink(){
        return _link;
    }

    public Node source(){ return _source; }

    public void entering(SimEnt locale){}
}
