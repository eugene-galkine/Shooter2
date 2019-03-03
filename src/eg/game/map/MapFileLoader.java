package eg.game.map;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import eg.game.world.objects.Wall;

public class MapFileLoader
{
	public static void saveMap(ArrayList<Object> list) 
	{
		String path = "src/maps/testMap.dat";
		try
		{
			//create the file first if needed
			File file = new File(path);
			file.createNewFile();
			
			DataOutputStream os = new DataOutputStream(new FileOutputStream(path));
			
			//go through list and save each object
			for (Object obj : list)
			{
				Wall wall = (Wall) obj;
				os.writeInt((int)wall.getX());
				os.writeInt((int)wall.getY());
				os.writeInt((int)wall.getWidth());
				os.writeInt((int)wall.getHeight());
				os.writeInt((int)wall.getRot());
				os.writeInt(wall.getImgID());
			}
			
			os.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Object> loadMap()
	{
		String path = "src/maps/testMap.dat";
		ArrayList<Object> list = new ArrayList<>();
		
		try
		{
			DataInputStream os = new DataInputStream(new FileInputStream(path));
			
			//read to end of file
			while (os.available() > 0)
			{
				Wall wall = new Wall(os.readInt(), os.readInt(), os.readInt(), os.readInt(), os.readInt());
				wall.setImg(os.readInt());
				list.add(wall);
			}
			
			os.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return list;
	}
	
}
