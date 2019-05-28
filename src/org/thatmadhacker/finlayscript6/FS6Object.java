package org.thatmadhacker.finlayscript6;

public class FS6Object {
	private int type;
	private Object value;
	private int skip = 0;
	public FS6Object(int type, Object value) {
		super();
		this.type = type;
		this.value = value;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	/*
	 * Used for logic statements to skip lines
	 */
	public int getSkip() {
		return skip;
	}
	/*
	 * Used for logic statements to skip lines
	 */
	public void setSkip(int skip) {
		this.skip = skip;
	}
}
