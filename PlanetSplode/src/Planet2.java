//start auto-imports
import com.sun.org.apache.bcel.internal.generic.*;
//end auto-imports

import fang2.core.*;
import fang2.sprites.*;
import fang2.transformers.*;
import fang2.attributes.*;
import java.awt.*;
import java.awt.geom.*;
import java.math.*;

/**
 * A moon rotating around a planet.
 * @author Alan Davis
 */

public class Planet2 extends Game
{
	Person p, p2;
	Sprite planet;
	Sprite moonSprite, moonPoint;
	Vector2D facing;
	StringSprite board , distMeter;
	CompositeSprite moonOrbit, planetOrbit, space, moon;
	private double distance, target;
	private Location2D clickLoc, clickTarg;
	private Location2D visibleLocation;
	private Location2D clicked;
	
	/**sets up the game*/
	public void setup()
	{
		planet = new OvalSprite( .3, .3 );
		planet.setColor( getColor( "green" ) );
		
		moonSprite = new OvalSprite( .1, .1 );
		moonSprite.setColor( getColor( "gray" ) );
		moonPoint = new RectangleSprite( .05, .08 );
		
		p2 = new Person();
		p2.setPlanet( moonSprite );
		moon = new CompositeSprite();
		moon.setLocation( -.4, 0 );
		moon.addSprite( moonSprite, moonPoint, p2.getPerson() );
		
		
		moonOrbit = new CompositeSprite();
		moonOrbit.addSprite( moon );
		moonOrbit.addTransformer( new SpinTransformer( 10 ) );
		moon.addTransformer( new SpinTransformer( -25 ) );
		
		p = new Person();
		p.setPlanet( planet );
		
		
		planetOrbit = new CompositeSprite();
		planetOrbit.addSprite( planet, new RectangleSprite( .1, .2 ), p.getPerson() );
		planetOrbit.addTransformer( new SpinTransformer( 5 ) );
		
		space = new CompositeSprite();
		space.setLocation( .5, .5 );
		space.addSprite( moonOrbit, planetOrbit );
		//space.addTransformer( new SpinTransformer( 34 ) );
		
		setupUI();
		setupMap();
		addSprite( space );
	}
	
	/**Scales space to the size dictated by the distance away from the camera*/
	private void zoom( double dist, double targ )
	{
		space.setScale( ( ( ( 0.5 / dist ) ) * 5 ) / 2 );
	}
	
	/**Important things having to do with the map*/
	private void setupMap()
	{
	
		distance = 1.0;
		target = 1.0;
		clickLoc = new Location2D( 0.5, 0.5 );
		clickTarg = clickLoc;
		
		/**This is used when moving the map around I think*/
		Box2D bounds = space.getBounds2D();
		visibleLocation = bounds.getCenter();
	}
	
	/**currently a tumor*/
	private void setupUI()
	{
	
		//This displays a string at the bottom for distance from the planet
		distMeter = new StringSprite( "X Pos: 0" );
		distMeter.setSize( 0.2 );
		distMeter.setLocation( 0.5, 0.9 );
		
		//this is the current angle of the object at the top of the screen
		board = new StringSprite( "Y Pos: 0" );
		board.setSize( 0.2 );
		board.setLocation( 0.5, 0.1 );
		
		addSprite( board, distMeter );
	}
	
	/**handle input and game events*/
	public void advance( double timePassed )
	{
		moonOrbit.rotateDegrees(.05);
		moon.rotateDegrees(.1);
		
		
		distMeter.setText( "X Pos: " + p.getPerson().getX() );
		board.setText( "Y Pos: " + p.getPerson().getY() );
		
		if ( getKeyPressed() == '+' )
		{
			target = target - .08;
			clickLoc = getClick2D();
		}
		
		if ( getKeyPressed() == '-' )
		{
			target = target + .08;
			clickLoc = getClick2D();
		}
		
		/**This operation lets me reset the scene*/
		if ( rightPressed() )
		{
			p.walkRight();
			p2.walkLeft();
		}
		
		if ( leftPressed() )
		{
			p.walkLeft();
			p2.walkRight();
		}
		
		if ( upPressed() )
		{
			p.stand();
			p2.stand();
		}
		
		/**This statement declares that the distance from the camera does not equal the
		***target, make the distance to the camera equal half the distance to the target.
		***This makes it so that no matter how far the person zooms in,
		*** the camera will always zoom smoothly toward the target... forever
		*/
		if ( distance != target )
		{
			distance = distance - ( ( ( distance - target ) / 2 ) * timePassed );
		}
		
		zoom( distance, target );
		
		
		
		
		
		/**gets where the mouse is first pressed down*/
		
		if ( clicked == null && mousePressed() )
		{
			clicked = getMouse2D();
			
		}
		
		/**clears clicked when the mouse is no longer down*/
		
		if ( clicked != null && !mousePressed() )
		{
			clicked = null;
		}
		
		/**mouse is being dragged*/
		
		if ( clicked != null && mousePressed() )
		{
			Location2D position = getMouse2D();
			Location2D translate = new Location2D(
			                           position.x - clicked.x,
			                           position.y - clicked.y );
			Box2D bounds = space.getBounds2D();
			
			/**below if statements keep the sprite on screen
			
			if ( bounds.getMinX() + translate.x > 0 )
				translate.x = -bounds.getMinX();
				
			if ( bounds.getMaxX() + translate.x < 1 )
				translate.x = 1 - bounds.getMaxX();
				
			if ( bounds.getMinY() + translate.y > 0 )
				translate.y = -bounds.getMinY();
				
			if ( bounds.getMaxY() + translate.y < 1 )
				translate.y = 1 - bounds.getMaxY();
				*/
			space.translate(
			    translate.x,
			    translate.y );
			    
			    
			clicked = position;
			
			visibleLocation.x -= translate.x;
			
			visibleLocation.y -= translate.y;
			
			
		}
		
		/**This is a tumor
		if ( getMouse2D().intersects( p.getPerson() ) )
		{
			space.setLocation( p.getPerson().getX() + .5, p.getPerson().getY() + 5 );
	}
		**/
	}
	
	public class Person
	{
		StringSprite letter;
		CompositeSprite ground;
		WalkBackward wlkbk;
		StandAnim std;
		WalkForward wlkfor;
		
		public Person()
		{
			letter = new StringSprite( "A" );
			letter.setFontFamilyName( "Dude" );
			letter.setLocation( 0, -.165 );
			letter.setSize( .05 );
			letter.setColor( getColor( "red" ) );
			ground = new CompositeSprite();
			ground.addSprite( letter );
			std = new StandAnim();
			wlkbk = new WalkBackward();
			wlkfor = new WalkForward();
		}
		
		public Sprite getPerson()
		{
			return ground;
		}
		
		public void setPlanet( Sprite planet )
		{
			letter.setLocation( 0, -( planet.getSize() / 2 ) - letter.getHeight() / 2 );
		}
		
		public void walkRight()
		{
			letter.setText( "" + 'A' );
			cancelAlarm( wlkfor );
			cancelAlarm( wlkbk );
			cancelAlarm( std );
			scheduleRelative( wlkfor, 0.2 );
		}
		
		public void walkLeft()
		{
			letter.setText( "" + 'A' );
			cancelAlarm( wlkfor );
			cancelAlarm( wlkbk );
			cancelAlarm( std );
			scheduleRelative( wlkbk, 0.2 );
		}
		
		public void stand()
		{
			letter.setText( "" + 'A' );
			cancelAlarm( wlkfor );
			cancelAlarm( wlkbk );
			cancelAlarm( std );
			scheduleRelative( std, 0.2 );
		}
		
		class WalkForward implements Alarm
		{
			public void act()
			{
				char spot = letter.getText().charAt( 0 );
				spot++;
				
				if ( spot == 'E' )
					letter.setText( "A" );
				else
					letter.setText( "" + spot );
					
				ground.rotate( .05 );
				
				scheduleRelative( this, 0.2 );
			}
		}
		
		class WalkBackward implements Alarm
		{
			public void act()
			{
				char spot = letter.getText().charAt( 0 );
				spot++;
				
				if ( spot == 'E' )
					letter.setText( "A" );
				else
					letter.setText( "" + spot );
					
				ground.rotate( -.05 );
				
				scheduleRelative( this, 0.2 );
			}
		}
		
		class StandAnim implements Alarm
		{
			public void act()
			{
				char spot = letter.getText().charAt( 0 );
				spot++;
				
				if ( spot == 'B' )
					letter.setText( "A" );
				else
					letter.setText( "" + spot );
					
				ground.rotate( .0 );
				
				scheduleRelative( this, 0.2 );
			}
		}
	}
}
