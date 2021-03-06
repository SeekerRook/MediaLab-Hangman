


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GUI extends Application {
	int score;
	int image =0;
	String dictid = "";
	Game game;
	Label wrdlabel ; 
	List <Button> Keys =  new ArrayList <Button>();
	Label prob = new Label();	
	@Override
	public void start(Stage primaryStage) {
		int AppWidth = 710;
		int AppHeight = 400;
		String placeholder = "-";


		
		primaryStage.setTitle("MediaLab Hangman");
		primaryStage.setResizable(false);
		AnchorPane pane = new AnchorPane();

		final ImageView img = new ImageView(new Image("img/"+String.valueOf(image)+".png"));
		AnchorPane.setLeftAnchor(img,5.0);

		AnchorPane labels = new AnchorPane();
		final Label wlabel = new Label("Words : " + placeholder);
		wlabel.setPadding(new Insets(10));
		AnchorPane.setLeftAnchor(wlabel, 10.0);

		final Label plabel = new Label("Points : " + placeholder);
		plabel.setPadding(new Insets(10));
		AnchorPane.setLeftAnchor(plabel, AppWidth/2 - 10.0);
		final Label glabel = new Label("Correct Guesses : " + placeholder);
		glabel.setPadding(new Insets(10));
		AnchorPane.setRightAnchor(glabel, 20.0);
		labels.getChildren().addAll(wlabel,plabel,glabel);
		wrdlabel = new Label(" ");
		wrdlabel.setFont(new Font("Arial",20));
		AnchorPane.setBottomAnchor(wrdlabel, 10.0);
		
		
		HBox Keyboard = new HBox();
		Label l1 = new Label("  Character : ");
		final TextField letter = new TextField();
		letter.maxWidth(10);
		Label l2 = new Label("  Postion : ");
		final TextField position = new TextField();
		position.maxWidth(10);
		final Button Guess = new Button ("GUESS");
		Keyboard.getChildren().addAll(l1,letter,l2,position,Guess);

		
		EventHandler<ActionEvent> guessc = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
			try {
				if(!letter.getText().matches("[A-Za-z]")) throw new Exception();
				if (!guess(letter.getText().toUpperCase(),Integer.parseInt(position.getText())-1)) {
					image++;
					img.setImage(new Image("img/"+String.valueOf(image)+".png"));
					wlabel.setText("Words:" + String.valueOf(game.getWsize()));
					plabel.setText("Points:" + score);
					glabel.setText("Correct Guesses: "+100*(game.getGuesses() - game.getWguesses())/Math.max(game.getGuesses(),1)+"%");			
				
				}
				else {
					wlabel.setText("Words:" + String.valueOf(game.getWsize()));
					plabel.setText("Points:" + score);
					glabel.setText("Correct Guesses: "+100*(game.getGuesses() - game.getWguesses())/Math.max(game.getGuesses(),1)+"%");
					wrdlabel.setText(game.getDisplayed());
				}
			}catch (Exception ex) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("ERROR");
				alert.setContentText("Invalid Guess");
				alert.showAndWait();

			}
				checkstatus();
				
				

			}
		};
		Guess.setOnAction(guessc);
		Guess.setDisable(true);
		Menu m = new Menu ("Aplication");
		MenuItem start = new MenuItem("Start");
		MenuItem load = new MenuItem("Load");
		MenuItem create = new MenuItem("Create");
		MenuItem exit = new MenuItem("Exit");
		m.getItems().add(start);
		m.getItems().add(load);
		m.getItems().add(create);
		m.getItems().add(exit);
		EventHandler<ActionEvent> START = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if (dictid == "") {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("WARNING");
					alert.setContentText("You have not Loaded a dictionary");
					alert.showAndWait();

					
				}
				else {
					game = new Game();
					image = 0;
					img.setImage(new Image("img/"+String.valueOf(image)+".png"));

					game.setDict(dictid);
					Guess.setDisable(false);
					game.setWord();
					String probs = " ";
					for (int i = 0; i < game.getWord().length(); i++) {
							probs += String.valueOf(i+1) + " : ";
							for (String s : game.Listperpos(i)) {
								probs += s + "  ";
							}
							probs += " \n ";
					}
					prob.setText(String.valueOf(probs));
					wrdlabel.setText(game.getDisplayed());	
					wlabel.setText("Words:" + String.valueOf(game.getWsize()));

					plabel.setText("Points:" + String.valueOf(game.getPoints()));

					glabel.setText("Correct Guesses: "+100*(game.getGuesses() - game.getWguesses())/Math.max(game.getWguesses(),1)+"%");

				}
			}
		};
		EventHandler<ActionEvent> LOAD  = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				final ChoiceBox <String> cb = new ChoiceBox<String>();
				for (File f: new File("medialab/").listFiles()) {
					if (f.isFile()) {
						cb.getItems().add(f.getName());
					}
					
				}
				Button btn = new Button ("Load");
				Label lblc = new Label("Choose from the List:");
				lblc.setPadding(new Insets(20));			
				
				VBox popup = new VBox(lblc,cb,btn);
				Scene pop = new Scene(popup);
				final Stage st = new Stage();
				st.setScene(pop);
				st.setTitle("LOAD DICTIONARY");
				st.show();
				EventHandler<ActionEvent> submit = new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						dictid = cb.getValue().replace("hangman_", "").replace(".txt", "");
						Dictionary tmp = new Dictionary(dictid);
						wlabel.setText("Words:" + String.valueOf(tmp.load().size()-1));


						st.close();
					}
				};
				btn.setOnAction(submit);
			}


		};
		EventHandler<ActionEvent> CREATE  = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				final Stage stag = new Stage();
				Label lblc = new Label("Submit Open Library ID:");
				lblc.setPadding(new Insets(20));
				final TextField text= new TextField();
				text.setPadding(new Insets(10));
				Button submit = new Button ("Download");
				submit.setPadding(new Insets(10));
				submit.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {
					String id;
					id =  text.getText();

					try {
						Game.newDictionary(id);
						stag.close();
					} catch (InvalidRangeException e1) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("INVAID DICTIONARY");
						alert.setContentText("Dictionary contains small words.");
						alert.showAndWait();
						
					} catch (UndersizeException e1) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("INVAID DICTIONARY");
						alert.setContentText("Dictionary too short.");
						alert.showAndWait();

					} catch (UnbalancedException e1) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("INVAID DICTIONARY");
						alert.setContentText("Dictionary Unbalanced.");
						alert.showAndWait();

					} catch (IOException e1) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("ERROR");
						alert.setContentText("Cant Create File.");
						alert.showAndWait();
					}

					        }
				}
				);
				VBox vbc = new VBox(5);
				vbc.getChildren().addAll(lblc,text,submit);
				Scene roundsScene = new Scene(vbc);

				stag.setScene(roundsScene);
				stag.setTitle("CREATE DICTIONARY");
				stag.show();				
				

			}
			
		};
		EventHandler<ActionEvent> EXIT = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				System.exit(0);
			}
		};
		start.setOnAction(START);
		load.setOnAction(LOAD);
		create.setOnAction(CREATE);
		exit.setOnAction(EXIT);
		Menu d = new Menu ("Details");
		MenuItem dictionary = new MenuItem("Dictionary");
		MenuItem rounds = new MenuItem("Rounds");
		MenuItem solution = new MenuItem("Solution");
		d.getItems().add(dictionary);
		d.getItems().add(rounds);
		d.getItems().add(solution);

		EventHandler<ActionEvent> DICTIONARY  = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if (dictid == "") {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("WARNING");
					alert.setContentText("You have not Loaded a dictionary");
					alert.showAndWait();

					
				}
				else {
				Dictionary d = new Dictionary(dictid);
				List<String> w = d.load();
				VBox log = new VBox();
				for (String s : w) {
					Label l = new Label(s);
					l.setPadding(new Insets(10));
					log.getChildren().add(l);
					
				}
				ScrollPane sp = new ScrollPane();
				sp.setContent(log);
				Scene roundsScene = new Scene(sp,200,300);
				Stage stag = new Stage();
				stag.setScene(roundsScene);
				stag.setTitle("DICTIONARY");
				stag.show();	
				}
			}
		};
		EventHandler<ActionEvent> ROUNDS  = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {

				VBox log = new VBox();
				List<String> r = Game.getLog(5);
				if (r.size() == 0) {
					Label l = new Label("No Previous Games");
					l.setPadding(new Insets(10));
					log.getChildren().add(l);				
				}
				else {
					for (String s : r) {
						Label l = new Label(s);
						l.setPadding(new Insets(10));
						log.getChildren().add(l);
						
					}					
				}

				Scene roundsScene = new Scene(log);
				Stage stag = new Stage();
				stag.setScene(roundsScene);
				stag.setTitle("ROUNDS");
				stag.show();
			}
		};
		EventHandler<ActionEvent> SOLUTION  = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if (game != null && !game.checkloose()) {
				wrdlabel.setText(game.getWord());
				for (Button key: Keys ) {
					key.setDisable(true);
				}
				game.log(false);
			}
			}
		};
		
		
		dictionary.setOnAction(DICTIONARY);
		rounds.setOnAction(ROUNDS);
		solution.setOnAction(SOLUTION);
		MenuBar mb = new MenuBar();
		mb.getMenus().add(m);
		mb.getMenus().add(d);
		

		AnchorPane.setRightAnchor(prob, 20.0);
	
		
		pane.getChildren().addAll(img,prob);
		wrdlabel.setPadding(new Insets(20)); 
		VBox vb = new VBox(mb,labels,pane,wrdlabel,Keyboard);
		Scene scene = new Scene(vb, AppWidth, AppHeight);

		
		primaryStage.setScene(scene); 
		primaryStage.show(); // Display the stage
	}
	public void addScore(int i) {
		score += i;
	}
	public boolean guess(String c, int pos) {
		boolean res = game.guess(c,pos);
		String probs = "";
		for (int i = 0; i < game.getWord().length(); i++) {
				probs += String.valueOf(i + 1) + " : ";
				for (String s : game.Listperpos(i)) {
					probs += s + "  ";
				}
				probs += " \n ";
		}
		prob.setText(String.valueOf(probs));
		if (res)score = game.getPoints();
		
		return res;
	}
	private Integer checkstatus() {
		if (game.checkwin()) {
			for (Button key: Keys ) {
				key.setDisable(true);
			}
			game.log();
			Label lb = new Label("YOU WIN :)");
			lb.setPadding(new Insets(50));

			Scene roundsScene = new Scene(lb);
			Stage stag = new Stage();
			stag.setScene(roundsScene);
			stag.setTitle("SUCCESS");
			stag.show();
			return 1;
	
		}
		else if(game.checkloose()) {
			wrdlabel.setText(game.getWord());
			for (Button key: Keys ) {
				key.setDisable(true);
			}
			game.log();
			Label lb = new Label("YOU LOSE :'(");
			lb.setPadding(new Insets(50));

			Scene roundsScene = new Scene(lb);
			Stage stag = new Stage();
			stag.setScene(roundsScene);
			stag.setTitle("GAME OVER");
			stag.show();	

			return -1;

		}

		else {
			return 0;
		}
		
		
		
		
	}
	public static void main(String[] args) {
		launch(args);
	}
}
