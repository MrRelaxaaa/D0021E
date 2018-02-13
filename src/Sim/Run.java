package Sim;

// An example of how to build a topology and starting the simulation engine

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Run {
	public static void main (String [] args) throws IOException {
 		//Creates two links
 		Link link1 = new Link();
		Link link2 = new Link();
		//Link link2 = new LossyLink(1, 50, 0.2);
		
		// Create two end hosts that will be
		// communicating via the router
		Sink sink1 = new Sink();
		Sink sink2 = new Sink();
		Node host1 = new Node(1,1, sink1);
		Node host2 = new Node(2,1, sink2);

		//Connect links to hosts
		host1.setPeer(link1);
		host2.setPeer(link2);

		// Creates as router and connect
		// links to it. Information about 
		// the host connected to the other
		// side of the link is also provided
		// Note. A switch is created in same way using the Switch class
		Router routeNode = new Router(2);
		routeNode.connectInterface(0, link1, host1);
		routeNode.connectInterface(1, link2, host2);


		//Traffic-Distribution Generators
		CBR c = new CBR(5);
		Gaussian g = new Gaussian(15, 2);
		Poisson p = new Poisson(10);

		// Generate some traffic
		host1.StartSending(2, 2,1000, 1, c, g, p);
		host2.StartSending(1, 1,5, 10, c, g, p);
		
		// Start the simulation engine and of we go!
		Thread t=new Thread(SimEngine.instance());
	
		t.start();
		try
		{
			t.join();
		}
		catch (Exception e)
		{
			System.out.println("The motor seems to have a problem, time for service?");
		}
		System.out.println("Average Delay Sink 1: " + sink1.getAvgDelay());
		System.out.println("Average Delay Sink 2: " + sink2.getAvgDelay());
		System.out.println("Average Jitter Sink 1: " + sink1.getAvgJitter());
		System.out.println("Average Jitter Sink 2: " + sink2.getAvgJitter());
		ArrayList<Double> times = p.getTimes();
		PrintWriter pw = new PrintWriter(new File("test.csv"));
		for(int i = 0; i < times.size(); i++){
			pw.println((times.get(i)).toString());
		}
		pw.close();
	}
}
