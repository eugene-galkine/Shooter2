package eg.game.state.MapMaker;

import eg.game.world.objects.interfaces.IUpdatable;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class MapView extends IUpdatable implements EventHandler<Event>
{
	private float x, y, scrolX, scrolY, mx, my;
	
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
				mx = -(int)mouse.getX();
				my = 800 - (int)mouse.getY();
				
				if (mouse.getButton() == MouseButton.PRIMARY)
				{
					x -= scrolX - mx;
					y -= scrolY - my;
					scrolX = mx;
					scrolY = my;
				}
					
				break;
			case "MOUSE_PRESSED":
				if (mouse.getButton() == MouseButton.PRIMARY)
					scrolX = -(int)mouse.getX();
					scrolY = 800 - (int)mouse.getY();
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
