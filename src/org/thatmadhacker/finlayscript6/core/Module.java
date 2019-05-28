package org.thatmadhacker.finlayscript6.core;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Module {
	private File f;
	private Process childProc;
	private Scanner in;
	private PrintWriter out;
	private Program program;
	public Module(File f, Program program) {
		super();
		this.f = f;
		this.program = program;
	}
	public File getF() {
		return f;
	}
	public void init() throws IOException {
		childProc = Runtime.getRuntime().exec(f.getAbsolutePath());
		in = new Scanner(childProc.getInputStream());
		out = new PrintWriter(childProc.getOutputStream(),true);
		out.println("INIT");
		while(true) {
			String method = in.nextLine();
			if(method.equals("END")) {
				break;
			}
			program.getEnv().addLibMethod(method,this);
		}
	}
	public FS6Object execMethod(String method, String[] args) {
		//TODO: write module interface
		return null;
	}
}
