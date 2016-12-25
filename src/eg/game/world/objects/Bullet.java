package eg.game.world.objects;

import eg.game.state.MpShooter.GameWorld;
import eg.game.world.objects.interfaces.IDrawable;
import eg.game.world.objects.interfaces.IUpdatable;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Bullet extends IUpdatable implements IDrawable
{
	private static final int IMG_WIDTH = 8;
	private static final int IMG_HEIGHT = 8;
	
	private float x, y;
	private float rot;
	private Image img;
	private float movX, movY, movSpeed;
	private Rectangle r;
	private int ownerID, weaponID;
	
	public Bullet(float inX, float inY, int inwid, float inRot, float inSpeed, int owner)
	{
		this.x = inX - IMG_WIDTH/2;
		this.y = inY - IMG_HEIGHT/2;
		this.rot = inRot;
		this.movSpeed = inSpeed;
		this.ownerID = owner;
		this.weaponID = inwid;
		
		//setup image
		img = getImage();
		
		//Calculate how much to move in each direction each frame
		movX = (float) Math.sin(Math.toRadians(rot)) * movSpeed;
		movY = (float) -Math.cos(Math.toRadians(rot)) * movSpeed;
		
		//set up bounds
		r = new Rectangle(x,y,IMG_WIDTH,IMG_HEIGHT);
	}
	
	public Image getImage()
	{
		return new Image("images/bullets/bullet.png",IMG_WIDTH,IMG_HEIGHT,true,true);
	}
	
	@Override
	public Image getImg() 
	{
		return img;
	}

	@Override
	public float getX() 
	{
		return x;
	}

	@Override
	public float getY() 
	{
		return y;
	}
	
	@Override
	public float getRot()
	{
		return rot;
	}
	
	@Override
	public void update(float delta) 
	{
		//update position based on how long since the last frame
		x += delta*movX;
		y += delta*movY;
		
		//remove bullet if it goes too far
		if (x > 2000 || x < -2000 || y > 2000 || y < -2000)
			GameWorld.getInstance().removeObject(this);
		
		//check collision
		r.setX(x);
		r.setY(y);
		if (GameWorld.getInstance().checkCollision(r.getBoundsInLocal()))
			GameWorld.getInstance().removeObject(this);
		
		//TODO collision with players & fix concurrent in collision inside world
		if (GameWorld.getInstance().bulletCollision(r.getBoundsInLocal(), ownerID, weaponID))
			GameWorld.getInstance().removeObject(this);
	}
}
