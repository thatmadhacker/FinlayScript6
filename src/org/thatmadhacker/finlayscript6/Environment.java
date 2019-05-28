package org.thatmadhacker.finlayscript6;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class Environment {
	
	private static final String DEFAULT_PATH = "/usr/share/FS6/modules/:$DIR/modules/";
	private String path;
	private Map<String,Module> libMethods = new HashMap<String,Module>();
	public static Environment createDefaultEnv() {
		File dir = new File(".");
		String path = DEFAULT_PATH.replaceAll("$DIR", dir.getAbsolutePath());
		return new Environment(path);
	}
	public Environment(String path) {
		super();
		this.path = path;
	}
	public Module loadModule(String name, Program p) throws Exception{
		String[] pathS = path.split(":");
		if(pathS.length == 0) {
			pathS = new String[] {path};
		}
		
		for(String s : pathS) {
			File dir = new File(s);
			
			for(File f : dir.listFiles()) {
				String moduleName = f.getName();
				if(f.getName().split("-").length > 0) {
					moduleName = f.getName().split("-")[0];
				}
				if(moduleName.equals(name)) {
					Module module = new Module(f,p);
					module.init();
					return module;
				}
			}
			
		}
		throw new FileNotFoundException("Failed to find module "+name+" in path "+path+"!");
	}
	public void addLibMethod(String method, Module module) {
		libMethods.put(method, module);
	}
}
