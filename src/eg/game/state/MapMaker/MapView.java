package eg.game.state.MapMaker;

import eg.game.Main;
import eg.game.world.objects.Wall;
import eg.game.world.objects.interfaces.IUpdatable;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class MapView extends IUpdatable implements EventHandler<Event>
{
	private float x, y, scrolX, scrolY, mx, my, placemx, placemy;
	private Wall placingWall;
	
	public MapView()
	{
		x = 0;
		y = 0;
		placingWall = null;
	}
	
	@Override
	public void handle(Event e) 
	{
		if (e instanceof MouseEvent)
		{
			MouseEvent mouse = (MouseEvent)e;
			
			switch (mouse.getEventType().getName())
			{
			case "MOUSE_DRAGGED":
				mx = -(int)mouse.getX();
				my = Main.WINDOW_HEIGHT - (int)mouse.getY();
				
				//scroll if we are holding mb1
				if (mouse.getButton() == MouseButton.SECONDARY)
				{
					x -= scrolX - mx;
					y -= scrolY - my;
					scrolX = mx;
					scrolY = my;
				} else if (mouse.getButton() == MouseButton.PRIMARY)
				{
					//resize the object we are placing if holding mb2
					placingWall.setWidth(placemx - mx);
					placingWall.setHeight(placemy - my);
				}
					
				break;
			case "MOUSE_PRESSED":
				if (mouse.getButton() == MouseButton.SECONDARY)
				{
					//start scrolling if we pressed mb1
					scrolX = -(int)mouse.getX();
					scrolY = Main.WINDOW_HEIGHT - (int)mouse.getY();
				} else if (mouse.getButton() == MouseButton.PRIMARY)
				{
					//place a wall if we pressed mb2
					placingWall = new Wall((int)x + (int)mouse.getX(), (int)y + (int)mouse.getY(), 1, 1);
					MapMakerWorld.getInstance().addObject(placingWall);
					placemx = -(int)mouse.getX();
					placemy = Main.WINDOW_HEIGHT - (int)mouse.getY();
				}
				
				break;
			case "MOUSE_RELEASED":
				break;
			default:
				break;
			}
		} else if (e instanceof KeyEvent)
		{
			KeyEvent key = (KeyEvent)e;
			
			if (!key.getEventType().getName().equals("KEY_PRESSED"))
				return;
			
			switch (key.getCode())
			{
			case Z:
				if (key.isControlDown())
					MapMakerWorld.getInstance().removeLast();
				
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void update(float delta) 
	{
		//update the camera position (none negative would scroll opposite of mouse movement)
		MapMakerWorld.getInstance().updateCamera(-x, -y);
	}
}
