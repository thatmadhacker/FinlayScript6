package org.thatmadhacker.finlayscript6.core;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Module extends Thread{
	private File f;
	private Process childProc;
	private Scanner in;
	private PrintWriter out;
	private Program program;
	//Thread safety
	private boolean locked = false;
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
		start();
	}
	private FS6Object returnVal = null;
	private boolean sending = false;
	public FS6Object execMethod(String method, String[] args) {
		while(!locked && !sending) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		locked = true;
		sending = true;
		returnVal = null;
		
		out.println(method);
		for(String s : args) {
			out.println(s.replaceAll("\n", "\\n"));
		}
		out.println("END");
		
		sending = false;
		
		while (returnVal == null) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		FS6Object obj = new FS6Object(returnVal.getType(), returnVal.getValue());
		obj.setSkip(returnVal.getSkip());
		returnVal = null;
		locked = false;
		return obj;
	}
	@Override
	public void run() {
		
		while(childProc.isAlive()) {
			String req = in.nextLine();
			while(sending) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			sending = true;
			if(req.startsWith("allocType")) {
				String name = req.replaceFirst("allocType", "").trim();
				int type = program.getEnv().getTypeManager().allocType(name);
				out.println(type);
			}else if(req.startsWith("return")) {
				String data = req.replaceFirst("return", "").trim();
				String[] s = Interpreter.splitNonQuotesA(data, " ");
				String value = s[0];
				int type = Integer.valueOf(s[1]);
				int skip = Integer.valueOf(s[2]);
				FS6Object obj = new FS6Object(type,value);
				obj.setSkip(skip);
				returnVal = obj;
			}
			sending = false;
		}
		
	}
	public Process getChildProc() {
		return childProc;
	}
}
