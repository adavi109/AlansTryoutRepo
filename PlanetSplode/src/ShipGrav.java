
//start auto-imports
import java.awt.event.*;
//end auto-imports

import fang2.core.*;
import fang2.sprites.*;
import fang2.transformers.*;
import fang2.attributes.*;

import java.awt.*;
import java.awt.geom.*;
import java.lang.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 * This example shows a rectangle which can be rotated and moved in all directions.  The box stops when it hits the sides.
 * @author Alan Davis
 */

public class ShipGrav extends Game
{
	StringSprite ship;
	double speedX, speedY;
	double accel, accelX, accelY;
	double angle;
	KeyboardInput kboard;
	Location2D clickLoc, clickTarg;
	private Location2D clicked;
	
	/**sets up the game*/
	public void setup()
	{
		ship = new StringSprite( "H" );
		ship.setColor( getColor( "red" ) );
		ship.setSize( 0.2 );
		ship.setFontFamilyName( "Ships" );
		ship.setLocation( 0.5, 0.5 );
		addSprite( ship );
		kboard = new KeyboardInput();
		canvas.addKeyListener(kboard);
		clickLoc = new Location2D( 0.5, 0.5 );
		clickTarg = clickLoc;
	}
	
	private void turn ( double angle, double timePassed )
	{
		ship.rotateDegrees( angle );
		ship.setRotationDegrees( ship.getRotation() + ( ( angle - ship.getRotation() ) / 2 ) * timePassed );
	}
	
	/**handle input and game events*/
	public void advance( double timePassed )
	{
		kboard.poll();
		

/**gets where the mouse is first pressed down*/
		
		if ( clicked == null && mousePressed() )
		{
			clicked = getMouse2D();
			clickTarg = clicked;
		}
		
		/**clears clicked when the mouse is no longer down*/
		
		if ( clicked != null && !mousePressed() )
		{
			clicked = null;
		}
		
		if ( clickLoc.x != clickTarg.x )
		{
			clickLoc.x = clickLoc.x + (clickTarg.x - clickLoc.x)/2 * (timePassed *2) ;
		}
		if ( clickLoc.y != clickTarg.y )
		{
			clickLoc.y = clickLoc.y + (clickTarg.y - clickLoc.y)/2 * (timePassed * 2) ;
		}
		ship.setLocation(clickLoc);
		// this should go away

			


		
		
	}
	
}