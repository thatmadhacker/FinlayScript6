package org.thatmadhacker.finlayscript6.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Environment {
	
	private static final String DEFAULT_PATH = ".:modules/:/usr/lib/FS6/modules/";
	private String path;
	public Map<String,Module> libMethods = new HashMap<String,Module>();
	public TypeManager typeManager = new TypeManager();
	public List<Module> modules = new ArrayList<Module>();
	public static Environment createDefaultEnv() {
		return new Environment(DEFAULT_PATH);
	}
	public Environment(String path) {
		this.path = path;
	}
	public Module loadModule(String name, Program p) throws Exception{
		String[] pathS = path.split(":");
		for(String s : pathS) {
			File dir = new File(s);
			
			if(!dir.exists()) {
				continue;
			}
			
			for(File f : dir.listFiles()) {
				String moduleName = f.getName();
				if(f.getName().split("-").length > 0) {
					moduleName = f.getName().split("-")[0];
				}
				if(moduleName.equals(name)) {
					Module module = new Module(f,p);
					module.init();
					modules.add(module);
					return module;
				}
			}
			
		}
		throw new FileNotFoundException("Failed to find module "+name+" in path "+path+"!");
	}
	public Module getModule(String moduleName) throws Exception{
		for(Module m : modules) {
			if(m.f.getName().equals(moduleName)) {
				return m;
			}
		}
		throw new Exception("Moduke "+moduleName+" not found!");
	}
	/*
	 * Loads FS6 files for external classes and stuff
	 */
	public Program loadFile(String name, Program program, String programName) throws Exception {
		for(String s : path.split(":")) {
			File f = new File(s,name);
			if(f.exists()) {
				Scanner in = new Scanner(f);
				List<String> lines = new ArrayList<String>();
				while(in.hasNextLine()) {
					lines.add(in.nextLine());
				}
				in.close();
				Program p = Interpreter.createProgram(lines, this);
				Interpreter.setupProgram(p);
				program.loadedPrograms.put(programName, p);
				return p;
			}
		}
		throw new FileNotFoundException("Failed to find file "+name+" in path "+path+"!");
	}
}
