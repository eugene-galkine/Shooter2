package eg.game.state;

import eg.game.Main;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;

public abstract class State
{
	private final GraphicsContext graphicsContext;
	protected final Scene scene;
	private final Main parent;
	
	protected State(GraphicsContext gc, Scene newScene, Main newParent)
	{
		graphicsContext = gc;
		scene = newScene;
		parent = newParent;
	}
	
	public abstract void start ();
}
