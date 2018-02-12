package Sim;

// An example of how to build a topology and starting the simulation engine

public class Run {
	public static void main (String [] args)
	{
 		//Creates two links
 		Link link1 = new Link();
		Link link2 = new Link();
		//Link link2 = new LossyLink(1, 50, 0.3);
		
		// Create two end hosts that will be
		// communicating via the router
		Node host1 = new Node(1,1);
		Node host2 = new Node(2,1);

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


		CBR c = new CBR(5);
		Gaussian g = new Gaussian(5, 1);
		Poisson p = new Poisson(4);
		// Generate some traffic
		host1.StartSending(2, 2,5, 1, c, g, p);
		//host2.StartSending(1, 1,5, 10, c, g, p);
		
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
		/*System.out.println("");
		System.out.println("Simulation outcome...");
		System.out.println("---------------------------------------");
		System.out.println("Packets sent: " + (host1.sent()+host2.sent()));
		System.out.println("Packets recieved: " + (host1.recieved()+host2.recieved()));
		System.out.printf("Total PLR: %.0f", (1.0-((host1.recieved()+host2.recieved())/(host1.sent()+host2.sent())))*100);
		System.out.println("%");
		System.out.printf("Average jitter: %.2f", (host1.avgJitter()+host2.avgJitter())/2);
		System.out.println("ms");
		System.out.println("---------------------------------------");
		*/

	}
}
