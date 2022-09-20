package mines;

import javafx.scene.control.Button;

class Spot extends Button{
	private int x;
	private int y;
	private boolean opened;
	
	public Spot(String s, int x, int y) {
		super(s);
		this.x = x;
		this.y = y;
		opened = false;
	}

	public int getx() {
		return x;
	}
	public int gety(){
		return y;
	}
	public void setOpened(boolean val) {
		opened = val;
	}
	public boolean getOpened(){
		return opened;
	}
}
