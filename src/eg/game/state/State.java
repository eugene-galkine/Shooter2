package eg.game.state;

import eg.game.Main;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;

public abstract class State
{
	protected GraphicsContext graphicsContext;
	protected Scene scene;
	protected Main parent;
	
	public State (GraphicsContext gc, Scene newScene, Main newParent)
	{
		graphicsContext = gc;
		scene = newScene;
		parent = newParent;
	}
	
	public abstract void start ();
}
