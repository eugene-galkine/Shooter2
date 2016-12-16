package eg.game.world;

import java.util.LinkedList;

import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import eg.game.net.ClientProxy;
import eg.game.world.objects.NetPlayer;
import eg.game.world.objects.interfaces.ICollidable;
import eg.game.world.objects.interfaces.IDrawable;
import eg.game.world.objects.interfaces.IUpdatable;
import eg.game.world.objects.player.Player;

public class World
{
	private static final int NUM_PLAYERS = 8;
	
	private static World instance = null;
	private static ClientProxy client = null;
	
	private LinkedList<ICollidable> solidObjects;
	private NetPlayer netPlayers[];
	private WorldUpdater updater;
	private Player ourPlayer;
	//private Pane pane;
	private WorldDrawer drawer;
	
	public World(GraphicsContext newgc, ClientProxy cp)
	{
		instance = this;
		solidObjects = new LinkedList<ICollidable>();
		updater = new WorldUpdater();
		client = cp;
		netPlayers = new NetPlayer[NUM_PLAYERS];
		drawer = new WorldDrawer(newgc);
		drawer.start();
		new Thread(updater).start();
		client.start();
	}
	
	public static World getInstance()
	{
		return instance;
	}
	
	public static ClientProxy getClient()
	{
		return client;
	}
	
	public void addObject(Object obj)
	{
		//add object with image to the world
		if (obj instanceof IDrawable)
			drawer.add((IDrawable)obj);
		
		//add updatable object to the world
		if (obj instanceof IUpdatable)
			updater.add((IUpdatable)obj);
		
		//add solid object to the world
		if (obj instanceof ICollidable)
			synchronized (solidObjects)
			{
				solidObjects.add((ICollidable)obj);
			}
		
		//this is the player
		if (obj instanceof Player)
			ourPlayer = (Player)obj;
	}
	
	public void removeObject(Object obj)
	{
		//remove object with image from the world
		if (obj instanceof IDrawable)
			drawer.remove((IDrawable)obj);
		
		//remove updatable object from the world
		if (obj instanceof IUpdatable)
			updater.remove((IUpdatable)obj);
		
		//add solid object from the world
		if (obj instanceof ICollidable)
			synchronized (solidObjects)
			{
				solidObjects.remove((ICollidable)obj);
			}
		
		//this is the player
		if (obj instanceof Player)
			System.out.println("ERROR: trying to remove player from world");
	}
	
	public void updateCamera(Player p)
	{
		//move camera over player
		drawer.setOffset(348 - p.getX(), 348 - p.getY());
	}
	
	public boolean checkCollision(Bounds b)
	{
		synchronized (solidObjects)
		{
			//check if this object intersects any that are collidable
			for (ICollidable obj : solidObjects)
				if (b.intersects(obj.getBounds()))
					return true;
			
			return false;
		}
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
				getClient().bulletHit(player.getID(), ownerID, weaponID);
				return true;
			}
		
		//is it hitting this player and not our own bullet?
		if (ourPlayer != null && ownerID != client.getID() && ourPlayer.getBounds().intersects(bulletBounds))
		{
			getClient().bulletHit(getClient().getID(), ownerID, weaponID);
			return true;
		}
		else
			return false;
	}
}
