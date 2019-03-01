package eg.game.world.objects.particleSystem;

import javafx.scene.image.Image;
import eg.game.state.mpShooter.GameWorld;

public class ParticleEmitterFactory
{
	public static final ParticleEmitterFactory Explosion = new ParticleEmitterFactory(3, 6, 3, 4, 6, 0, 360, 6, 17, 20, 80, "explosion");
	public static final ParticleEmitterFactory Blood = new ParticleEmitterFactory(6, 10, 0, 10, 18, -30, 30, 3, 8, 4, 10, "blood");
	
	private float emitDuration, minDuration, maxDuration, minDir, maxDir, minSpeed, maxSpeed, minSize, maxSize;
	private int minObjs, maxObjs;
	private Image img[];
	
	private ParticleEmitterFactory (int minObjs, int maxObjs, float emitDuration, float minDuration, float maxDuration, float minDir, float maxDir,
			float minSpeed, float maxSpeed, float minSize, float maxSize, String imageName)
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
		
		float sizeDif = maxSize - minSize;
		sizeDif /= 12f;
		img = new Image[12];
		
		//images are loaded here to avoid loading at runtime which causes lag
		for (int i = 0; i < img.length; i++, minSize += sizeDif)
			img[i] = new Image("images/particles/" + imageName + ".png", minSize, minSize, true, false);
	}
	
	public void create(float x, float y, float dir)
	{
		GameWorld.getInstance().addObject(new ParticleEmitter(
				minObjs, maxObjs, emitDuration, minDuration, maxDuration, minDir, maxDir, minSpeed, maxSpeed, minSize, maxSize, x, y, dir, img));
	}
}
