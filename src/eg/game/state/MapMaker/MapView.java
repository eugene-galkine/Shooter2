package eg.game.state.mapMaker;

import eg.game.Main;
import eg.game.world.objects.Wall;
import eg.game.world.objects.interfaces.IUpdatable;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 * 
 * @author Eugene Galkine
 *
 * controls for map editor:
 * save - ctrl+s
 * load - ctrl+o
 * undo last move - ctrl+z
 * pan view - rmb
 * place/select/move - lmb
 * zoom - scroll wheel
 * rotate selected - 1,2,3,4,5,6,7,8
 * reset camera - 0
 * next texture - Q
 * remove selected - DEL
 * toggle move/resize - M
 *
 */

public class MapView extends IUpdatable implements EventHandler<Event>
{
	private static final MouseButton scrollButton = MouseButton.SECONDARY;
	private static final MouseButton editButton = MouseButton.PRIMARY;
	private float x, y, scrolX, scrolY, mx, my, placemx, placemy, mapZoom, oldX, oldY, oldW, oldH;
	private Wall selectedWall;
	private boolean move;
	
	public MapView()
	{
		x = 0;
		y = 0;
		selectedWall = null;
		mapZoom = 1;
		move = false;
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
					if (!move)
					{
						//resize the object we are placing if holding mb2
						selectedWall.setWidth(oldW + (placemx - mx) / mapZoom);
						selectedWall.setHeight(oldH + (placemy - my) / mapZoom);
					} else
					{
						//move object around
						selectedWall.setX(oldX + ((placemx - mx) / mapZoom));
						selectedWall.setY(oldY + ((placemy - my) / mapZoom));
					}
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
					
					placemx = mx;
					placemy = my;
					
					//select object on click
					Wall hit = (Wall) MapMakerWorld.getInstance().checkCollision(selectedWall.getBounds());
					if (hit != null)
					{
						//start moving the selected object if we clicked on something
						move = true;
						selectedWall = hit;
						selectedWall.select();
					} else 
					{
						//otherwise we just created a new wall
						move = false;
						MapMakerWorld.getInstance().addObject(selectedWall);
					}
					
					//record original position and reset the size counter
					oldX = selectedWall.getX();
					oldY = selectedWall.getY();
					oldW = 0;
					oldH = 0;
				}
				
				break;
			case "MOUSE_RELEASED":
				
				if (mouse.getButton() == editButton)
				{
					//selecting objects in the view
					if (Math.abs(selectedWall.getWidth()) < 5 && Math.abs(selectedWall.getHeight()) < 5)
					{
						MapMakerWorld.getInstance().removeObject(selectedWall);
						selectedWall = null;
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
				//undo last move with ctrl-z
				
				if (key.isControlDown())
					if (selectedWall != null)
					{
						//revert the position
						selectedWall.setX(oldX);
						selectedWall.setY(oldY);
						
						//revert the size if needed
						if (oldW != 0 && oldH != 0)
						{
							selectedWall.setHeight(oldH);
							selectedWall.setWidth(oldW);
						}
					}
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
			case M:
				//toggle move/resize
				move = !move;
				//if we are now resizing record old size for undo
				if (!move && selectedWall != null)
				{
					oldW = selectedWall.getWidth();
					oldH = selectedWall.getHeight();
				}
				break;
			case DIGIT0:
				//reset view with 0 key
				mapZoom = 1;
				x = 0;
				y = 0;
				MapMakerWorld.getInstance().zoom(mapZoom);
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
