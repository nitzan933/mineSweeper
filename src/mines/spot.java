package mines;

import javafx.scene.control.Button;

class spot extends Button{
	private int x;
	private int y;
	private boolean opened;
	
	public spot(String s, int x, int y) {
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
	public void setflag(boolean val) {
		opened = val;
	}
	public boolean getflag(){
		return opened;
	}
}
