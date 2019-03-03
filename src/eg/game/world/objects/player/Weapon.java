package eg.game.world.objects.player;

import java.util.Random;

import eg.game.state.mpShooter.GameWorld;
import eg.game.world.objects.Bullet;

public class Weapon 
{
	public static final Weapon pistol = new Weapon("pistol", 0, 10, false, 100, 6, 100);

	private static final Weapon[] list =
			{
					pistol
			};
	
	public static Weapon getFromID(int id)
	{
		for (Weapon w : list)
			if (w.getID() == id)
				return w;
		
		return null;
	}

	private final String name;
	private final boolean automatic;
	private final int shootDelay;
	private final int inaccuracy;
	private final int weaponID;
	private final float bulletSpeed;
	private final int damage;
	
	private long shootTimer;
	private final Random r;
	
	private Weapon(String name, int weaponID, int damage, boolean auto, int delay, int inaccuracy, int bulletSpeed)
	{
		this.name = name;
		this.automatic = auto;
		this.shootDelay = delay;
		this.inaccuracy = inaccuracy;
		this.weaponID = weaponID;
		this.bulletSpeed = bulletSpeed;
		this.damage = damage;
		
		shootTimer = 0;
		r = new Random();
	}

	boolean isAuto()
	{
		return automatic;
	}
	
	private int getID() 
	{
		return weaponID;
	}
	
	public int getDamage() 
	{
		return damage;
	}
	
	void shoot(float x, float y, float rot, int ID)
	{
		//don't shoot too soon
		if (System.currentTimeMillis() - shootTimer < shootDelay)
			return;
		
		//update timer
		shootTimer = System.currentTimeMillis();
		
		//shoot a bullet
		Bullet b = new Bullet(x,y,weaponID,rot + (r.nextInt(inaccuracy) - (inaccuracy/2f)),bulletSpeed, ID);
		GameWorld.getInstance().addObject(b);
		
		GameWorld.getClient().shoot(x,y,b.getRot());
	}
	
	public void shootExact(float x, float y, float rot, int ID)
	{
		//don't shoot too soon
		if (System.currentTimeMillis() - shootTimer < shootDelay)
			return;
		
		//update timer
		shootTimer = System.currentTimeMillis();
		
		//shoot a bullet
		Bullet b = new Bullet(x,y,weaponID,rot,bulletSpeed, ID);
		GameWorld.getInstance().addObject(b);
		
		GameWorld.getClient().shoot(x,y,b.getRot());
	}
}
