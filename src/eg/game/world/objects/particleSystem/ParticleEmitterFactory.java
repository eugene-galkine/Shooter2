package eg.game.world.objects.particleSystem;

import eg.game.state.mpShooter.GameWorld;

public class ParticleEmitterFactory
{
	public static final ParticleEmitterFactory Explosion = new ParticleEmitterFactory(5, 10, 10, 4, 8, 0, 360, 2, 10, 20, 80);
	
	float emitDuration, minDuration, maxDuration, minDir, maxDir, minSpeed, maxSpeed, minSize, maxSize;
	int minObjs, maxObjs;
	
	private ParticleEmitterFactory (int minObjs, int maxObjs, float emitDuration, float minDuration, float maxDuration, float minDir, float maxDir,
			float minSpeed, float maxSpeed, float minSize, float maxSize)
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
	}
	
	public void create(float x, float y, float dir)
	{
		GameWorld.getInstance().addObject(new ParticleEmitter(
				minObjs, maxObjs, emitDuration, minDuration, maxDuration, minDir, maxDir, minSpeed, maxSpeed, minSize, maxSize, x, y, dir));
	}
}
