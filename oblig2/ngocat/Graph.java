import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Collections;
import java.lang.Math;
import java.util.TreeMap;
import java.util.Map;


public class Graph{

    public HashMap<Integer,Task> tasks = new HashMap<Integer,Task>();

    public void addTask(int id, Task task){
	tasks.put(id, task);
    }

    // making connections between tasks.
    public void makeEdges(){
	for(Task child: tasks.values()){
	    ArrayList<Integer> parents = child.parentsInfo;
	    // parentsInfo is an ArrayList containing ids of
	    // parents.
	    for(int i : parents){
		// for each id
		if( i != 0){
		    // get the parent from tasks and add it as parent
		    Task parent = tasks.get(i);
		    child.addParent(parent);
		    // add task as child to the parent
		    parent.addChild(child);
		}
	    }
	}
    }
    
    public boolean hasLoop() throws LoopException{
	LinkedList<Task> list = new LinkedList<Task>();
	Task v;
	int count = 0;
	
	// finding tasks where indegree is zero
	// if indegree is zero, add to the queue
	// and give task's earliest start time = 0.
	for(Task task: tasks.values()){
	    ArrayList<Task> parents = task.parents;
	    if(!parents.isEmpty()){
		continue;
	    }else{
		task.earliestStart = 0;
		list.add(task);
	    }
	}

	// while the queue is not empty,
	/* 
	   1. take out the first in queue call this task v.
	   2. get v's children- arrayList.
	   3. add 1 to count.
	   4. if children-arryList is not zero.
	   5. reduce children's indegree by 1.
	   6. if any child has indegree zero,
	   give the child's earliest start time = v.time + v.earliestStart,
	   add put the child in the queue.
	   7. repeat from 1.
	*/
	while(!list.isEmpty()){
	    v = list.removeFirst();
	    ArrayList<Task> children = v.children;
	    count++;
	    if(!children.isEmpty()){
		for(Task temp: children){
		    temp.indegree -= 1;
		    if(temp.indegree == 0){
			temp.earliestStart = v.time + v.earliestStart;
			list.add(temp);
		    }
		}
	    }
	}

	// if count is less than tasks.size. this means the graph has a loop.
	if(count< tasks.size())
	    throw new LoopException("the graph has a loop");

	return false;
    }
    
    // give latest start time to all the tasks.
    public void addLatestStart(){
	for(Task task: tasks.values()){
	    task.addLatestStart();
	}
    }

    // give finish time to all the tasks
    public void addFinishTime(){
	for(Task task: tasks.values()){
	    task.addFinishTime();
	}
    }
    
    public LinkedList<Task> criticalTasks(){
	// iterates over the tasks to find those tasks with 
	// earliest start time == latest start time.
	// put these in a linkedList and return this list.
	LinkedList<Task> criticalTasks = new LinkedList<Task>();
	for(Task task: tasks.values()){
	    if(task.earliestStart == task.latestStart){
		criticalTasks.add(task);
	    }else{ 
		continue;
	    }
	}
	return criticalTasks;
    }

    public void printOutTasks(){
	System.out.println("Executing Tasks........ ");
	System.out.println("");
	TreeMap<Integer,ContainerForPrintTasks> sortedMap = new TreeMap<Integer,ContainerForPrintTasks>();
	// making a TreeMap with the property of sorting its values after their keys.
	// in this case the keys are of type integer.
	for(Task task : tasks.values()){
	    int earliestStart = task.earliestStart;
	    int finishTime = task.finishTime;
	    
	    if(sortedMap.containsKey(earliestStart) == false){
		// if the task's earliestStart time does exist in the sortedMap
		// make a container, add itself to the container, and
		// put the container into the sortedMap with its earliest time
		// as key.
		ContainerForPrintTasks c = new ContainerForPrintTasks();
		c.addStartingTask(task);
		sortedMap.put(earliestStart,c);
	    }else{// if the earliest start time exists in the sortedMap
		// get the container with key = earliest start time.
		// add task to the container in the linkedlist of Starting Tasks
		sortedMap.get(earliestStart).addStartingTask(task);
	    }
	    if(sortedMap.containsKey(finishTime) == false){
		// if finish time does not exist as key in the sortedMap
		ContainerForPrintTasks f = new ContainerForPrintTasks();
		f.addFinishedTask(task); 
		// add task into the container 
		// as one of the finished task
		sortedMap.put(finishTime,f);
		// put the container into the sortedMap with finishtime as key
	    }else{
		sortedMap.get(finishTime).addFinishedTask(task);
		// get the container with finishtime as key and add task to the container
		// as one of the finished task.
	    }
	}
	int currentStaff = 0;
	int time = 0;
	while(!sortedMap.isEmpty()){
	    // get the first pair in the sortedMap, sorted by the keys(Integer)
	    Map.Entry<Integer,ContainerForPrintTasks> first = sortedMap.pollFirstEntry();
	    ContainerForPrintTasks tmp = first.getValue();
	    // get the container of the pair.
	    System.out.println("Time:"+ tmp.time);
	    // print out the time of the container
	    time = tmp.time;
	    // update time = container's time.
	    tmp.print();
	    // print out the tasks in the container
	    currentStaff += tmp.manpower;
	    // update current staff by adding the total staff in the container.
	    System.out.println("Current staff:"+currentStaff);
	    System.out.println("");
	}
	System.out.println("The shortest executing time is "+time);
	
    }

}

class LoopException extends Exception {
    LoopException(){
	super();
    }

    LoopException(String e){
	super(e);
    }
}

class ContainerForPrintTasks{

    /* this container contains a LinkedList for starting tasks and
       a LinkedList for finished tasks. 
       It also has a method for printing out all the tasks.
    
     */

    int manpower;
    int time;
    LinkedList<Task> startingTasks = new LinkedList<Task>();
    LinkedList<Task> finishedTasks = new LinkedList<Task>();

    public void addStartingTask(Task task){
	startingTasks.add(task);
	manpower += task.manpower;
	this.time = task.earliestStart;
    }
    public void addFinishedTask(Task task){
	finishedTasks.add(task);
	manpower -= task.manpower;
	this.time = task.finishTime;
    }

    public void print(){
	if(!finishedTasks.isEmpty()){
	    for(Task f: finishedTasks){
		System.out.println("Finished:"+ f.name);
	    }
	}
	if(!startingTasks.isEmpty()){
	    for(Task s: startingTasks){
		System.out.println("Starting:"+ s.name);
	    }
	}
    }
}
