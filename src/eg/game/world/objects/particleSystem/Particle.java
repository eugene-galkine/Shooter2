package eg.game.world.objects.particleSystem;

import eg.game.state.mpShooter.GameWorld;
import eg.game.world.objects.interfaces.IDrawable;
import eg.game.world.objects.interfaces.IUpdatable;
import javafx.scene.image.Image;

public class Particle extends IUpdatable implements IDrawable
{
	float x, y, mx, my, duration;
	private Image img;
	
	public Particle (float x, float y, float dir, float speed, float timer, float size)
	{
		this.x = x - (size/2);
		this.y = y - (size/2);
		this.mx = (float) (Math.sin(Math.toRadians(dir)) * speed);
		this.my = (float) -(Math.cos(Math.toRadians(dir)) * speed);
		this.duration = timer;
		
		img = new Image("images/particles/explosion.png", size, size, true, false);
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
	public void update(float delta) 
	{
		duration -= delta;
		
		if (duration <= 0)
		{
			GameWorld.getInstance().removeObject(this);
			return;
		}
		
		x += mx * delta;
		y += my * delta;
	}

}
