package org.thatmadhacker.finlayscript6.core;

public class FS6Object {
	public int type;
	public Object value;
	public int skip = 0;
	public FS6Object(int type, Object value) {
		this.type = type;
		this.value = value;
	}
	public boolean isTrue() {
		if(type == TypeManager.TYPE_BOOLEAN) {
			return (boolean) value;
		}else if(type == TypeManager.TYPE_INTEGER) {
			long i = (long) value;
			return i == 1 ? true : false;
		}else if(type == TypeManager.TYPE_STRING) {
			String s = (String) value;
			return s.equalsIgnoreCase("true") ? true : false;
		}
		return false;
	}
}
