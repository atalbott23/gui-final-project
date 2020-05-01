package finalProject;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;



public class Peg extends Circle
{
	Color bottomSide;
	boolean found = false;
	String bottomName;
	double degAdd;
	double curDeg;
	public Peg(Color c)
	{
		super(15);
		setFill(Color.GHOSTWHITE);
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
		else if(c == Color.DEEPPINK)
		{
			bottomName = "Pink";
		}
		
		degAdd = 0;

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

	public String getColor()
	{
		return bottomName;
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
	
	public double findNewX(double x, double r)
	{
		
		double y = Math.toRadians(x);
		double z = Math.cos(y) * r;
		
		return z;
	}
	
	public double findNewY(double x, double r)
	{
		
		double y = Math.toRadians(x);
		double z = Math.sin(y) * r;
		
		return z;
	}
	
//	public double findDegX(double x, double r)
//	{
//		
//		double a = Math.acos(x/r);
//		double deg = Math.toDegrees(a);
//		System.out.println(x + "|" + a + "|" + deg);
//		
//		return deg;
//	}
//	
//	public double findDegY(double x, double r)
//	{
//		double a = Math.asin(x/r);
//		double deg = Math.toDegrees(a);
//		
//		return deg;
//	}
	
	public void setDeg(double d)
	{
		curDeg = d;
	}


}

