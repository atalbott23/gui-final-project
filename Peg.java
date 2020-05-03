package finalProject;

import java.util.Random;

import javafx.animation.FillTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;


//Class that contains the pegs on the board and moves the pegs as well as preforms animation
public class Peg extends Circle
{
	Color bottomSide; //to flip when peg is clicked
	boolean found = false; //only turn true if found
	String bottomName; //Holds so it can be checked with various functions
	double curDeg; //holds current degree for positioning
	public Peg(Color c)
	{
		super(15);
		setFill(Color.GHOSTWHITE);//Top of the Peg
		setStroke(Color.BLACK);
		setStrokeWidth(2);
		bottomSide = c;

		if(c == Color.RED)
		{
			bottomName = "Red";
		}
		else if(c == Color.YELLOW)
		{
			bottomName = "Yellow";
		}
		else if(c == Color.BLUE)
		{
			bottomName = "Blue";
		}
		else if(c == Color.GREEN)
		{
			bottomName = "Green";
		}
		else if(c == Color.ORANGE)
		{
			bottomName = "Orange";
		}
		else if(c == Color.MEDIUMPURPLE)
		{
			bottomName = "Purple";
		}


	}
	//Sets the peg at its initial and  new cordinaants
	public void setCord(double x, double y)
	{
		setTranslateX(x);
		setTranslateY(y);
	}
	//Turns found to true
	public void setFound(boolean t)
	{
		found = t;
	}
	//Checks to see if that peg was found before so it cant be used again
	public boolean getFound()
	{
		return found;
	}
	//To check the color with various functions
	public String getColor()
	{
		return bottomName;
	}
	//Gets the pegs color
	public Color getBottom()
	{
		return bottomSide;
	}
	//Flips the peg over and transitiions it to black
	public void setSpot(Color c)
	{
		FillTransition fillTransition = new FillTransition(Duration.seconds(3), this);
		fillTransition.setFromValue(bottomSide);
		fillTransition.setToValue(c);
		fillTransition.setCycleCount(1);
		fillTransition.play();

	}
	//for end game effect to randomize show times for better effect
	public void lastCall()
	{
		Random r = new Random();
		FillTransition fillTransition = new FillTransition(Duration.seconds(r.nextInt(4)), this);
		fillTransition.setFromValue(bottomSide);
		fillTransition.setToValue(Color.GHOSTWHITE);
		fillTransition.setCycleCount(FillTransition.INDEFINITE);
		fillTransition.play();

	}
	//Shows peg for 3 seconds then returns it to white
	public void resetTop()
	{
		FillTransition fillTransition = new FillTransition(Duration.seconds(3), this);
		fillTransition.setFromValue(bottomSide);
		fillTransition.setToValue(Color.GHOSTWHITE);
		fillTransition.setCycleCount(1);
		fillTransition.play();  
	}
	//Moves the peg to the next iteration in the animation timer
	public void move(double deltat, double r)
	{
		double x = getTranslateX();
		curDeg += deltat;
		double y = getTranslateY();

		x = findNewX(curDeg, r);

		setTranslateX(x);

		y = findNewY(curDeg, r);

		setTranslateY(y);

	}
	//finds new x by converting to radians then using cosine times radius of dot
	public double findNewX(double x, double r)
	{

		double y = Math.toRadians(x);
		double z = Math.cos(y) * r;

		return z;
	}
	//finds new y by converting to radians then using sin times radius of dot
	public double findNewY(double x, double r)
	{

		double y = Math.toRadians(x);
		double z = Math.sin(y) * r;

		return z;
	}
	//initializes the degrees
	public void setDeg(double d)
	{
		curDeg = d;
	}


}


