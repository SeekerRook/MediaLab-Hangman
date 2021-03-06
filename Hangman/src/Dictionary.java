import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Dictionary {
	
	private String filename;
	private List<String> Words = new ArrayList<String>();
	public Dictionary(String ID) {
		filename = "medialab/hangman_" + ID + ".txt";	

	}
	private Set<String> split (String in) {
		String inns = in.replaceAll("[^a-zA-Z]", " ");
		String [] wordsnsymbols = inns.split(" ");
		Set <String> res = new LinkedHashSet<String>();
		for (String word : wordsnsymbols) {
			String wordu = word.toUpperCase();
			if (wordu.length()>=6)res.add(wordu);
		}

		return res;
	}
	void check(Set<String> dict) throws UndersizeException,	UnbalancedException,InvalidRangeException {//TODO : Add exception handlers
		int total = 0;
		int longerthan9 = 0;
		for(String word : dict) {
			if (word.length()<6) {
				throw new InvalidRangeException();
			}
			total += 1;
			if (word.length()>= 9) {
				longerthan9 += 1;
			}
		}
		if (total < 20) {
			throw new UndersizeException();
		}
		if (longerthan9*100/total < 20)
			throw new UnbalancedException();
		

	}
	public void fromString(String Data) throws InvalidRangeException, UndersizeException,UnbalancedException, IOException {

	    Set<String> candidate = split(Data);
   		  check(candidate);
		      File myObj = new File(filename);
		      if (myObj.createNewFile()) {

		    	  FileWriter writer = new FileWriter(filename);


			    	  for(String str: candidate ) {
				    	    writer.write(str + System.lineSeparator());
				    	  }
				    	  writer.close();		    		  


		        System.out.println("File created: " + myObj.getName());
		      } else {
		    	  FileWriter writer = new FileWriter(filename);


		    	  for(String str: candidate ) {
			    	    writer.write(str + System.lineSeparator());
			    	  }
			    	  writer.close();		    		  

			    	  System.out.println("File updated: " + myObj.getName());
		      }
		    

	}
	public String getID() {
		return filename;
	}

	public void print_all() {
		try {
		 BufferedReader br = new BufferedReader(new FileReader(filename));
		 String line;
		 while ((line = br.readLine()) != null) {
		   System.out.println(line);
		 }
		 br.close();
		}
		catch(IOException e) {
			System.out.print("Error Loading Dictionary");
			e.printStackTrace();
		}
	}
	public List<String> load () {
		if (Words.isEmpty()) {
		
		try {
			 BufferedReader br = new BufferedReader(new FileReader(filename));
			 String line;

			 while ((line = br.readLine()) != null) {
				 
			   Words.add(line);
			   
			 }
			 
			 br.close();

			}
			catch(IOException e) {
				System.out.print("Error Loading Dictionary");
				e.printStackTrace();
			}
		}
		 return Words;
	}
}
