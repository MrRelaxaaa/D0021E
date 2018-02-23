package Sim;

/**
 * Created by Hultstrand on 2018-02-23.
 */
public class bindUpdateEv implements Event{
    private NetworkAddr _source;
    private NetworkAddr _destination;

    bindUpdateEv (NetworkAddr from, NetworkAddr to)
    {
        _source = from;
        _destination = to;
    }

    public NetworkAddr source() {return _source;}

    public NetworkAddr destination()
    {
        return _destination;
    }

    public void entering(SimEnt locale)
    {
    }
}
