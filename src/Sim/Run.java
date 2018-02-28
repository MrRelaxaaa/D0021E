package Sim;

// An example of how to build a topology and starting the simulation engine

import Sim.Entities.*;
import Sim.Generators.CBR;
import Sim.Generators.Gaussian;
import Sim.Generators.Poisson;

import java.io.IOException;

public class Run {
	public static void main (String [] args) throws IOException {
 		//Creates two links
 		Link link1 = new Link();
		Link link2 = new Link();
		Link link3 = new Link();
		//Link link2 = new LossyLink(1, 50, 0.2);
		
		// Create two end hosts that will be
		// communicating via the router
		Node host1 = new Node(1,1, new Sink());
		Node host2 = new Node(2,2, new Sink());

		//Connect links to hosts
		host1.setPeer(link1);
		host2.setPeer(link2);

		HomeAgent ha = new HomeAgent(2, 1, new Sink());
		ha.connectNode(link2);
		ha.connectRouter(link3);


		//dd
		// Creates as router and connect
		// links to it. Information about 
		// the host connected to the other
		// side of the link is also provided
		// Note. A switch is created in same way using the Switch class
		Router routeNode = new Router(3);
		routeNode.connectInterface(0, link1, host1);
		routeNode.connectInterface(1, link3, ha);


		//Traffic-Distribution Generators
		CBR c = new CBR(5);
		Gaussian g = new Gaussian(25, 7);
		Poisson p = new Poisson(10);

		// Generate some traffic
		host1.StartSending(2, 1,5, 1, c, g, p);
		host2.StartSending(1, 1,5, 10, c, g, p);

		//MobileEv mob1 = new MobileEv(link2, host2);
		//host2.send(link2, mob1, 0);

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
		/*System.out.println("Average Delay Sink 1: " + sink1.getAvgDelay());
		System.out.println("Average Delay Sink 2: " + sink2.getAvgDelay());
		System.out.println("Average Jitter Sink 1: " + sink1.getAvgJitter());
		System.out.println("Average Jitter Sink 2: " + sink2.getAvgJitter());
		ArrayList<Double> times = p.getTimes();
		PrintWriter pw = new PrintWriter(new File("test.csv"));
		for(int i = 0; i < times.size(); i++){
			pw.println((times.get(i)).toString());
		}
		pw.close();*/
	}
}
