package eg.game.world;

import eg.game.world.objects.interfaces.IUpdatable;

class WorldUpdater implements Runnable
{
	//using a classic linked list instead of java container one because we can avoid concurrent errors this way
	private IUpdatable listHead;
	private IUpdatable listTail;

	public WorldUpdater()
	{
		listHead = null;
		listTail = null;
	}
	
	public void add(IUpdatable obj) 
	{
		//if list is empty make a new list, otherwise append to the end
		if (listHead == null)
		{
			listHead = listTail = obj;
		} else
		{
			listTail.next = obj;
			obj.prev = listTail;
			listTail = obj;
		}
	}

	public void remove(IUpdatable obj) 
	{	
		if (listHead != obj && listTail != obj)
		{
			//middle of list
			obj.prev.next = obj.next;
			obj.next.prev = obj.prev;
		} else if (listHead != obj)
		{
			//the end of the list
			obj.prev.next = null;
			listTail = obj.prev;
		} else if (listTail != obj)
		{
			//the start of the list
			listHead = obj.next;
			listHead.prev = null;
		} else //(listHead == obj && listTail == obj)
		{
			//the only one on the list
			listHead = listTail = null;
		}
	}
	
	@Override
	public void run() 
	{
		long lastUpdate = System.currentTimeMillis();
		
		//run FOREVER!! (unless killed by System.exit(0) in Main)
		while (true)
		{	
			//calculate delta time
			float delta = (System.currentTimeMillis() - lastUpdate) / 100f;
			lastUpdate = System.currentTimeMillis();
			
			//first object on linked list
			IUpdatable obj = listHead;
			
			//iterate thru linked list and update every object
			while (obj != null)
			{
				obj.update(delta);
				obj = obj.next;
			}
			
			//wait 3 milliseconds
			try {
				synchronized(WorldUpdater.this) {
					wait(3);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
