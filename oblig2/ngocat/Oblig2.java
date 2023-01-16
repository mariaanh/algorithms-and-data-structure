import java.io.File;
import java.util.Vector;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Map;
import java.lang.Integer;
import java.util.regex.Pattern;

public class Oblig2 {
    public static void main(String[] args) {
	Graph graph = new Graph();

	try {
	    Scanner stdin = new Scanner(System.in);
	    // take in command of the text file to read
	    Scanner infile = new Scanner (new File (args[0]));
	    
	    String firstLine = infile.nextLine();
	    // read the first line

	    infile.hasNextLine();
	    // count the first line.

	    firstLine = firstLine.trim();
	    // remove whitespace and the endpoints of firstline

	    int numberOfTasks = Integer.parseInt(firstLine);

	    // make firstLine to an Integer.

	    while (infile.hasNextLine()) {
		// while txt file has next line.

		String line = infile.nextLine();
		Vector<String> data = new Vector<String>();

		line = line.trim();
		// remove white space at the end points of the line


		if (line.equals(""))
		    // if line consists of nothing
		    /* Do nothing */
		    continue;

		String regex = "\\s";
		// regex \\s = all non words

		Pattern pattern = Pattern.compile(regex);
		String[] words = pattern.split(line);
		// split all non words

		for (String word : words) {
		    if (word.equals(""))
			continue;
		    word.trim();
		    // remove white space at end points
		    data.add(word); // add word to a vector = data
		}
		ArrayList<Integer> parents = new ArrayList<Integer>();
		// making an ArrayList called parents to put ids of parents to the task

		for(int i = 4; i < data.size(); i++){
		    parents.add(Integer.valueOf(data.get(i)));
		    // from location word[4], starts the id of parents.
		 
		}
		Task task = new Task(Integer.parseInt(data.get(0)),
				     data.get(1),
				     Integer.parseInt(data.get(2)),
				     Integer.parseInt(data.get(3)),
				     parents);
		graph.addTask(Integer.parseInt(data.get(0)),task);
		/* making a new task and put in its information */
	    }

	    infile.close();
	} catch(java.io.FileNotFoundException e){
	    // if file could not be found
	    System.err.println("can't find the given file");
	    System.exit(1);
	}
	


	/* executing the project */
	graph.makeEdges();
	// make edges
	
	System.out.println("");

	try {
	    graph.hasLoop();

	    System.out.println("there is no loop.");
	} catch (LoopException e) {
	    System.err.println("the graph has a loop, exiting with failure");
	    System.exit(1);
	}

	graph.addLatestStart();
	// add latest start time to all the tasks
	graph.addFinishTime();
	// add finish time to all the tasks
	LinkedList<Task> list = graph.criticalTasks();
	// find all the critical tasks

	System.out.println("");

	if(list.isEmpty()){
	    System.out.println("there is no critical task");
	}else{
            System.out.println("the critical tasks are:");
	    for(Task criticalTask : list){
		System.out.println(criticalTask.name);
	    }
	}
	System.out.println("");
	graph.printOutTasks(); 
	// print out all the tasks corresponding to the
	// their dependencies. 
    }
}
