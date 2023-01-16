import java.util.Scanner;
import java.io.File;
import java.util.regex.Pattern;
import java.util.Vector;
import java.util.LinkedList;

public class Readfile{
    String textfile;
    public Readfile(String txt){
	this.textfile = txt;
    }

    public LinkedList<Vector<String>> read(){
	LinkedList<Vector<String>> list = new LinkedList<Vector<String>>();
	try{
	    Scanner infile = new Scanner(new File(textfile));
	    // read file txt
	    
	    while(infile.hasNextLine()){
		String line = infile.nextLine();
		line = line.trim();
		if(line.equals("")){
		    // Do nothing
		    continue;
		}
		String regex = "\\s";
		// regex "\\s" = all non words
		Pattern pattern = Pattern.compile(regex);
		String[] words = pattern.split(line);
		// split all non words
		
		Vector<String> info = new Vector<String>();

		for(String word: words){
		    if(word.equals(""))
			continue;
		    word.trim();
		    info.add(word);
		}
		
		list.add(info);

	    } 
	} catch(java.io.FileNotFoundException e){
		// if file could not be found
		System.err.println("can't find the given file");
		System.exit(1);
	}
	return list;
    }
}