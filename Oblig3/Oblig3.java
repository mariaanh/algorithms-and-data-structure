import java.util.Scanner;
import java.io.File;
import java.util.regex.Pattern;
import java.util.Vector;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class Oblig3 {
    public static void main(String[] args) {
	Scanner stdin = new Scanner(System.in);
	Readfile file1 = new Readfile(args[0]); // read file config.txt
	Readfile file2 = new Readfile(args[1]); // read file data.txt
      
	LinkedList<Vector<String>> list1 = file1.read(); 
	// return a linkedlist of vectors for each line in config.txt
	LinkedList<Vector<String>> list2 = file2.read(); 
	// return a linkedlist of vectors for each line in data.txt
	Network network = new Network();

	for(Vector<String> line: list2){
	    // for each line in data.txt
	    ArrayList<Integer> dataElements = new ArrayList<Integer>();
	    for(int i = 2; i < line.size(); i++){
		if(line.get(i)!= null){
		    // put the data elements in an arraylist
		    dataElements.add(Integer.parseInt(line.get(i)));
		}
	    }
	    // make a new data, add its id, capacity and dataelements
	    Machine data = new Machine(Integer.parseInt(line.get(0)),Integer.parseInt(line.get(1)), dataElements);
	    network.addMachine(data);
	}
	// make all the connections
	network.makeConnections(list1);
	returnRequest(network);

    }
    /* this method ask for the request and store the entered arguments
       as string in a vector.
     */
    public static void askRequest(Vector<String> requestedInfo){
	System.out.println("");
	System.out.println("give the request in the form; \n"
			   + "machine_id : search_preference : ownership_preference : element element \n" +
			   "Notice the space between each arguments." );
	System.out.println("");
	// take in the request as a line
	Scanner request = new Scanner(System.in);
	String line = request.nextLine().trim();
	// remove white space at the end points of the line
	String regex = "\\W";
	// this regex splits all the non word
	Pattern pattern = Pattern.compile(regex);
	String[] words = pattern.split(line);
	for(String word : words){
	    word.trim();
	    // if word equals == empty, skip
	    if(word.equals("")){
		continue;
	    }
	    requestedInfo.add(word);
	}

    }

    /* this method return search result for the given request
     */
    public static void returnRequest(Network network){
	Vector<String> requestedInfo = new Vector<String>();
	askRequest(requestedInfo);
	/* ask for the request and store the entered
	   search preference in requestedinfo
	 */
	int machineId = Integer.parseInt(requestedInfo.get(0));
	// convert string to integer
	String searchPreference = requestedInfo.get(1);
	String ownershipPreference = requestedInfo.get(2);
	float totalCost = 0;
	float totalTime = 0;
	for(int i = 3; i < requestedInfo.size(); i++){
	    // for each element requested;

	    int element = Integer.parseInt(requestedInfo.get(i));
	    network.shortestPath(machineId,searchPreference);
	    /* find the shortest path for all machines
	       from the initial machine = machineId
	     */
	    PriorityQueue<Machine> queue = new PriorityQueue<Machine>();
	    /* put the machines in a queue, this queue has priority
	       according the request
	     */
	    network.findElement(element, ownershipPreference,
				queue);
	    if(queue.isEmpty()== true){
		System.out.println(element+" : does not exist");
		// if queue is empty the element does not exist
	    }else{
		// machine which has the current element
	
		Machine machineE = queue.poll();
	       
		network.makeRoute(machineE.id);
		// route is a string which shows the shortest path
		if(machineE.endPointer != machineId){
		    /* there a possibility of the case where
		       the machine which has the element can not be reach
		       from the initial machine. If this occurs then,
		       there will be a mismatch in endpointer and machineId.
		     */
		    System.out.println(element+" : route does not exist for this element");
		} else{ 
		    /* there is a path to the requested element
		     */
		    System.out.println(element + " : " + machineE.route + " " +
				       "(t = " + machineE.time +
				       " c = " + machineE.cost + ")" );
		    totalCost += machineE.cost;
		    totalTime += machineE.time;
		    if(network.network.get(machineId).containsElementO(element) == false
		       && network.network.get(machineId).containsElementC(element) == false){
			// if the requested element comes from other machines
			// then we store it for next request.
			network.network.get(machineId).addCopyElement(element);
		    }
		}
	    }   
	}
	System.out.println("");
	System.out.println("Totall cost: " + totalCost + "  " +
			   "Totall time; " + totalTime );
	System.out.println(" ");
	System.out.println("Do you want to make another request? \n" 
			   + "If yes enter y, if exit enter n");

	Scanner enter = new Scanner(System.in);
	// ask for another request
	while(enter.hasNextLine()){
	    String line = enter.nextLine().trim();
	    switch (line.charAt(0)){
	    case 'n':
		System.exit(0);
		break;
	    case 'y':
		returnRequest(network);
		break;
	    }
	}

    }

    
    
}