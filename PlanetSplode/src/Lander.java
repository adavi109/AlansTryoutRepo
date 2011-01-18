
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

public class Lander extends Game
{
	StringSprite ship;
	double speedX, speedY;
	double accel, accelX, accelY;
	double thrust, thrustx, thrusty;
	double thrustAngle;
	double accelAngle;
	KeyboardInput kboard;
	
	/**sets up the game*/
	public void setup()
	{
		speedX = 0;
		speedY = 0;
		accelX = 0;
		accelY = 0;
		accel = 0;
		accelAngle = 0;
		thrustAngle = 0;
		ship = new StringSprite( "H" );
		ship.setColor( getColor( "red" ) );
		ship.setSize( 0.2 );
		ship.setFontFamilyName( "Ships" );
		ship.setLocation( 0.5, 0.5 );
		addSprite( ship );
		kboard = new KeyboardInput();
		canvas.addKeyListener(kboard);
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
		
		if (kboard.keyDown(KeyEvent.VK_A))
		{
			accelAngle = accelAngle - 1;
		}
		
		if (kboard.keyDown(KeyEvent.VK_D))
		{
			accelAngle = accelAngle + 1;
		}
		
		if (kboard.keyDown(KeyEvent.VK_RIGHT))
		{
			accelAngle = accelAngle + 1;
		}
		
		if (kboard.keyDown(KeyEvent.VK_LEFT))
		{
			accelAngle = accelAngle - 1;
		}
		
		if (kboard.keyDown(KeyEvent.VK_DOWN))
		{}
		
		/**This accelerates the ship*/
		if (kboard.keyDown(KeyEvent.VK_UP) || kboard.keyDown(KeyEvent.VK_W) )
		{
			accel = 0.6;
		}
		
		else
			accel = 0;
			
		/**This stops the ship when it hits the edge of the screen in free fall*/
		if ( ( ship.getX() >= 1.0 && speedX >= 0.1 ) || ( ship.getX() <= 0 && speedX <= -0.1 ) )
		{
			speedX = 0;
		}
		
		/**This stops the ship when it hits the edge of the screen in free fall*/
		if ( ( ship.getY() >= 1.0 && speedY >= 0.1 ) || ( ship.getY() <= 0 && speedY <= -0.1 ) )
		{
			accelY = 0;
			speedY = 0;
		}
		
		accelX = accel * Math.sin( ship.getRotation() );
		
		accelY = accel * Math.cos( ship.getRotation() );
		
		speedY = speedY + -accelY * timePassed;
		
		speedX = speedX + accelX * timePassed;
		
		ship.rotateDegrees( accelAngle );
		
		ship.translate( speedX * timePassed, speedY * timePassed );
	}
	
}