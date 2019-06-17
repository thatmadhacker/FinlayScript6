package org.thatmadhacker.finlayscript6.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Environment {
	
	private static final String DEFAULT_PATH = "/usr/share/FS6/modules/:$DIR/modules/";
	private String path;
	public Map<String,Module> libMethods = new HashMap<String,Module>();
	public TypeManager typeManager = new TypeManager();
	public List<Module> modules = new ArrayList<Module>();
	public static Environment createDefaultEnv() {
		File dir = new File(".");
		String path = DEFAULT_PATH.replaceAll("\\$DIR", dir.getPath());
		return new Environment(path);
	}
	public Environment(String path) {
		this.path = path;
	}
	public Module loadModule(String name, Program p) throws Exception{
		String[] pathS = path.split(":");
		if(pathS.length == 0) {
			pathS = new String[] {path};
		}
		
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
}
