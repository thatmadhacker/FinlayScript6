package org.thatmadhacker.finlayscript6.core;

import java.util.List;

public class Method {
	private List<String> lines;
	private String name;
	private String args;
	public Method(List<String> lines, String name, String args) {
		super();
		this.lines = lines;
		this.name = name;
		this.args = args;
	}
	public List<String> getLines() {
		return lines;
	}
	public String getName() {
		return name;
	}
	public String getArgs() {
		return args;
	}
}
