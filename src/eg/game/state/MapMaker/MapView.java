package eg.game.state.mapMaker;

import eg.game.Main;
import eg.game.world.objects.Wall;
import eg.game.world.objects.interfaces.IUpdatable;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
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
			mapZoom /= scroll.getDeltaY() < 0 ? 1.25 : 0.8;
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
					//deselect if we have a wall already selected
					if (selectedWall != null)
						selectedWall.deselect();
					
					//place a wall if we pressed mb2
					selectedWall = new Wall((float)((x - mx) / mapZoom), (float)((y + mouse.getY()) / mapZoom), 1, 1, 0);
					selectedWall.select();
					MapMakerWorld.getInstance().addObject(selectedWall);
					placemx = mx;
					placemy = my;
				}
				
				break;
			case "MOUSE_RELEASED":
				if (mouse.getButton() == editButton)
				{
					//selecting objects in the view
					if (Math.abs(selectedWall.getWidth()) < 5 && Math.abs(selectedWall.getHeight()) < 5)
					{
						MapMakerWorld.getInstance().removeObject(selectedWall);
						selectedWall = (Wall) MapMakerWorld.getInstance().checkCollision(selectedWall.getBounds());
						//change color of the wall if we are clicking on something instead of empty canvas
						if (selectedWall != null)
							selectedWall.select();
					} else
						//fix negative size
						selectedWall.normalize();
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
				//remove last placed with ctrl-z
				if (key.isControlDown())
					MapMakerWorld.getInstance().removeLast();
				break;
			case DIGIT0:
				//reset view with 0 key
				mapZoom = 1;
				x = 0;
				y = 0;
				MapMakerWorld.getInstance().zoom(mapZoom);
				break;
			case Q:
				//change the image on this object when pressing q
				if (selectedWall != null)
					selectedWall.nextImg();
				break;
			case S:
				//save with ctrl-s
				if (key.isControlDown())
					MapMakerWorld.getInstance().saveMap();
				break;
			case DELETE:
				//remove selected block with delete key
				if (selectedWall != null)
				{
					MapMakerWorld.getInstance().removeObject(selectedWall);
					selectedWall = null;
				}
				break;
			case O:
				//load map with o
				if (key.isControlDown())
					MapMakerWorld.getInstance().loadMap();
				break;
			case DIGIT1:
				//set rotation to 0 with 1
				if (selectedWall != null)
					selectedWall.setRot(0);
				break;
			case DIGIT2:
				//set rotation to 45 with 2
				if (selectedWall != null)
					selectedWall.setRot(45);
				break;
			case DIGIT3:
				//set rotation to 90 with 3
				if (selectedWall != null)
					selectedWall.setRot(90);
				break;
			case DIGIT4:
				//set rotation to 135 with 4
				if (selectedWall != null)
					selectedWall.setRot(135);
				break;
			case DIGIT5:
				//set rotation to 180 with 5
				if (selectedWall != null)
					selectedWall.setRot(180);
				break;
			case DIGIT6:
				//set rotation to 225 with 6
				if (selectedWall != null)
					selectedWall.setRot(225);
				break;
			case DIGIT7:
				//set rotation to 270 with 7
				if (selectedWall != null)
					selectedWall.setRot(270);
				break;
			case DIGIT8:
				//set rotation to 315 with 8
				if (selectedWall != null)
					selectedWall.setRot(315);
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
