package eg.game.world.objects.interfaces;

public abstract class IUpdatable 
{
	//this is a linked list object in WorldUpdater
	public IUpdatable next = null;
	public IUpdatable prev = null;
	
	abstract public void update(float delta);
}
