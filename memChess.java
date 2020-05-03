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
	Rectangle diceBox;
	Text messageBoard;
	Text diceMessage;
	Integer finds = 0;
	Integer wrongs = 0;
	String color;
	Driver drive;
	LinkedList <Peg> outie;//to move the outer pegs
	LinkedList <Peg> innie;//to move the inner pegs
	int redFound;
	int blueFound;
	int yellowFound;
	int greenFound;
	int orangeFound;
	int purpleFound;
	boolean turn;
	boolean gameOn;
	boolean musicOn = false;
	MediaPlayer mp;


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
		turn = false; //Testing with it turned on, but will be turned off
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

	public void setUp()
	{

		drawSide1();
		drawSide2();
		drawBoard();
		dice();
		controls();
		updateScore();
	}

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

		double[] degPoint = {0, 22.5, 45, 67.5,90,112.5,135,157.5,180,202.5,225,247.5,270,292.5,315,337.5,0,45,90,135,180,225,270,315};


		for(int i = 0; i < colArr.length; i ++)
		{
			Peg spot = new Peg(colArr[i]);
			double cordX = (i <16)?  spot.findNewX(degPoint[i], 250):spot.findNewX(degPoint[i], 125);
			double cordY = (i <16)?  spot.findNewY(degPoint[i], 250):spot.findNewY(degPoint[i], 125);
			spot.setDeg(degPoint[i]);
			spot.setCord(cordX, cordY);
			spot.addEventHandler
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

		extraControls();

	}

	public void gameOver()
	{

		gameOn =false;
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


	public void playAudio(String f)
	{
		File fsFile = new File( f );
		String rh = fsFile.toURI().toString();
		Media m = new Media( rh );
		mp = new MediaPlayer(m);
		mp.setVolume(0.5);

		mp.play();
	}

	public void playClip(String c)
	{
		File clip = new File(c);
		String rH = clip.toURI().toString();
		System.out.println("reallyHere="+rH);
		AudioClip soundClip = new AudioClip(rH);
		soundClip.setVolume(0.7);

		soundClip.play(); 
	}


	public void extraControls()
	{
		StackPane.setAlignment(messageBoard, Pos.TOP_LEFT);
		messageBoard.setTranslateY(10);

		diceMessage.setText("Your roll was: " + color);

		StackPane.setAlignment(diceMessage, Pos.BOTTOM_CENTER);
		//		messageBoard.setTranslateY(10);
		board.getChildren().addAll(messageBoard, diceMessage);

	}

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

	public void controls()
	{
		Button dif = new Button("Easy Mode");
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

		Button single = new Button("Start Game");

		single.addEventHandler
		(  MouseEvent.MOUSE_CLICKED,
				(MouseEvent m )->
		{
			if(single.getText() == "Start Game")
			{
				gameOn = true;
				single.setText("Restart");
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
		Button multi = new Button("Game Rules");

		multi.addEventHandler
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
		AnchorPane.setLeftAnchor(single, 20.0);
		AnchorPane.setTopAnchor(single, 20.0);
		AnchorPane.setRightAnchor(multi, 20.0);
		AnchorPane.setTopAnchor(multi, 20.0);
		controlPanel.getChildren().addAll(dif,single,multi);
		controlPanel.setPadding(new Insets(20, 0, 20,0));
	}


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

		Text rollHere = new Text("Click the die to roll");
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

	public void rollDie(Die die)
	{

		if(diceBox.getFill() != Color.BLACK)
		{
			if(Dice.getRandomRoll() == Dice.RED)
			{
				if(redFound != 4)
				{
					die.setDie(Color.RED);
					curRoll = die.getDie();
					diceBox.setFill(Color.BLACK);
					playClip("roll.mp3");
				}
				else
				{
					while(diceBox.getFill() != Color.BLACK)
					{
						rollDie(die);
					}				}


			}
			else if(Dice.getRandomRoll() == Dice.BLUE)
			{
				if(blueFound != 4)
				{
					die.setDie(Color.BLUE);
					curRoll = die.getDie();
					diceBox.setFill(Color.BLACK);
					playClip("roll.mp3");
				}
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
				if(yellowFound != 4)
				{

					die.setDie(Color.YELLOW);
					curRoll = die.getDie();
					diceBox.setFill(Color.BLACK);
					playClip("roll.mp3");
				}
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
				if(greenFound != 4)
				{
					die.setDie(Color.GREEN);
					curRoll = die.getDie();
					diceBox.setFill(Color.BLACK);
					playClip("roll.mp3");
				}
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
				if(orangeFound != 4)
				{
					die.setDie(Color.ORANGE);
					curRoll = die.getDie();
					diceBox.setFill(Color.BLACK);
					playClip("roll.mp3");
				}
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
				if(purpleFound != 4)
				{
					die.setDie(Color.MEDIUMPURPLE);
					curRoll = die.getDie();
					diceBox.setFill(Color.BLACK);
					playClip("roll.mp3");
				}
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


	//To roll dice randomly
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

	// To shuffle the color order
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

	public void drawSide1()
	{
		Text firstPlayer = new Text("Player 1");

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
		player1.getChildren().addAll(firstPlayer, player1Red, player1Blue, player1Yellow, player1Green, player1Orange, player1Purple);
		String rights = finds.toString();
		String wrong = wrongs.toString();

		Text rightFinds = new Text("Found: " + rights);
		Text misses = new Text("Misses: " + wrong);
		player1.getChildren().addAll(rightFinds,misses);
	}

	public void startGame()
	{				
		if(musicOn)
		{		
			mp.stop();
			musicOn = false;
		}

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

	public void  foundColor(String c)
	{
		if(c == "Red")
		{
			redFound++;
			System.out.println("Red=" + redFound);
		}
		else if(c == "Yellow")
		{
			yellowFound++;
			System.out.println("Yellow=" + yellowFound);
		}
		else if(c == "Blue")
		{
			blueFound++;
			System.out.println("Blue=" + blueFound);
		}
		else if(c == "Green")
		{
			greenFound++;
			System.out.println("Green=" + greenFound);

		}
		else if(c == "Orange")
		{
			orangeFound++;
			System.out.println("Orange=" + orangeFound);

		}
		else if(c == "Purple")
		{
			purpleFound++;
			System.out.println("Purple=" + purpleFound);
		}
	}

	public void drawSide2()
	{
		Text secondPlayer = new Text("Rules");
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
		player2.getChildren().addAll(secondPlayer, rulesParagraph);
		player2.setVisible(false);

	}



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
