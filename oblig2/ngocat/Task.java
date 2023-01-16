import java.util.ArrayList;


public class Task implements Comparable<Task>{
    public int id;
    public String name;
    public int time;
    public int manpower;
    public ArrayList<Task>  children = new ArrayList<Task>();
    public ArrayList<Task> parents = new ArrayList<Task>();
    public ArrayList<Integer> parentsInfo;
    public int finishTime ;
    public int earliestStart;
    public int latestStart;
    public int indegree;

    // contructor to take in datas from the txt file
    public Task(int id,
		String name,
		int time,
		int manpower,
		ArrayList<Integer> parentsInfo){ 
	/* the information of the dependencies of the task is taken in as
	   an arraylist of integers */
	this.id = id;
	this.name = name;
	this.time = time;
	this.manpower = manpower;
	this.parentsInfo = parentsInfo;

    }
   
    public void addParent(Task parent){
	parents.add(parent);
	indegree += 1;
    }

    public void addChild(Task child){
	children.add(child);
    }

    // latest start is defined as the latest time 
    // the task should be starting in order
    // to not delay the entire process.
    public void addLatestStart(){

	/* find the child with least earliest start time */
	if(!children.isEmpty()){
	    Task tmp = children.get(0);
	    for(int i = 1; i < children.size(); i++){
		if(tmp.earliestStart > children.get(i).earliestStart){
		    tmp = children.get(i);
		}else{
		    continue;
		}
	    }
	    this.latestStart = tmp.earliestStart - time;

	    // latest start time is the time of the child with 
	    // least start time subtracts the task's time.
	} 
    }
    
    // finish time is the time when the task is finished.
    public void addFinishTime(){
	this.finishTime = time + earliestStart;
    }

    // comparing it self and other by earliest start time
    public int compareTo(Task other){
	if(this.earliestStart > other.earliestStart){
	    return 1; 
	}
	else if(this.earliestStart < other.earliestStart){
	    return -1;
	}else{ // this.latestStart == other.latestStart 
	    if(this.finishTime > other.finishTime){
		return 1;
	    }else if(this.finishTime < other.finishTime){
		return -1;
	    }else{
		return 0;
	    }
	}
    }
}
