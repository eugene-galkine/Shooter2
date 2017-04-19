package eg.game.world.objects;

import eg.game.world.objects.player.Person;
import eg.game.world.objects.player.Weapon;
import javafx.scene.image.Image;

public class NetPlayer extends Person
{
	private static final int IMG_WIDTH = 64;
	private static final int IMG_HEIGHT = 64;
	
	private Weapon weapon;
	private int ID;
	
	public NetPlayer(int x, int y, int id) 
	{
		super(x, y);
		
		this.ID = id;
		
		weapon = Weapon.pistol;
	}

	@Override
	public Image getImage()
	{
		return new Image("images/people/player2.png",IMG_WIDTH,IMG_HEIGHT,true,true);
	}

	public void updatePos(int x, int y, int rot)
	{
		this.x = x;
		this.y = y;
		this.rot = rot;
	}
	
	public void setWeapon(int id)
	{
		weapon = Weapon.getFromID(id);
	}
	
	public void shoot(float x, float y, float shootRot) 
	{
		weapon.shootExact(x, y, shootRot, ID);
	}
	
	public int getID()
	{
		return ID;
	}
	
	@Override
	public void update(float delta) 
	{
		
	}
}
