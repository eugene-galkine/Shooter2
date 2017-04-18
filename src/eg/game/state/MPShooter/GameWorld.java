package eg.game.state.mpShooter;

import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import eg.game.net.ClientProxy;
import eg.game.world.World;
import eg.game.world.objects.Gernade;
import eg.game.world.objects.NetPlayer;
import eg.game.world.objects.player.Person;
import eg.game.world.objects.player.Player;

public class GameWorld extends World
{
	private static GameWorld instance = null;
	private static ClientProxy client = null;
	private static final int NUM_PLAYERS = 8;
	
	private NetPlayer netPlayers[];
	private Player ourPlayer;
	private Object playerLock;
	
	public GameWorld(GraphicsContext newgc, ClientProxy cp) 
	{
		super(newgc);
		instance = this;
		ourPlayer = null;
		netPlayers = new NetPlayer[NUM_PLAYERS];
		client = cp;
		client.start();
	}

	public static GameWorld getInstance()
	{
		return instance;
	}
	
	public static ClientProxy getClient()
	{
		return client;
	}
	
	@Override
	public void addObject(Object obj)
	{
		super.addObject(obj);
		
		//this is the player
		if (obj instanceof Player)
		{
			if (playerLock == null)
				playerLock = new Object();
			
			ourPlayer = (Player)obj;
			synchronized (playerLock)
			{
				playerLock.notify();
			}
		}
	}
	
	@Override
	public void removeObject(Object obj)
	{
		super.removeObject(obj);
		
		//this is the player
		if (obj instanceof Player)
			System.out.println("ERROR: trying to remove player from world");
	}
	
	public void newPlayer(int ID)
	{
		//new player joined
		netPlayers[ID] = new NetPlayer(0, 0, ID);
		
		addObject(netPlayers[ID]);
	}
	
	public void removePlayer(int ID) 
	{
		//remove a network player
		removeObject(netPlayers[ID]);
		netPlayers[ID] = null;
	}
	
	public void updatePlayer(int id, int x, int y, int rot) 
	{
		//new network player joined
		if (netPlayers[id] != null)
			netPlayers[id].updatePos(x, y, rot);
		else
			System.out.println("Missing player with ID: " + id);
	}

	public void netShoot(int id, int shootRot) 
	{
		//someone else shot
		if (netPlayers[id] != null)
			netPlayers[id].shoot(shootRot);
		else
			System.out.println("Missing player with ID: " + id);
	}

	public boolean bulletCollision(Bounds bulletBounds, int ownerID, int weaponID) 
	{
		//test bullet collision on network players
		for (NetPlayer player : netPlayers)
			if (player != null && player.getID() != ownerID && player.getBounds().intersects(bulletBounds))
			{
				//getClient().bulletHit(player.getID(), ownerID, weaponID);
				return true;
			}
		
		//is it hitting this player and not our own bullet?
		if (ourPlayer != null && ownerID != client.getID() && ourPlayer.getBounds().intersects(bulletBounds))
		{
			ourPlayer.hit(weaponID);
			getClient().bulletHit(getClient().getID(), ownerID, weaponID);
			return true;
		}
		else
			return false;
	}

	public void spawnPlayer(int x, int y, int health) 
	{
		//spawn player if he exists
		if (ourPlayer != null)
			ourPlayer.spawn(x,y,health);
		else
		{
			//wait for the player to exist and then spawn him
			new Thread(new Runnable() 
			{
				@Override
				public void run() 
				{
					//initiate the lock object if it needs to be (will always need to be unless I add something else that used this object)
					if (playerLock == null)
						playerLock = new Object();
						
					while (ourPlayer == null)
						try 
						{
							synchronized (playerLock)
							{
								playerLock.wait();
							}
						} catch (InterruptedException e) {}
					
					//finally spawn when ready
					ourPlayer.spawn(x,y,health);
				}
			}).start();
		}
	}
	
	public void throwGernade(int id, int x, int y, int rot) 
	{
		GameWorld.getInstance().addObject(new Gernade(x+(Person.IMG_WIDTH/2), y+Person.IMG_HEIGHT/2, rot, id, 50));
	}
}
