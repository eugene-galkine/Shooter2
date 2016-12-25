package eg.game.state.MapMaker;

import eg.game.world.objects.interfaces.IUpdatable;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class MapView extends IUpdatable implements EventHandler<Event>
{
	private float x, y, scrolX, scrolY;
	
	public MapView()
	{
		x = 0;
		y = 0;
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
				x = -(int)mouse.getScreenX();
				y = 800 - (int)mouse.getScreenY() - 1;
				
				if (mouse.getButton() == MouseButton.PRIMARY)
				{
					x -= scrolX - x;
					y -= scrolY - y;
					scrolX = x;
					scrolY = y;
				}
					
				break;
			case "MOUSE_PRESSED":
				if (mouse.getButton() == MouseButton.PRIMARY)
					scrolX = x;
					scrolY = y;
				break;
			case "MOUSE_RELEASED":
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void update(float delta) 
	{
		MapMakerWorld.getInstance().updateCamera(x, y);
	}
}
