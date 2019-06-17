package org.thatmadhacker.finlayscript6.core;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ProcessBuilder.Redirect;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;

public class Module extends Thread {
	public File f;
	private Process childProc;
	private Scanner in;
	private PrintWriter out;
	private Program program;
	private Lock locked;
	private Lock sending;

	public Module(File f, Program program) {
		this.f = f;
		this.program = program;
	}


	public void init() throws IOException {
		ProcessBuilder builder = new ProcessBuilder(f.getAbsolutePath());
		builder.redirectError(Redirect.INHERIT);
		childProc = builder.start();
		in = new Scanner(childProc.getInputStream());
		out = new PrintWriter(childProc.getOutputStream(), true);
		out.println("INIT");
		while (true) {
			String method = in.nextLine();
			if (method.equals("END")) {
				break;
			}
			program.env.libMethods.put(method, this);
		}
		start();
	}

	private FS6Object returnVal = null;

	public FS6Object execMethod(String method, List<FS6Object> args) {
		locked.lock();
		sending.lock();
		returnVal = null;

		out.println(method);
		for (FS6Object o : args) {
			out.println(o.type + " " + o.value.toString().replaceAll("\n", "\\n"));
		}
		out.println("END");

		sending.unlock();

		while (returnVal == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		FS6Object obj = new FS6Object(returnVal.type, returnVal.value);
		obj.skip = returnVal.skip;
		returnVal = null;
		locked.unlock();
		return obj;
	}

	@Override
	public void run() {

		while (childProc.isAlive()) {
			if (in.hasNext()) {
				String req = in.nextLine();
				sending.lock();
				if (req.startsWith("allocType")) {
					String name = req.replaceFirst("allocType", "").trim();
					int type = program.env.typeManager.allocType(name);
					out.println(type);
				} else if (req.startsWith("return")) {
					String data = req.replaceFirst("return", "").trim();
					String[] s = Interpreter.splitNonQuotesA(data, " ");
					String value = s[0];
					int type = Integer.valueOf(s[1]);
					int skip = Integer.valueOf(s[2]);
					FS6Object obj = new FS6Object(type, value);
					obj.skip = skip;
					returnVal = obj;
				} else if (req.startsWith("modCom")) {
					String[] parse = req.split(" ", 3);
					String message = parse[2];
					String modTo = parse[1];
					try {
						program.env.getModule(modTo).send("modCom "+f.getName()+" "+message);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (req.startsWith("getLVar")) {
					String name = req.split(" ",2)[1];
					FS6Object obj = program.localVars.get(name);
					if(obj == null) {
						out.println("-1 null");
					}else {
						out.println(obj.type+" "+obj.value.toString());
					}
				} else if (req.startsWith("setLVar")) {
					String[] parse = req.split(" ",4);
					String name = parse[1];
					Integer type = Integer.valueOf(parse[2]);
					Object value = parse[3];
					if(type == TypeManager.TYPE_INTEGER) {
						value = Integer.valueOf(parse[3]);
					}
					FS6Object obj = new FS6Object(type,value);
					program.localVars.put(name, obj);
				} else if (req.startsWith("getGVar")) {
					String name = req.split(" ",2)[1];
					FS6Object obj = program.globalVars.get(name);
					if(obj == null) {
						out.println("-1 null");
					}else {
						out.println(obj.type+" "+obj.value.toString());
					}
				} else if (req.startsWith("setGVar")) {
					String[] parse = req.split(" ",4);
					String name = parse[1];
					Integer type = Integer.valueOf(parse[2]);
					Object value = parse[3];
					if(type == TypeManager.TYPE_INTEGER) {
						value = Integer.valueOf(parse[3]);
					}
					FS6Object obj = new FS6Object(type,value);
					program.globalVars.put(name, obj);
				}
				sending.unlock();
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public Process getChildProc() {
		return childProc;
	}

	public void send(String message) {
		locked.lock();
		sending.lock();
		out.println(message);
		sending.unlock();
		locked.unlock();
	}

	public void close() {
		try {
			join(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		childProc.destroy();
	}
}
