package org.thatmadhacker.finlayscript6.core;

import java.util.List;

public class Method {
	public List<String> lines;
	public String name;
	public String args;
	public Method(List<String> lines, String name, String args) {
		super();
		this.lines = lines;
		this.name = name;
		this.args = args;
	}
}
