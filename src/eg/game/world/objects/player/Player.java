package eg.game.world.objects.player;

import eg.game.Main;
import eg.game.state.mpShooter.GameWorld;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import eg.game.Control;

public class Player extends Person implements EventHandler<Event>
{
	private static final int MOVE_SPEED = 25;

	private float movX, movY;
	private boolean movLeft, movRight, movUp, movDown, isShooting, throwReleased;
	private int health;
	private final Weapon weapon;
	//private int ammo;
	
	public Player(int x, int y)
	{
		super(x,y);
		movX = 0;
		movY = 0;
		movLeft = false;
		movRight = false;
		movUp = false;
		movDown = false;
		
		throwReleased = true;
		
		//set my weapon
		weapon = Weapon.pistol;
		//ammo = weapon.getMaxAmmo();
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
					(getX()+IMG_WIDTH/2f) - (mouse.getX() - (Main.MAGIC_NUM - getX())),
					(mouse.getY() - (Main.MAGIC_NUM - getY())) - (getY()+IMG_HEIGHT/2f))));
			
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
			if (key.getCode() == Control.Leftward.getCode())
			{
				if (pressed != movLeft)
					changed = true;
				
				movLeft = pressed;
			} else if (key.getCode() == Control.Rightward.getCode())
			{
				if (pressed != movRight)
					changed = true;
				
				movRight = pressed;
			} else if (key.getCode() == Control.Backward.getCode())
			{
				if (pressed != movDown)
					changed = true;
				
				movDown = pressed;
			} else if (key.getCode() == Control.Forward.getCode())
			{
				if (pressed != movUp)
					changed = true;
				
				movUp = pressed;
			} else if (key.getCode() == Control.Gernade.getCode())
			{
				if (throwReleased)
				{
					GameWorld.getClient().throwGernade(x,y,getRot());
					throwReleased = false;
				}
				else if (!pressed)
					throwReleased = true;
			} else if (key.getCode() == Control.Reload.getCode())
			{
				//TODO
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
			movX = -MOVE_SPEED;
		else if (movRight)
			movX = MOVE_SPEED;
		else
			movX = 0;
		
		//vertical movement
		if (movUp)
			movY = -MOVE_SPEED;
		else if (movDown)
			movY = MOVE_SPEED;
		else
			movY = 0;
		
		//fix diagonal speed boost
		if (movX != 0 && movY != 0)
		{
			movX = movX * 0.7071f;//sin and cos of 45
			movY = movY * 0.7071f;
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
		if (GameWorld.getInstance().checkCollision(bounds.getBoundsInParent()) != null)
			x = oldX;
		
		//update both x and y for collision box in case x was reverted
		bounds.setX(x + BOUND_SIZE_DIFF);
		bounds.setY(y + BOUND_SIZE_DIFF);
		
		//check y collision
		if (GameWorld.getInstance().checkCollision(bounds.getBoundsInParent()) != null)
			y = oldY;
		
		//reset y bounds
		bounds.setY(y + BOUND_SIZE_DIFF);
		
		//tell server about our new position and rotation
		GameWorld.getClient().updatePlayerPos((int)x, (int)y, (int)rot);
		
		//update the camera
		GameWorld.getInstance().updateCamera(Main.MAGIC_NUM - x, Main.MAGIC_NUM - y);
		
		//shoot gun if needed
		if (isShooting)
			shoot();
	}
	
	private void shoot()
	{
		weapon.shoot(x+IMG_WIDTH/2f,y+IMG_HEIGHT/2f,rot,GameWorld.getClient().getID());
	}

	public void hit(int weaponID) 
	{
		Weapon weapon = Weapon.getFromID(weaponID);
		if (weapon != null)
			takeDamage(weapon.getDamage());
		else
			System.out.println("Weapon for id=" + weaponID + " is null");
	}
	
	public void takeDamage(int damage)
	{
		health -= damage;
		System.out.println(health);
		
		if (health <= 0)
		{
			GameWorld.getClient().dead();
		}
	}

	public void spawn(int x, int y, int health) 
	{
		this.x = x;
		this.y = y;
		this.health = health;
		
		bounds.setX(x + BOUND_SIZE_DIFF);
		bounds.setY(y + BOUND_SIZE_DIFF);
	}
	
	public void reload()
	{
		
	}
}
