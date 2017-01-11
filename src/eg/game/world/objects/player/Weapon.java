package eg.game.world.objects.player;

import java.util.Random;

import eg.game.state.mpShooter.GameWorld;
import eg.game.world.objects.Bullet;

public class Weapon 
{
	public static final Weapon pistol = new Weapon("pistol", 0, 10, false, 100, 6, 100, 15);
	
	private static final Weapon list[] =
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
	private final int maxAmmo;
	
	private long shootTimer;
	private Random r;
	
	private Weapon(String name, int weaponID, int damage, boolean auto, int delay, int inaccuracy, int bulletSpeed, int maxAmmo)
	{
		this.name = name;
		this.automatic = auto;
		this.shootDelay = delay;
		this.inaccuracy = inaccuracy;
		this.weaponID = weaponID;
		this.bulletSpeed = bulletSpeed;
		this.damage = damage;
		this.maxAmmo = maxAmmo;
		
		shootTimer = 0;
		r = new Random();
	}

	public boolean isAuto()
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
	
	public int getMaxAmmo()
	{
		return maxAmmo;
	}
	
	public void shoot(float x, float y, float rot, int ID)
	{
		//don't shoot too soon
		if (System.currentTimeMillis() - shootTimer < shootDelay)
			return;
		
		//update timer
		shootTimer = System.currentTimeMillis();
		
		//shoot a bullet
		Bullet b = new Bullet(x,y,weaponID,rot + (r.nextInt(inaccuracy) - (inaccuracy/2)),bulletSpeed, ID);
		GameWorld.getInstance().addObject(b);
		
		GameWorld.getClient().shoot((int)b.getRot());
	}
}
