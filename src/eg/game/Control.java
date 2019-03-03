package eg.game;

import javafx.scene.input.KeyCode;

public class Control
{
	public static final Control Forward = new Control(KeyCode.W);
	public static final Control Backward = new Control(KeyCode.S);
	public static final Control Leftward = new Control(KeyCode.A);
	public static final Control Rightward = new Control(KeyCode.D);
	
	public static final Control Grenade = new Control(KeyCode.G);
	public static final Control Reload = new Control(KeyCode.R);
	
	private final KeyCode ourCode;
	
	private Control(KeyCode code)
	{
		ourCode = code;
	}
	
	public KeyCode getCode()
	{
		return ourCode;
	}
}
