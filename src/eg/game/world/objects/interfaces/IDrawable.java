package eg.game.world.objects.interfaces;

import javafx.scene.image.Image;

public interface IDrawable
{
	Image getImg();
	float getX();
	float getY();
	
	default float getRot()
	{
		return 0;
	}
	
	default float getWidth()
	{
		return (float) getImg().getWidth();
	}
	
	default float getHeight()
	{
		return (float) getImg().getHeight();
	}
}
