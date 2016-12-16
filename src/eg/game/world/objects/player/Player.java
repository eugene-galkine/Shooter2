package eg.game.world.objects.player;

import eg.game.world.World;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class Player extends Person implements EventHandler<Event>
{	
	private float movX, movY;
	private boolean movLeft, movRight, movUp, movDown, isShooting;
	private int movSpeed = 25, health;
	private Weapon weapon;
	
	public Player(int x, int y)
	{
		super(x,y);
		movX = 0;
		movY = 0;
		movLeft = false;
		movRight = false;
		movUp = false;
		movDown = false;
		
		//set my weapon
		weapon = Weapon.pistol;
		isShooting = false;
		health = 100;
	}

	@Override
	public Image getImage()
	{
		return new Image("images/people/player2.png",IMG_WIDTH,IMG_HEIGHT,true,true);
	}

	@Override
	public void handle(Event e)
	{
		if (e instanceof MouseEvent)
		{
			MouseEvent mouse = (MouseEvent)e;
			
			//look at mouse
			setRot(180 + (float)Math.toDegrees(Math.atan2(
					(getX()+IMG_WIDTH/2) - (mouse.getX() - (348 - getX())), 
					(mouse.getY() - (348 - getY())) - (getY()+IMG_HEIGHT/2))));
			
			//mouse event
			switch (mouse.getEventType().getName())
			{
			case "MOUSE_PRESSED":
				if (!weapon.isAuto())
					shoot();
				else
					isShooting = true;
				break;
			case "MOUSE_RELEASED":
				isShooting = false;
				break;
			default:
				break;
			}
		} else if (e instanceof KeyEvent)
		{
			KeyEvent key = (KeyEvent)e;
			boolean pressed = (key.getEventType().getName().equals("KEY_PRESSED"));
			
			boolean changed = false;
			
			//keyboard event
			switch (key.getCode())
			{
			case A:
				if (pressed != movLeft)
					changed = true;
				
				movLeft = pressed;
				break;
			case D:
				if (pressed != movRight)
					changed = true;
				
				movRight = pressed;
				break;
			case S:
				if (pressed != movDown)
					changed = true;
				
				movDown = pressed;
				break;
			case W:
				if (pressed != movUp)
					changed = true;
				
				movUp = pressed;
				break;
			default:
				break;	
			}
			
			//move direction was changed
			if (changed)
			{
				setMoveVect();
			}
		}
	}
	
	private void setMoveVect ()
	{
		//horizontal movement
		if (movLeft)
			movX = -movSpeed;
		else if (movRight)
			movX = movSpeed;
		else
			movX = 0;
		
		//vertical movement
		if (movUp)
			movY = -movSpeed;
		else if (movDown)
			movY = movSpeed;
		else
			movY = 0;
		
		//fix diagonal speed boost
		if (movX != 0 && movY != 0)
		{
			movX = (float)movX * 0.7071f;//sin and cos of 45
			movY = (float)movY * 0.7071f;
		}
	}
	
	@Override
	public void update(float delta) 
	{	
		float oldX = x;
		float oldY = y;
		
		x += movX * delta;
		y += movY * delta;
		
		//update x for collision box
		bounds.setX(x + BOUND_SIZE_DIFF);
		
		//check x collision
		if (World.getInstance().checkCollision(bounds.getBoundsInParent()))
			x = oldX;
		
		//update both x and y for collision box in case x was reverted
		bounds.setX(x + BOUND_SIZE_DIFF);
		bounds.setY(y + BOUND_SIZE_DIFF);
		
		//check y collision
		if (World.getInstance().checkCollision(bounds.getBoundsInParent()))
			y = oldY;
		
		//reset y bounds
		bounds.setY(y + BOUND_SIZE_DIFF);
		
		//tell server about our new position and rotation
		World.getClient().updatePlayerPos((int)x, (int)y, (int)rot);

		//update the camera
		World.getInstance().updateCamera(this);
		
		//shoot gun if needed
		if (isShooting)
			shoot();
	}
	
	private void shoot()
	{
		weapon.shoot(x+IMG_WIDTH/2,y+IMG_HEIGHT/2,rot,World.getClient().getID());
	}

	public void hit(int weaponID) 
	{
		health -= Weapon.getFromID(weaponID).getDamage();
		
		if (health <= 0)
		{
			World.getClient().dead();
		}
	}
}
