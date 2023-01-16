import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;
import java.util.PriorityQueue;


public class Network{
    HashMap<Integer, Machine> network = new HashMap<Integer,Machine>();

    public void addMachine(Machine data){
	// putting data id as key, and data as value
	// in the hashmap network.
	network.put(data.id, data);
    }

    /* this method takes each line of file config.txt and make
     a connection, thereafter adding the connection to the corresponding 
     machine or data */
    public void makeConnections(LinkedList<Vector<String>> listOfConnections){
	for(Vector<String> vector: listOfConnections){
	    // make a connection of a line in config.txt
	    
	    Machine machine1 = network.get(Integer.parseInt(vector.get(0)));
	    Machine machine2 = network.get(Integer.parseInt(vector.get(1)));
	    
	    Connection connection = new Connection(machine1.id, machine2.id,
						   Float.parseFloat(vector.get(2)),
						   Float.parseFloat(vector.get(3)));

	    machine1.addConnection(machine2.id, connection);
	    machine2.addConnection(machine1.id, connection);
	    // each connection is added to the corresponding machines
	}
    }

    /* this method reset the stored info in each Machine
     made during shortestPath method. 
    It's default modus is all machines are set to unvisisted
    and the weight is set to a max value */
    public void resetStoredInfoSP(){
	for(Machine machine: network.values()){
	    machine.resetStoredSPInfo();
	}
    }

    /* this method does the djikstra's 
       shortest path algorithm */
    /* allocating each machine the shortest path from the initial node 
       or machine. */
    public void shortestPath(Integer machineId, String searchPreference){
	PriorityQueue<Machine> queue = new PriorityQueue();

	this.resetStoredInfoSP();

	Machine machine = network.get(machineId);
	machine.unvisited = false;
	machine.weight = 0;
	machine.cost = 0;
	machine.time = 0;
	queue.add(machine);

	while(queue.isEmpty() == false){
	    Machine current = queue.poll();
	    for(Integer id: current.listOfAllConnections.keySet()){
		Connection connection = current.listOfAllConnections.get(id);
		Machine connectedMachine = network.get(id);
		if(connectedMachine.unvisited == false){
		    continue;
		}
		if(searchPreference.equals("C") || searchPreference.equals("B")){
		    if(connectedMachine.weight > connection.cost + current.cost){
			
			connectedMachine.backPointer = current.id;
			connectedMachine.weight = connection.cost + current.cost;
			connectedMachine.cost = connection.cost + current.cost;
			connectedMachine.time = connection.time + current.time;
		    }
		}else if(searchPreference.equals("T")){
		    if(connectedMachine.weight > connection.time + current.time){
			
			connectedMachine.backPointer = current.id;
			connectedMachine.weight = connection.time + current.time;
			connectedMachine.time = connection.time + current.time;
			connectedMachine.cost = connection.cost + current.cost;
		    }
		}
		if(queue.contains(connectedMachine) == false){
		    queue.add(connectedMachine);
		}
	    }
	    current.unvisited = false;
	}

	
    }
    /* Iterates over all machines to find the requested element,
       the machines which has the element are put into a queue 
       with priority corresponding to its weight, the weight is 
       either based on cost or time or both corresponding to the
       request.
    */
    public void findElement(Integer element, String ownershipPreference, PriorityQueue<Machine> queue){

	for(Machine machine: network.values()){
	    if( ownershipPreference.equals("O")){
		if(machine.containsElementO(element) == true){
		    queue.add(machine);
		}
	    }
	    if( ownershipPreference.equals("A")){
		if(machine.containsElementO(element) == true ||
		   machine.containsElementC(element) == true){
		    queue.add(machine);
		}
	    }
	}
    }


    /* Each machine is placed with a backpointer to its earlier
       machine. This method use the backpointer to make a String
       route, which shows the path to the initial machine. 
     */
    public void makeRoute(Integer id){
	Machine machine = network.get(id);
	String route;
	int backPointer;
	backPointer = machine.backPointer; 
	route = String.valueOf(machine.id);
	while(backPointer != 0){
	    route = route + "->" + String.valueOf(backPointer);
	    machine.endPointer = backPointer;
	    backPointer = network.get(backPointer).backPointer;
	}
	machine.route = route;

    }
 

}	   