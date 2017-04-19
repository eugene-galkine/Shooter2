package eg.game.world.objects.particleSystem;

import java.util.Random;

import javafx.scene.image.Image;
import eg.game.state.mpShooter.GameWorld;
import eg.game.world.objects.interfaces.IUpdatable;

public class ParticleEmitter extends IUpdatable 
{
	private float emitDuration, minDuration, maxDuration, minDir, maxDir, minSpeed, maxSpeed, minSize, maxSize, x, y, dir;
	private int minObjs, maxObjs;
	private Image img[];
	private Random r;
	
	public ParticleEmitter (int minObjs, int maxObjs, float emitDuration, float minDuration, float maxDuration, float minDir, float maxDir,
			float minSpeed, float maxSpeed, float minSize, float maxSize, float x, float y, float dir, Image img[])
	{
		this.minObjs = minObjs;
		this.maxObjs = maxObjs;
		this.emitDuration = emitDuration;
		this.minDuration = minDuration;
		this.maxDuration = maxDuration;
		this.minDir = minDir;
		this.maxDir = maxDir;
		this.minSpeed = minSpeed;
		this.maxSpeed = maxSpeed;
		this.minSize = minSize;
		this.maxSize = maxSize;
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.img = img;
		
		r = new Random();
		
		if (emitDuration <= 0)
			emit();
	}
	
	@Override
	public void update(float delta)
	{
		if (emitDuration <= 0)
			GameWorld.getInstance().removeObject(this);
		else
		{
			emit();
			emitDuration -= delta;
		}
	}
	
	private void emit()
	{
		int count = minObjs + r.nextInt(maxObjs - minObjs);
		
		for (int i = 0; i < count; i++)
			GameWorld.getInstance().addObject(new Particle(x, y, dir + minDir + (r.nextFloat() * (maxDir - minDir)), minSpeed  + (r.nextFloat() * (maxSpeed - minSpeed)), minDuration + (r.nextFloat() * (maxDuration - minDuration)), minSize + (r.nextFloat() * (maxSize - minSize)), img[r.nextInt(img.length)]));
	}
}
