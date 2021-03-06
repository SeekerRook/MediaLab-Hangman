import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;



public class Game {
	/**
	   *
	   * Class that implements Hangman game Logic
	   *
	*/
	Dictionary dict ;
	private int points;
	private String Word;
	private String[] Letters;
	private List<String> Displayed = new ArrayList<String>();
	private int wguesses;
	private int guesses;
	private List<String> words;
	
	private static String fileName = "log.txt";
	/**
	   *
	   * The Constructor of the Game class
	   *
	*/
	public Game (){

		points = 0;
		wguesses = 0;
		guesses = 0;
		
	}
	/**
	   *
	   * Select Dictionary for this Game
	   *
	*/
	public void setDict(String dictid) {

		dict = new Dictionary(dictid);
		words = dict.load();
	}
	/**
	   *
	   * Get Probability that character c is in position pos
	   *
	*/
	public double probability(String c, int pos ) {

		double total = 0.0;
		double good= 0.0;

		for (String s : words) {
			total += 1.0;
			if (s.length()>pos)
			if (s.charAt(pos) == c.charAt(0)) good+= 1.0;
		}
		return good/total ;
	}
	/**
	   *
	   * return the points of the game
	   *
	*/
	public int getPoints() {

		return points;
	} 
	/**
	   *
	   * return a list of most probable character for position pos
	   *
	*/
	public List<String> Listperpos(int pos){

		List <String>res = new ArrayList <String>();
		List<myPair> pairs = new ArrayList <myPair>(); 
		for (char c = 'A'; c <='Z' ; c++) {
			double pr = probability(String.valueOf(c),pos);
			if (pr!=0.0)
			pairs.add(new myPair(pr,c));
		}
		Collections.sort(pairs,new myPair.ComPair());

		for (myPair p : pairs) {
			
			res.add(String.valueOf( p.getChar()));
		}
		return res;
	}
	/**
	   *
	   * Tselects a random word for the game
	   *
	*/
	public void setWord() {

		
		Random rand = new Random();
		int index = rand.nextInt(words.size());
		Word = words.get(index);
		Letters = Word.split("");

		for (int i = 0; i < Word.length(); i++) {
			Displayed.add("_");
		}
		List <String> words_copy = new ArrayList<String>(words);
//		System.out.println("Word  = "+ Word); //for debugging
		for (String w : words_copy) {
			if (w.length() != Word.length()) {
				words.remove(w);
			}
		}
		
	}	
	/**
	   *
	   * returns the chosen word of the game
	   *
	*/
	public String getWord() {

		return Word;
	}
	/**
	   *
	   * returns the word with _ in not found positions
	   *
	*/
	public String getDisplayed() {

		String res= "";
		for (String l : Displayed) {
			res += l;
			if (l == "_") res += " ";
					
		}
		return res;
	}
	/**
	   *
	   * checks if c is in position pos and does all the necessary updates
	   *
	*/
	public boolean guess(String c, Integer pos) {

		guesses ++;
		if (Letters[pos].charAt(0) == c.charAt(0)) {
			int index;
				index = pos;
				double prob = probability(c,index);
				if (prob < 0.25) points += 30;
				else if (prob < 0.4) points += 15;
				else if (prob < 0.6) points += 10;
				else points += 5;
				Letters[index] = "";
				Displayed.set(index,c);
				List<String> words_copy = new ArrayList<String>(words);
				for (String w : words_copy) {
					if (w.charAt(index)!= c.charAt(0)) {
						words.remove(w);
					}
				}
		return true;
		}
		else {
			wguesses += 1;
			return false;
		}
		
	}
	/**
	   *
	   * Chacks if the Game is Won (all Letters are found)
	   *
	*/
	public boolean checkwin() {

		for (String S : Letters) {

			if (S != "") return false;
		}
		return true;
	}
	/**
	   *
	   * Checks if game is Lost (more than 6 guesses)
	   *
	*/
	public boolean checkloose() {

		if (wguesses >= 6) return true;
		else return false;
	}
	/**
	   *
	   * logs current game based on current game state
	   *
	*/
	public void log() {

		log(!checkloose());
	}
	/**
	   *
	   * return number of wrong guesses
	   *
	*/
	public int getWguesses() {

		return wguesses;
	}
	/**
	   *
	   * return number of  guesses
	   *
	*/
	public int getGuesses() {

		return guesses;
	}
	/**
	   *
	   * logs game as won if state = true or lost if state = false
	   *
	*/
	public void log(boolean state) {

		String Winner = (!state)?"Computer":"Player";
		String res = "Word: "+ Word+ " \t- Winner: "+ Winner +"\t - Guesses: "+guesses;
		try {
		File mFile = new File(fileName);
		FileInputStream fis = new FileInputStream(mFile);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String result = "\n";
		String line = "";
		while( (line = br.readLine()) != null){
		 result = result + line + "\n";
		 
		}

		result = res + result;

		mFile.delete();
		FileOutputStream fos;

		fos = new FileOutputStream(mFile);
		
		fos.write(result.getBytes());
		fos.flush();
		br.close();
		fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	   *
	   * returns the outcome of the last games, equal in number of Games variable
	   *
	*/
	public static List<String> getLog(int Games) {

		List<String> l = new ArrayList<String>();
	    try (LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(fileName))) {
	         String line = lineNumberReader.readLine();
	         for(int i = 1; i <= Games; i++){
	            //int number = lineNumberReader.getLineNumber();
	            l.add(line);
	            line = lineNumberReader.readLine();
	            if(line == null){
	               break;
	            }
	         }
	      }catch (IOException e) {
		        System.out.println("An error occurred.");
		        e.printStackTrace();
		      }
		return l;
	}
	/**
	   *
	   * Create a new Dictionary
	   *
	*/
	public static void newDictionary(String id) throws InvalidRangeException, UndersizeException, UnbalancedException, IOException {

		UrltoJson url = new UrltoJson();
		String path = "https://openlibrary.org/books/"+id+".json";
		String res = url.download(path);
		Dictionary dict = new Dictionary(id);
		dict.fromString(res);
	}
	/**
	   *
	   * return number of available words
	   *
	*/
	public int getWsize() {
		
		return words.size();
	}
}
