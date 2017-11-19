package ThePrap;

//altered new Robot
import robocode.*;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.Robot;
import java.util.Random;
import robocode.ScannedRobotEvent;
import static robocode.util.Utils.normalRelativeAngleDegrees;

import java.awt.*;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * ThePraptinator - a robot by TEAM OOPS
 */
public class ThePraptinator extends Robot
{
	int turnDir = 1;
	int count = 0; 
	double gunTurnAmt; // How much to turn our gun when searching
	String nameOthRob; // Name of the robot we're currently tracking
	int dist =100;
	 int gunturnDirection = 1;
	double prevEner = 100;
	Random generator = new Random();
	/**
	 * run: ThePraptinator's default behavior
	 */
	public void run() {
		setBodyColor(Color.pink);
		setGunColor(Color.black);
		setBulletColor(Color.black);
		
		
		
		
		nameOthRob = null; // Initialize to not tracking anyone
		setAdjustGunForRobotTurn(true); // Keep the gun still when we turn
		gunTurnAmt = 10; // Initialize gunTurn to 10
		
		while (true) 
		{
			// turn the Gun (looks for enemy)
			turnGunRight(gunTurnAmt);
			// Keep track of how long we've been looking
			count++;
			// If we've haven't seen our target for 2 turns, look left
			if (count > 2) {
				gunTurnAmt = -10;
			}
			// If we still haven't seen our target for 5 turns, look right
			if (count > 5) {
				gunTurnAmt = 10;
			}
			// If we *still* haven't seen our target after 10 turns, find another target
			if (count > 11) {
				nameOthRob = null;
			}
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) 
	{	
	double absoluteBearing = getHeading() + e.getBearing();
		double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());

		// If it's close enough, fire!
		if (Math.abs(bearingFromGun) <= 3) {
			turnGunRight(bearingFromGun);
			// We check gun heat here, because calling fire()
			// uses a turn, which could cause us to lose track
			// of the other robot.
			if (getGunHeat() == 0) {
				fire(Math.min(3 - Math.abs(bearingFromGun), getEnergy() - .1));
			}
		} // otherwise just set the gun to turn.
		// Note:  This will have no effect until we call scan()
		else {
			turnGunRight(bearingFromGun);
		}
		// Generates another scan event if we see a robot.
		// We only need to call this if the gun (and therefore radar)
		// are not turning.  Otherwise, scan is called automatically.
		if (bearingFromGun == 0) 
		{
		scan();
		}
		else if(bearingFromGun >= 100)
		{		
		int gen = generator.nextInt(3)+1;
		if(gen ==1)  randomMovementOne();
		if(gen ==2)  randomMovementTwo();
		if(gen ==3)  randomMovementThree();
		}
	}
	public void randomMovementOne()
	{
		ahead(50);
		turnRight(30);
		back(20);
		turnRight(generator.nextInt(40));
		ahead(generator.nextInt(40)+15);
	}
	public void randomMovementTwo()
	{
		turnRight(generator.nextInt(40));
		ahead(generator.nextInt(40)+15);
	}
	public void randomMovementThree()
	{
		back(20);
		turnRight(generator.nextInt(40));
		ahead(generator.nextInt(40)+15);
	}
	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) 
	{
		// Replace the next line with any behavior you would like
		turnRight(normalRelativeAngleDegrees(90 - (getHeading() - e.getHeading())));
	
		ahead(dist);
		dist *= -1;
		
	if (e.getBearing() >= 0) {
			turnDir= 1;
		} else {
			turnDir= -1;
		}
		ahead(generator.nextInt(40)+20);
		scan();
	}
	
	public void onHitRobot(HitRobotEvent e) {
		double turnGunAmt = normalRelativeAngleDegrees(e.getBearing() + getHeading() - getGunHeading());

		turnGunRight(turnGunAmt);
		fire(3);
	}
	public void onHitWall(HitWallEvent e) 
	{
		// Replace the next line with any behavior you would like
		back(20);
		turnDir=-turnDir;
	}	
}
