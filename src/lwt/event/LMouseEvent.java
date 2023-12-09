package lwt.event;

import org.eclipse.swt.events.MouseEvent;

import lwt.LFlags;

public class LMouseEvent {
	
	public int button;
	public int x;
	public int y;
	public int type;

	public LMouseEvent(int button, int x, int y, int type) {
		this.button = button;
		this.x = x;
		this.y = y;
	}
	
	public LMouseEvent(MouseEvent e, boolean release, boolean repeat) {
		x = e.x;
		y = e.y;
		button = 0;
		if (e.button == 1)
			button = LFlags.LEFT;
		else if (e.button == 2)
			button = LFlags.RIGHT;
		else if (e.button == 3)
			button = LFlags.MIDDLE;
		if (release) {
			if (repeat)
				type = LFlags.DOUBLEPRESS;
			else
				type = LFlags.RELEASE;
		} else {
			if (repeat)
				type = LFlags.REPEATPRESS;
			else
				type = LFlags.PRESS;
		}
	}
	
}
