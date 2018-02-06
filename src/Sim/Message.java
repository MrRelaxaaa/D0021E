package Sim;

// This class implements an event that send a Message, currently the only
// fields in the message are who the sender is, the destination and a sequence 
// number

// @start_time used to timestamp whenever a message event is created,
// needed in order to establish what the resulting delay is.
public class Message implements Event{
	private NetworkAddr _source;
	private NetworkAddr _destination;
	private int _seq=0;
	double start_time;
	
	Message (NetworkAddr from, NetworkAddr to, int seq)
	{
		_source = from;
		_destination = to;
		_seq=seq;
		this.start_time = SimEngine.getTime();
	}

	public NetworkAddr source()
	{
		return _source; 
	}
	
	public NetworkAddr destination()
	{
		return _destination; 
	}
	
	public int seq()
	{
		return _seq; 
	}

	public void entering(SimEnt locale)
	{
	}
}
	
