package org.thatmadhacker.finlayscript6.core;

public class FS6Object {
	public int type;
	public Object value;
	public int skip = 0;
	public FS6Object(int type, Object value) {
		super();
		this.type = type;
		this.value = value;
	}
}
