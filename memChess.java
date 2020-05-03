package finalProject;

import java.io.File;
import java.util.LinkedList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.input.*;

public class memChess extends Application
{
	long lasttime;
	boolean firsttime = true;
	BorderPane root;
	Scene scene;
	AnchorPane controlPanel;
	VBox player1;//Left side
	VBox player2;//Right side
	HBox player1Red;//Stores the selected pegs in an ordered position-- Might change to something more efficent
	HBox player1Blue;//Stores the selected pegs in an ordered position
	HBox player1Yellow;//Stores the selected pegs in an ordered position
	HBox player1Green;//Stores the selected pegs in an ordered position
	HBox player1Orange;//Stores the selected pegs in an ordered position
	HBox player1Purple;//Stores the selected pegs in an ordered position
	StackPane board;//Board where pegs are
	Color curRoll;// To store current roll to check against peg
	Rectangle diceBox; //outter box of the die mainly for user feed back that they rolled the die
	Text messageBoard; //Message board that keeps the user informed along the way
	Text diceMessage; //Tells the user what they rolled
	Integer finds = 0; //Tallies user correct
	Integer wrongs = 0; //Tallies user misses
	String color; //For dice message to change with each user roll
	Driver drive; //Driver for animation
	LinkedList <Peg> outie;//to move the outer pegs
	LinkedList <Peg> innie;//to move the inner pegs
	int redFound; //keeps track of pegs found
	int blueFound; //keeps track of pegs found
	int yellowFound; //keeps track of pegs found
	int greenFound; //keeps track of pegs found
	int orangeFound; //keeps track of pegs found
	int purpleFound; //keeps track of pegs found
	boolean turn; //for rolling the die to turn it on and off
	boolean gameOn; //keeps track of game over
	boolean musicOn = false; //for game over music so it doesnt play forever
	MediaPlayer mp; //Media player for game over music


	public static void main( String[] args )
	{ launch(args); }

	public void start(Stage stage)
	{
		root = new BorderPane();
		scene = new Scene(root, 900, 800);
		stage.setTitle("Final Project");
		stage.setScene(scene);
		stage.show();
		player1 = new VBox();
		player2 = new VBox();
		board = new StackPane();
		controlPanel = new AnchorPane();
		turn = false; 
		color = "N/A";
		outie = new LinkedList<Peg>();
		innie = new LinkedList<Peg>();

		messageBoard = new Text("Welcome lets test your memory!");
		messageBoard.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
		diceMessage = new Text("Your roll was: ");
		diceMessage.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
		lasttime = 0;
		gameOn = false;

		setUp();
		drive = new Driver();
		root.setCenter(board);
		root.setLeft(player1);
		root.setRight(player2);
		root.setBottom(controlPanel);
	}
	//Calls each important set up function
	public void setUp()
	{
		drawSide1();
		drawSide2();
		drawBoard();
		dice();
		controls();
		updateScore();
	}
	//Function drawBoard
	//Purpose:Draws the entire board an initializes each peg while adding them to the linked list
	//Parameters:None
	//Return:None
	public void drawBoard()
	{
		Circle outterCircle = new Circle(400,400,300);
		outterCircle.setFill(Color.BURLYWOOD);
		outterCircle.setStroke(Color.BLACK);
		outterCircle.setStrokeWidth(3);
		Circle innerCircle = new Circle(400,400,200);
		innerCircle.setFill(Color.BURLYWOOD);
		innerCircle.setStroke(Color.BLACK);
		innerCircle.setStrokeWidth(3);
		Circle centerCircle = new Circle(400,400,75);
		centerCircle.setFill(Color.BLACK);
		StackPane.setAlignment(outterCircle, Pos.CENTER);

		board.getChildren().addAll(outterCircle,innerCircle,centerCircle);

		Color[] colArr= new Color[24];//Sets four sets of colors
		for(int i = 0; i < 24; i++)
		{
			if(i < 4)
			{
				colArr[i] = Color.RED;
			}
			else if( i < 8 && i >= 4)
			{
				colArr[i] = Color.BLUE;
			}
			else if(i < 12 && i >= 8)
			{
				colArr[i] = Color.YELLOW;
			}
			else if( i < 16 && i >= 12)
			{
				colArr[i] = Color.GREEN;
			}
			else if(i < 20 && i >= 16)
			{
				colArr[i] = Color.ORANGE;
			}
			else
			{
				colArr[i] = Color.MEDIUMPURPLE;
			}
		}
		RandomColor(colArr); //Randomizes colors for peg assignment;
		//Holds the initial degrees for both circles
		double[] degPoint = {0, 22.5, 45, 67.5,90,112.5,135,157.5,180,202.5,225,247.5,270,292.5,315,337.5,0,45,90,135,180,225,270,315};
		//Sets up all of the pegs with a Random color assignment
		for(int i = 0; i < colArr.length; i ++)
		{
			Peg spot = new Peg(colArr[i]);
			double cordX = (i <16)?  spot.findNewX(degPoint[i], 250):spot.findNewX(degPoint[i], 125);
			double cordY = (i <16)?  spot.findNewY(degPoint[i], 250):spot.findNewY(degPoint[i], 125);
			spot.setDeg(degPoint[i]);
			spot.setCord(cordX, cordY);
			
			spot.addEventHandler//Event Handler That interactes with each peg for their color and properties
			(  MouseEvent.MOUSE_CLICKED,
					(MouseEvent m )->
			{
				if(gameOn)
				{
					if(turn)
					{
						if(!spot.getFound())
						{
							Color c = spot.getBottom();
							spot.setSpot(c);
							if(c == curRoll)
							{

								messageBoard.setText("You selected a " + spot.getColor() + " peg: Correct!");
								spot.setFound(true);
								finds++;
								updateScore();
								spot.setSpot(Color.BLACK);
								foundColor(spot.getColor());
								Dot dot = new Dot(c);
								setDots(spot.getColor(), dot);
								playClip("chime.mp3");
								if(finds == 24)
								{
									gameOver();
								}
							}
							else
							{
								playClip("wrong.mp3");
								messageBoard.setText("You selected a " + spot.getColor() + " peg: Incorrect :(");
								spot.resetTop();
								wrongs++;
								updateScore();

							}
							diceBox.setFill(Color.FLORALWHITE);
							turn = false;
						}
					}
				}

			});
			
			if(i<16)
			{
				outie.add(spot);
			}
			else
			{
				innie.add(spot);
			}
			board.getChildren().add(spot);
		}
		//Calls Extra Controls
		extraControls();

	}
	//Function extraControls
	//Purpose:Sets up the message board as well as the dice message
	//Parameters: None 
	//Return: None
	public void extraControls()
	{
		StackPane.setAlignment(messageBoard, Pos.TOP_LEFT);
		messageBoard.setTranslateY(10);

		diceMessage.setText("Your roll was: " + color);

		StackPane.setAlignment(diceMessage, Pos.BOTTOM_CENTER);
		//		messageBoard.setTranslateY(10);
		board.getChildren().addAll(messageBoard, diceMessage);

	}
	//Function gameOver
	//Purpose: Ends game with UX effects
	//Parameters: None
	//Return: None
	public void gameOver()
	{
		gameOn =false; 
		board.getChildren().clear();// Had to do this because everytime the game would end there would be random black dots
		drawBoard();
		for(Peg p: outie)
		{
			p.lastCall();
		}
		for(Peg p: innie)
		{
			p.lastCall();
		}
		messageBoard.setText("Congratulations! You have won! Play Again?");
		playAudio("trial.mp3");
		musicOn = true;
	}
	//Function playAudio
	//Purpose: Playing the end game music
	//Parameters: String for what the file is called
	//Return: None
	public void playAudio(String f)
	{
		File fsFile = new File( f );
		String rh = fsFile.toURI().toString();
		Media m = new Media( rh );
		mp = new MediaPlayer(m);
		mp.setVolume(0.5);

		mp.play();
	}
	//Function playClip
	//Purpose: Plays little clips that are for UX
	//Parameters: String with the file name
	//Return: None
	public void playClip(String c)
	{
		File clip = new File(c);
		String rH = clip.toURI().toString();
		System.out.println("reallyHere="+rH);
		AudioClip soundClip = new AudioClip(rH);
		soundClip.setVolume(0.7);

		soundClip.play(); 
	}
	//Function setColorText
	//Purpose: Changes the string of color to update both the dice messsage and the message board 
	//Parameters: None (Gets input from curRoll a global variable
	//Return: None
	public void setColorText()
	{
		if(curRoll == Color.RED)
		{
			color = "Red";
		}
		else if(curRoll == Color.YELLOW)
		{
			color = "Yellow";
		}
		else if(curRoll == Color.BLUE)
		{
			color = "Blue";
		}
		else if(curRoll == Color.GREEN)
		{
			color = "Green";
		}
		else if(curRoll == Color.ORANGE)
		{
			color = "Orange";
		}
		else if(curRoll == Color.MEDIUMPURPLE)
		{
			color = "Purple";
		}
	}
	//Function controls
	//Purpose: Sets up and assigns responsibilites to the control panel at the bottom
	//Parameters: None
	//Return: None
	public void controls()
	{
		Button dif = new Button("Easy Mode"); //Toggles between easy mode and hard mode while starting and stoping the driver
		System.out.println();
		dif.addEventHandler
		(  MouseEvent.MOUSE_CLICKED,
				(MouseEvent m )->
		{
			if(dif.getText() == "Easy Mode")
			{
				dif.setText("Hard Mode");
				drive.start();

			}
			else
			{
				dif.setText("Easy Mode");
				drive.stop();
			}
		});
		//Starts game while toggling between start and reset
		Button startGame = new Button("Start Game");

		startGame.addEventHandler
		(  MouseEvent.MOUSE_CLICKED,
				(MouseEvent m )->
		{
			if(startGame.getText() == "Start Game")
			{
				gameOn = true;
				startGame.setText("Restart");
				board.getChildren().clear();
				outie.clear();
				innie.clear();
				drawBoard();
				dice();
				startGame();
			}
			else
			{
				gameOn = true;
				board.getChildren().clear();
				outie.clear();
				innie.clear();
				drawBoard();
				dice();
				startGame();
			}
		});
		//Shows and hides the game rules since its kind of wordy
		Button rules = new Button("Game Rules");

		rules.addEventHandler
		(  MouseEvent.MOUSE_CLICKED,
				(MouseEvent m )->
		{
			if(player2.isVisible())
			{
				player2.setVisible(false);
			}
			else
			{
				player2.setVisible(true);
			}
		});
		
		AnchorPane.setLeftAnchor(dif, 400.0);
		AnchorPane.setRightAnchor(dif, 400.0);
		AnchorPane.setTopAnchor(dif, 20.0);
		AnchorPane.setLeftAnchor(startGame, 20.0);
		AnchorPane.setTopAnchor(startGame, 20.0);
		AnchorPane.setRightAnchor(rules, 20.0);
		AnchorPane.setTopAnchor(rules, 20.0);
		controlPanel.getChildren().addAll(dif,startGame,rules);
		controlPanel.setPadding(new Insets(20, 0, 20,0));
	}
	//Function startGame
	//Purpose: Starts the game by showing the tops of the pegs for an instant
	// as well as clearing the scorBoard
	//Parameters: None
	//Return: None
	public void startGame()
	{				
		if(musicOn)
		{		
			mp.stop();
			musicOn = false;
		}
		//Flashes the tops of the pegs so users can have a chance to see it
		for(Peg p: outie)
		{
			p.resetTop();
		}
		for(Peg p: innie)
		{
			p.resetTop();
		}
		redFound = 0;
		blueFound = 0;
		yellowFound = 0;
		greenFound = 0;
		orangeFound = 0;
		purpleFound = 0;
		finds = 0;
		wrongs = 0;

		player1Red.getChildren().clear();
		player1Blue.getChildren().clear();
		player1Yellow.getChildren().clear();
		player1Green.getChildren().clear();
		player1Orange.getChildren().clear();
		player1Purple.getChildren().clear();

		Text redScore = new Text("Red's: ");
		Text blueScore = new Text("Blue's: ");
		Text yellowScore = new Text("Yellow's: ");
		Text greenScore = new Text("Green's: ");
		Text orangeScore = new Text("Orange's: ");
		Text pinkScore = new Text("Purple's: ");
		player1Red.getChildren().add(redScore);
		player1Blue.getChildren().add(blueScore);
		player1Yellow.getChildren().add(yellowScore);
		player1Green.getChildren().add(greenScore);
		player1Orange.getChildren().add(orangeScore);
		player1Purple.getChildren().add(pinkScore);
	}
	//Function dice
	//Purpose: Sets up the interactive die as well as the box that will change color on click
	//Parameters: None
	//Return: None
	public void dice()
	{
		diceBox = new Rectangle(100,100);
		diceBox.setStroke(Color.BLACK);
		diceBox.setStrokeWidth(3);
		diceBox.setFill(Color.FLORALWHITE);
		Die die = new Die();
		die.addEventHandler
		(  MouseEvent.MOUSE_CLICKED,
				(MouseEvent m )->
		{
			if(gameOn)
			{
				rollDie(die);
			}

		});
		StackPane.setAlignment(diceBox, Pos.TOP_RIGHT);
		StackPane.setAlignment(die, Pos.TOP_RIGHT);

		Text rollHere = new Text("Click the die to roll\n   (or press 'Enter')");
		rollHere.setTranslateX(-3);
		rollHere.setTranslateY(105);
		rollHere.setFont(Font.font("Verdana", FontWeight.BOLD, 9.5));
		StackPane.setAlignment(rollHere, Pos.TOP_RIGHT);

		board.getChildren().addAll(diceBox, die, rollHere);
		scene.setOnKeyPressed
		(  (KeyEvent ke) -> 
		{
			if(gameOn)
			{
				if (ke.getCode().equals(KeyCode.ENTER))
				{
					rollDie(die);
				}
			}

		} 
				);
	}
	//Function roleDie
	//Purpose: Roles the die randomly while checking to see if that color has anymore pegs on the board
	//Parameters: Die
	//Return: None
	public void rollDie(Die die)
	{
		//Checks the backgrond of the die box first to proceed
		if(diceBox.getFill() != Color.BLACK)
		{
			//Rolls the die and gets the color
			if(Dice.getRandomRoll() == Dice.RED)
			{
				//if there is a peg remaining proceed
				if(redFound != 4)
				{
					die.setDie(Color.RED);
					curRoll = die.getDie();
					diceBox.setFill(Color.BLACK);
					playClip("roll.mp3");
				}
				//else keeping rolling behind the scenes until an available peg
				else
				{
					while(diceBox.getFill() != Color.BLACK)
					{
						rollDie(die);
					}				}


			}
			else if(Dice.getRandomRoll() == Dice.BLUE)
			{
				//if there is a peg remaining proceed
				if(blueFound != 4)
				{
					die.setDie(Color.BLUE);
					curRoll = die.getDie();
					diceBox.setFill(Color.BLACK);
					playClip("roll.mp3");
				}
				//else keeping rolling behind the scenes until an available peg
				else
				{
					while(diceBox.getFill() != Color.BLACK)
					{
						rollDie(die);
					}				
				}


			}
			else if(Dice.getRandomRoll() == Dice.YELLOW)
			{
				//if there is a peg remaining proceed
				if(yellowFound != 4)
				{

					die.setDie(Color.YELLOW);
					curRoll = die.getDie();
					diceBox.setFill(Color.BLACK);
					playClip("roll.mp3");
				}
				//else keeping rolling behind the scenes until an available peg
				else
				{
					while(diceBox.getFill() != Color.BLACK)
					{
						rollDie(die);
					}
				}


			}
			else if(Dice.getRandomRoll() == Dice.GREEN)
			{
				//if there is a peg remaining proceed
				if(greenFound != 4)
				{
					die.setDie(Color.GREEN);
					curRoll = die.getDie();
					diceBox.setFill(Color.BLACK);
					playClip("roll.mp3");
				}
				//else keeping rolling behind the scenes until an available peg
				else
				{
					while(diceBox.getFill() != Color.BLACK)
					{
						rollDie(die);
					}				
				}


			}
			else if(Dice.getRandomRoll() == Dice.ORANGE)
			{
				//if there is a peg remaining proceed
				if(orangeFound != 4)
				{
					die.setDie(Color.ORANGE);
					curRoll = die.getDie();
					diceBox.setFill(Color.BLACK);
					playClip("roll.mp3");
				}
				//else keeping rolling behind the scenes until an available peg
				else
				{
					while(diceBox.getFill() != Color.BLACK)
					{
						rollDie(die);
					}
				}

			}
			else if(Dice.getRandomRoll() == Dice.PURPLE)
			{
				//if there is a peg remaining proceed
				if(purpleFound != 4)
				{
					die.setDie(Color.MEDIUMPURPLE);
					curRoll = die.getDie();
					diceBox.setFill(Color.BLACK);
					playClip("roll.mp3");
				}
				//else keeping rolling behind the scenes until an available peg
				else
				{
					while(diceBox.getFill() != Color.BLACK)
					{
						rollDie(die);
					}
				}


			}
		}
		else
		{
			messageBoard.setText("You need to select a peg now.");
		}
		setColorText();
		diceMessage.setText("Your roll was: " + color);
		turn = true;

	}
	//Function setDots
	//Purpose: Sets the dot to the appropriate HBox on the side
	//Parameters: A string of what color it is and the Dot assiosciated with it
	//Return: None
	public void setDots(String c,Dot d)
	{
		if(c == "Red")
		{
			player1Red.getChildren().add(d);

		}
		else if(c == "Yellow")
		{
			player1Yellow.getChildren().add(d);

		}
		else if(c == "Blue")
		{
			player1Blue.getChildren().add(d);
		}
		else if(c == "Green")
		{
			player1Green.getChildren().add(d);

		}
		else if(c == "Orange")
		{
			player1Orange.getChildren().add(d);

		}
		else if(c == "Purple")
		{
			player1Purple.getChildren().add(d);
		}
	}
	//To roll dice randomly Enum
	private enum Dice
	{
		RED, 
		BLUE, 
		YELLOW, 
		GREEN, 
		ORANGE,
		PURPLE;

		public static Dice getRandomRoll() {
			Random random = new Random();
			return values()[random.nextInt(values().length)];
		}
	}
	//Function RandomColor
	//Purpose: Ranomizes the array of colors so I can add them to the board in no order
	//Parameters: An array of colors
	//Return: An array of colors
	public Color[] RandomColor(Color[] array)
	{
		Random rand = new Random();
		for(int i=0; i <array.length; i++)
		{
			int randomSpot = rand.nextInt(array.length);
			Color temp = array[i];
			array[i]= array[randomSpot];
			array[randomSpot] = temp;
		}

		return array;
	}
	//Function drawSide1
	//Purpose: Draws the left side with the scored board also with how the game will look in the end
	//Parameters:None
	//Return: None
	public void drawSide1()
	{
		Text scoreBoard = new Text("Score Board");

		player1Red = new HBox();
		player1Blue = new HBox();
		player1Yellow = new HBox();
		player1Green = new HBox();
		player1Orange = new HBox();
		player1Purple = new HBox();

		player1Red.setSpacing(5);
		player1Blue.setSpacing(5);
		player1Yellow.setSpacing(5);
		player1Green.setSpacing(5);
		player1Orange.setSpacing(5);
		player1Purple.setSpacing(5);

		Text redScore = new Text("Red's: ");
		Text blueScore = new Text("Blue's: ");
		Text yellowScore = new Text("Yellow's: ");
		Text greenScore = new Text("Green's: ");
		Text orangeScore = new Text("Orange's: ");
		Text pinkScore = new Text("Purple's: ");
		player1Red.getChildren().add(redScore);
		player1Blue.getChildren().add(blueScore);
		player1Yellow.getChildren().add(yellowScore);
		player1Green.getChildren().add(greenScore);
		player1Orange.getChildren().add(orangeScore);
		player1Purple.getChildren().add(pinkScore);
		//Assigns all of  the dots initially
		for(int i = 0; i < 4; i++)
		{
			Dot red1 = new Dot(Color.RED);
			Dot blue1 = new Dot(Color.BLUE);;
			Dot yellow1 = new Dot(Color.YELLOW);
			Dot green1 = new Dot(Color.GREEN);
			Dot orange1 = new Dot(Color.ORANGE);
			Dot pink1 = new Dot(Color.MEDIUMPURPLE);

			player1Red.getChildren().add(red1);
			player1Blue.getChildren().add(blue1);;
			player1Yellow.getChildren().add(yellow1);
			player1Green.getChildren().add(green1);
			player1Orange.getChildren().add(orange1);
			player1Purple.getChildren().add(pink1);

		}

		player1.setPadding(new Insets(70, 12, 15, 12));
		player1.setSpacing(20);
		player1.getChildren().addAll(scoreBoard, player1Red, player1Blue, player1Yellow, player1Green, player1Orange, player1Purple);
		String rights = finds.toString();
		String wrong = wrongs.toString();

		Text rightFinds = new Text("Found: " + rights);
		Text misses = new Text("Misses: " + wrong);
		player1.getChildren().addAll(rightFinds,misses);
	}
	//Function updateScore
	//Purpose: Removes the old score and updates it with the new score
	//Parameters: None
	//Return: None
	public void updateScore()
	{
		player1.getChildren().remove(8);
		player1.getChildren().remove(7);
		String rights = finds.toString();
		String wrong = wrongs.toString();

		Text rightFinds = new Text("Found: " + rights);
		Text misses = new Text("Misses: " + wrong);
		player1.getChildren().addAll(rightFinds,misses);

	}
	//Function foundColor
	//Purpose: Updates the count of pegs of a certian color found so the die doesnt need to be rolled forever
	//Parameters: String of what color it is
	//Return: None
	public void  foundColor(String c)
	{
		if(c == "Red")
		{
			redFound++;
		}
		else if(c == "Yellow")
		{
			yellowFound++;
		}
		else if(c == "Blue")
		{
			blueFound++;
		}
		else if(c == "Green")
		{
			greenFound++;

		}
		else if(c == "Orange")
		{
			orangeFound++;
		}
		else if(c == "Purple")
		{
			purpleFound++;
		}
	}
	//Function drawSide2
	//Purpose: Sets up the rules and hides them for the user to toggle
	//Parameters:None
	//Return: None
	public void drawSide2()
	{
		Text rulesTitle = new Text("Rules");
		Text rulesParagraph = new Text("Welcome to your "
				+ "\nbrain game."
				+ "\nThis is a memory game "
				+ "\nthat will test your skills "
				+ "\nwith two diifculties."
				+ "\nif you choose to accept "
				+ "\nthe challenge select"
				+ "\nstart game and "
				+ "\nmemorize as much as "
				+ "\npossible. If you mess up "
				+ "\ntoo much go ahead "
				+ "and "
				+ "\npress restart to wipe "
				+ "\nthe board anew. "
				+ "\nRoll the Die in the "
				+ "\ncorner to choose a "
				+ "\ncolor to find, you "
				+ "\ncan also select 'Enter' "
				+ "\nto roll if that is your "
				+ "\ngrove."
				+ "\nTry and solve it in "
				+ "\nthe least amount of rolls "
				+ "\nas possible. "
				+ "\nMost importantly enjoy! \n"
				+ "Oh, as mentioned if "
				+ "\nyou're feeling "
				+ "\nadventours go ahead "
				+ "\nand take a crack at hard "
				+ "\nmode by pressing the "
				+ "\nbottom middle button.");

		rulesParagraph.setFont(Font.font("Verdana", 12));
		player2.setPadding(new Insets(70, 12, 15, 12));
		player2.setSpacing(20);
		player2.getChildren().addAll(rulesTitle, rulesParagraph);
		player2.setVisible(false);

	}


	//Class dot with the purpose of creating a similar dot to append on the side so players can keep track of the pegs they found
	public class Dot extends Circle
	{

		public Dot(Color c)
		{
			super(4);
			setFill(c);
			setStroke(Color.BLACK);
			setStrokeWidth(1);

		}

	}
	//Class Die with the purpose of setting up a die that will change and hold the color of the die
	public class Die extends Group
	{
		Color cur = Color.YELLOW;
		Rectangle box;
		Circle dot;


		public Die()
		{
			box = new Rectangle(40,40);
			dot = new Circle(9);
			box.setStroke(Color.BLACK);
			box.setFill(Color.WHITE);
			box.setStrokeWidth(2);	
			setTranslateX(-30);
			setTranslateY(30);
			dot.setTranslateX(20);
			dot.setTranslateY(20);
			dot.setFill(cur);
			dot.setStroke(Color.BLACK);
			dot.setStrokeWidth(2);

			getChildren().addAll(box,dot);

		}

		public void setDie(Color c)
		{

			cur = c;
			dot.setFill(cur);
		}


		public Color getDie()
		{
			return cur;
		}


	}
	//Animation driver for moving the Pegs
	public class Driver extends AnimationTimer
	{
		@Override
		public void handle(long now)
		{
			if ( firsttime ) { lasttime = now; firsttime = false; }
			else
			{
				double deltat = (now-lasttime) * 12.0e-9;//Moves peg at a certain speed 
				lasttime = now;

				for(Peg p: outie)
				{
					p.move(deltat, 250);
				}

				for(Peg p: innie)
				{
					p.move(-deltat, 150);
				}

			}

		}


	}


}
