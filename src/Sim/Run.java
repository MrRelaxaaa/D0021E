package Sim;

// An example of how to build a topology and starting the simulation engine

import Sim.Entities.*;
import Sim.Events.RouterSolicitation;
import Sim.Generators.CBR;
import Sim.Generators.Gaussian;
import Sim.Generators.Poisson;

import java.io.IOException;

public class Run {
	public static void main (String [] args) throws IOException {
 		//Creates two links
 		Link link1 = new Link();
		Link link2 = new Link();
		//Link link2 = new LossyLink(1, 50, 0.2);
		
		// Create two end hosts that will be
		// communicating via the router
		Node host1 = new Node(1,1, new Sink());
		Node host2 = new Node(2,1, new Sink());

		//Connect links to hosts
		host1.setPeer(link1);
		host2.setPeer(link2);

		// Creates as router and connect
		// links to it. Information about 
		// the host connected to the other
		// side of the link is also provided
		// Note. A switch is created in same way using the Switch class
		Router routeNode = new Router(3, 3);
		Router homeAgent = new Router(3, 3);

		routeNode.set_otherRouter(homeAgent);
		homeAgent.set_otherRouter(routeNode);

		routeNode.connectInterface(0, link1, host1);
		homeAgent.connectInterface(0, link2, host2);

		//Need to set Hosts HomeAgent
		//Then send RouterSolicitation and UnbindUpdate with delays
		host2.set_homeAgent(homeAgent);
		host2.sendRouterSolicitation(routeNode, 10, new NetworkAddr(1, 10));
		host2.sendReturnToHome(homeAgent, 30);

		// Generate some traffic
		host1.StartSending(2, 1,5, 1);
		host2.StartSending(1, 1,5, 10);

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
