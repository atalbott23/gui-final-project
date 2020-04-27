package finalProject;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Semaphore;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class memChess extends Application
{
	BorderPane root;
	Scene scene;
	AnchorPane controlPanel;
	VBox player1;//Left side
	VBox player2;//Right side
	HBox player1Red;//Stores the selected pegs in an ordered position-- Might change to something more efficent
	HBox player2Red;//Stores the selected pegs in an ordered position
	HBox player1Blue;//Stores the selected pegs in an ordered position
	HBox player2Blue;//Stores the selected pegs in an ordered position
	HBox player1Yellow;//Stores the selected pegs in an ordered position
	HBox player2Yellow;//Stores the selected pegs in an ordered position
	HBox player1Green;//Stores the selected pegs in an ordered position
	HBox player2Green;//Stores the selected pegs in an ordered position
	HBox player1Orange;//Stores the selected pegs in an ordered position
	HBox player2Orange;//Stores the selected pegs in an ordered position
	HBox player1Pink;//Stores the selected pegs in an ordered position
	HBox player2Pink;//Stores the selected pegs in an ordered position
	StackPane board;//Board where pegs are
	Color curRoll;// To store current roll to check against peg


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

		setUp();
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
				colArr[i] = Color.DEEPPINK;
			}

		}
		RandomColor(colArr); //Randomizes colors for peg assignment;

		//Had to use geometry on my calculator because math on here wasnt working. Sets all of the pegs.
		double[] cordX = {250,230.97,176.777,95.671,0,-95.671,-176.777,-230.97,-250,
				-230.97,-176.777,-95.671,0,95.671,176.777,230.97,125,88.388,0,-88.388,
				-125,-88.388,0,88.388};

		double[] cordY = {0,95.671,176.777,230.97,250,230.97,
				176.777,95.671,0,-95.671,-176.777,-230.97,-250,
				-230.97,-176.777,-95.671,0,88.388,125,88.388,0,
				-88.388,-125,-88.388};		

		for(int i = 0; i < cordX.length; i ++)
		{
			Peg spot = new Peg(colArr[i]);
			spot.setCord(cordX[i], cordY[i]);
			spot.addEventHandler
			(  MouseEvent.MOUSE_CLICKED,
					(MouseEvent m )->
			{
				if(!spot.getFound())
				{
					Color c = spot.getBottom();
					spot.setSpot(c);
					if(c == curRoll)
					{
						System.out.println("found one");
						
					}
					else
					{
						System.out.println("Nope");
					}
				}

			});
			board.getChildren().add(spot);
		}
	}
	
	public void controls()
	{
		Button dif = new Button("Easy Mode");
		AnchorPane.setLeftAnchor(dif, 400.0);
		AnchorPane.setRightAnchor(dif, 400.0);
		AnchorPane.setTopAnchor(dif, 20.0);
		controlPanel.getChildren().add(dif);
		controlPanel.setPadding(new Insets(20, 0, 20,0));
	}
	
	
	public void dice()
	{
		Rectangle box = new Rectangle(100,100);
		box.setStroke(Color.BLACK);
		box.setStrokeWidth(3);
		box.setFill(Color.AQUA);
		Die die = new Die();
		die.addEventHandler
		(  MouseEvent.MOUSE_CLICKED,
				(MouseEvent m )->
		{
//			Dice.getRandomRoll();
			if(Dice.getRandomRoll() == Dice.RED)
			{
				die.setDie(Color.RED);
				curRoll = die.getDie();
			}
			else if(Dice.getRandomRoll() == Dice.BLUE)
			{
				die.setDie(Color.BLUE);
				curRoll = die.getDie();
			}
			else if(Dice.getRandomRoll() == Dice.YELLOW)
			{
				die.setDie(Color.YELLOW);
				curRoll = die.getDie();
			}
			else if(Dice.getRandomRoll() == Dice.GREEN)
			{
				die.setDie(Color.GREEN);
				curRoll = die.getDie();
			}
			else if(Dice.getRandomRoll() == Dice.ORANGE)
			{
				die.setDie(Color.ORANGE);
				curRoll = die.getDie();
			}
			else
			{
				die.setDie(Color.DEEPPINK);
				curRoll = die.getDie();
			}
			
			

		});
		StackPane.setAlignment(box, Pos.TOP_RIGHT);
		StackPane.setAlignment(die, Pos.TOP_RIGHT);
		board.getChildren().addAll(box, die);
	}


	//To roll dice randomly
	private enum Dice
	{
		RED, 
		BLUE, 
		YELLOW, 
		GREEN, 
		ORANGE,
		PINK;


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
		player1Pink = new HBox();

		player1Red.setSpacing(5);
		player1Blue.setSpacing(5);
		player1Yellow.setSpacing(5);
		player1Green.setSpacing(5);
		player1Orange.setSpacing(5);
		player1Pink.setSpacing(5);

		Text redScore = new Text("Red's: ");
		Text blueScore = new Text("Blue's: ");
		Text yellowScore = new Text("Yellow's: ");
		Text greenScore = new Text("Green's: ");
		Text orangeScore = new Text("Orange's: ");
		Text pinkScore = new Text("Pink's: ");
		player1Red.getChildren().add(redScore);
		player1Blue.getChildren().add(blueScore);
		player1Yellow.getChildren().add(yellowScore);
		player1Green.getChildren().add(greenScore);
		player1Orange.getChildren().add(orangeScore);
		player1Pink.getChildren().add(pinkScore);

		for(int i = 0; i < 4; i++)
		{
			Dot red1 = new Dot(Color.RED);
			Dot blue1 = new Dot(Color.BLUE);;
			Dot yellow1 = new Dot(Color.YELLOW);
			Dot green1 = new Dot(Color.GREEN);
			Dot orange1 = new Dot(Color.ORANGE);
			Dot pink1 = new Dot(Color.DEEPPINK);

			player1Red.getChildren().add(red1);
			player1Blue.getChildren().add(blue1);;
			player1Yellow.getChildren().add(yellow1);
			player1Green.getChildren().add(green1);
			player1Orange.getChildren().add(orange1);
			player1Pink.getChildren().add(pink1);

		}

		player1.setPadding(new Insets(70, 12, 15, 12));
		player1.setSpacing(20);
		player1.getChildren().addAll(firstPlayer, player1Red, player1Blue, player1Yellow, player1Green, player1Orange, player1Pink);
	}

	public void drawSide2()
	{
		Text secondPlayer = new Text("Player 2");

		player2Red = new HBox();
		player2Blue = new HBox();
		player2Yellow = new HBox();
		player2Green = new HBox();
		player2Orange = new HBox();
		player2Pink = new HBox();

		player2Red.setSpacing(5);
		player2Blue.setSpacing(5);
		player2Yellow.setSpacing(5);
		player2Green.setSpacing(5);
		player2Orange.setSpacing(5);
		player2Pink.setSpacing(5);

		Text redScore = new Text("Red's: ");
		Text blueScore = new Text("Blue's: ");
		Text yellowScore = new Text("Yellow's: ");
		Text greenScore = new Text("Green's: ");
		Text orangeScore = new Text("Orange's: ");
		Text pinkScore = new Text("Pink's: ");

		player2Red.getChildren().add(redScore);
		player2Blue.getChildren().add(blueScore);
		player2Yellow.getChildren().add(yellowScore);
		player2Green.getChildren().add(greenScore);
		player2Orange.getChildren().add(orangeScore);
		player2Pink.getChildren().add(pinkScore);

		for(int i = 0; i < 4; i++)
		{
			Dot red2 = new Dot(Color.RED);
			Dot blue2 = new Dot(Color.BLUE);
			Dot yellow2 = new Dot(Color.YELLOW);
			Dot green2 = new Dot(Color.GREEN);
			Dot orange2 = new Dot(Color.ORANGE);
			Dot pink2 = new Dot(Color.DEEPPINK);

			player2Red.getChildren().add(red2);
			player2Blue.getChildren().add(blue2);
			player2Yellow.getChildren().add(yellow2);
			player2Green.getChildren().add(green2);
			player2Orange.getChildren().add(orange2);
			player2Pink.getChildren().add(pink2);

		}

		player2.setPadding(new Insets(70, 12, 15, 12));
		player2.setSpacing(20);
		player2.getChildren().addAll(secondPlayer, player2Red, player2Blue, player2Yellow, player2Green, player2Orange, player2Pink);

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

	public class Peg extends Circle
	{
		Color bottomSide;
		boolean found = false;
		public Peg(Color c)
		{
			super(15);
			setFill(Color.GHOSTWHITE);
			setStroke(Color.BLACK);
			setStrokeWidth(2);
			bottomSide = c;

		}

		public void setCord(double x, double y)
		{
			setTranslateX(x);
			setTranslateY(y);
		}

		public void setFound(boolean t)
		{
			found = t;
		}

		public boolean getFound()
		{
			return found;
		}

		public Color getBottom()
		{
			return bottomSide;
		}

		public void setSpot(Color c)
		{
			setFill(c);
		}
		
		public void resetTop()
		{
			setFill(Color.GHOSTWHITE);
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


}
