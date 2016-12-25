package eg.game.world.objects.interfaces;

import javafx.scene.image.Image;

public interface IDrawable
{
	public Image getImg();
	public float getX();
	public float getY();
	public default float getRot()
	{
		return 0;
	}
	
	public default float getWidth()
	{
		return (float) getImg().getWidth();
	}
	
	public default float getHeight()
	{
		return (float) getImg().getHeight();
	}
}
