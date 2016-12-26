package eg.game.state.MapMaker;

import eg.game.Main;
import eg.game.world.objects.Wall;
import eg.game.world.objects.interfaces.IUpdatable;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class MapView extends IUpdatable implements EventHandler<Event>
{
	private static final MouseButton scrollButton = MouseButton.SECONDARY;
	private static final MouseButton editButton = MouseButton.PRIMARY;
	private float x, y, scrolX, scrolY, mx, my, placemx, placemy, mapZoom;
	private Wall selectedWall;
	
	public MapView()
	{
		x = 0;
		y = 0;
		selectedWall = null;
		mapZoom = 1;
	}
	
	@Override
	public void handle(Event e) 
	{
		if (e instanceof ScrollEvent)
		{
			ScrollEvent scroll = (ScrollEvent)e;
			//zoom with the mouse wheel
			mapZoom /= scroll.getDeltaY() < 0 ? 2 : 0.5;
			MapMakerWorld.getInstance().zoom(mapZoom);
		} else if (e instanceof MouseEvent)
		{
			MouseEvent mouse = (MouseEvent)e;
			mx = -(int)mouse.getX();
			my = Main.WINDOW_HEIGHT - (int)mouse.getY();
			
			switch (mouse.getEventType().getName())
			{
			case "MOUSE_DRAGGED":
				//scroll if we are holding mb1
				if (mouse.getButton() == scrollButton)
				{
					x -= scrolX - mx;
					y -= scrolY - my;
					scrolX = mx;
					scrolY = my;
				} else if (mouse.getButton() == editButton)
				{
					//resize the object we are placing if holding mb2
					selectedWall.setWidth((placemx - mx) / mapZoom);
					selectedWall.setHeight((placemy - my) / mapZoom);
				}
					
				break;
			case "MOUSE_PRESSED":
				if (mouse.getButton() == scrollButton)
				{
					//start scrolling if we pressed mb1
					scrolX = mx;
					scrolY = my;
				} else if (mouse.getButton() == editButton)
				{
					//place a wall if we pressed mb2
					selectedWall = new Wall((float)((x - mx) / mapZoom), (float)((y + mouse.getY()) / mapZoom), 1, 1);
					MapMakerWorld.getInstance().addObject(selectedWall);
					placemx = mx;
					placemy = my;
				}
				
				break;
			case "MOUSE_RELEASED":
				if (mouse.getButton() == editButton)
				{
					//selecting objects in the view
					if (selectedWall.getWidth() == 1 && selectedWall.getWidth() == selectedWall.getHeight())
					{
						MapMakerWorld.getInstance().removeObject(selectedWall);
						selectedWall = (Wall) MapMakerWorld.getInstance().checkCollision(selectedWall.getBounds());
						if (selectedWall != null)
						{
							//TODO change color or something
							selectedWall.setWidth(10);
						}
					}
				}
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
				//remove last placed
				if (key.isControlDown())
					MapMakerWorld.getInstance().removeLast();
				break;
			case DIGIT0:
				//reset view
				mapZoom = 1;
				x = 0;
				y = 0;
				MapMakerWorld.getInstance().zoom(mapZoom);
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
