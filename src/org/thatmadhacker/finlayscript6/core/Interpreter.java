package org.thatmadhacker.finlayscript6.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Interpreter {

	public static List<String> loadProgram(File file) throws Exception {
		Scanner in = new Scanner(file);
		List<String> list = new ArrayList<String>();
		while (in.hasNextLine()) {
			list.add(in.nextLine());
		}
		in.close();
		return list;
	}

	public static Program createProgram(List<String> lines) throws Exception {
		return new Program(lines, Environment.createDefaultEnv());
	}

	public static Program createProgram(List<String> lines, Environment env) throws Exception {
		return new Program(lines, env);
	}

	public static ProgramResult execProgram(Program p) throws Exception {
		return execMethod("", p);
	}

	public static ProgramResult execMethod(String method, Program p) throws Exception {

		for (Method m : p.getMethods()) {
			if (m.getName().equals(method)) {
				return execMethod(m, p);
			}
		}
		throw new Exception("Method not found!");
	}

	private static ProgramResult execMethod(Method method, Program program) throws Exception {
		program.setReturned(false);
		program.setCurrReturnVal(new FS6Object(TypeManager.TYPE_NONE, null));
		Map<String, FS6Object> localVars = program.getLocalVars();
		program.setLocalVars(new HashMap<String, FS6Object>());
		for (int i = 0; i < method.getLines().size(); i++) {
			i += execLine(method.getLines().get(i), i, program, method);
			if (program.isReturned()) {
				break;
			}
		}
		FS6Object returnVal = program.getCurrReturnVal();
		ProgramResult result = new ProgramResult(program.getReturnVal(), program.getGlobalVars(),
				program.getLocalVars(), returnVal);
		program.setLocalVars(localVars);
		return result;
	}

	private static int execLine(String line, int index, Program program, Method method) throws Exception {
		if (line.startsWith("#include")) {
			String module = line.replaceFirst("#include", "").trim();
			program.getEnv().loadModule(module, program);
			return 0;
		}
		if (line.startsWith("//") || line.startsWith("#")) {
			return 0;
		}
		if (line.contains("=")) {
			String variable = line.split("=", 2)[0].trim();
			boolean global = false;
			if (variable.split(" ", 2).length > 1) {
				global = true;
			}
			if (global) {
				variable = variable.split(" ", 2)[1];
			}
			String eval = line.split("=", 2)[1].trim();
			FS6Object obj = eval(eval, index, program);
			if (!global) {
				program.getLocalVars().put(variable, obj);
			} else {
				program.getGlobalVars().put(variable, obj);
			}
			return obj.getSkip();
		} else {
			FS6Object obj = eval(line, index, program);
			return obj.getSkip();
		}
	}

	private static FS6Object eval(String eval, int line, Program program) throws Exception {
		if (!eval.contains("(")) {
			if (isInt(eval)) {
				return new FS6Object(TypeManager.TYPE_INTEGER, Integer.valueOf(eval));
			} else {
				return new FS6Object(TypeManager.TYPE_STRING, eval.replaceAll("\"", ""));
			}
		}
		String methodName = eval.split("\\(", 2)[0].trim();
		String methodArgs = eval.split("\\(", 2)[1].trim();
		methodArgs = methodArgs.substring(0, methodArgs.lastIndexOf(")"));
		if (methodName.equals("if")) {
			if (methodArgs.equalsIgnoreCase("\"true\"")) {
				FS6Object var = new FS6Object(TypeManager.TYPE_NONE, null);
				return var;
			} else if (methodArgs.equalsIgnoreCase("\"false\"")) {
				FS6Object var = new FS6Object(TypeManager.TYPE_NONE, null);
				int count = 0;
				int endLine = -1;
				for (int i = line; i < program.getLines().size(); i++) {
					count = adjustCount(program.getLines().get(i), count);
					if (count == 0) {
						endLine = i;
						break;
					}
				}
				if (endLine == -1) {
					throw new Exception("End bracket not found for if statement on line " + line);
				}
				var.setSkip(endLine - line);
				return var;
			} else if (methodArgs.equals("1")) {
				FS6Object var = new FS6Object(TypeManager.TYPE_NONE, null);
				return var;
			} else if (methodArgs.equals("0")) {
				FS6Object var = new FS6Object(TypeManager.TYPE_NONE, null);
				int count = 0;
				int endLine = -1;
				for (int i = line; i < program.getLines().size(); i++) {
					count = adjustCount(program.getLines().get(i), count);
					if (count == 0) {
						endLine = i;
						break;
					}
				}
				if (endLine == -1) {
					throw new Exception("End bracket not found for if statement on line " + line);
				}
				var.setSkip(endLine - line);
				return var;
			} else {
				FS6Object result = eval(methodArgs, -1, program);
				if (result.getType() == TypeManager.TYPE_STRING) {
					if (((String) result.getValue()).equalsIgnoreCase("true")) {
						FS6Object var = new FS6Object(TypeManager.TYPE_NONE, null);
						return var;
					}
				} else if (result.getType() == TypeManager.TYPE_INTEGER) {
					if (((Integer) result.getValue()) == 1) {
						FS6Object var = new FS6Object(TypeManager.TYPE_NONE, null);
						return var;
					}
				} else if (result.getType() == TypeManager.TYPE_BOOLEAN) {
					if (((Boolean) result.getValue()) == true) {
						FS6Object var = new FS6Object(TypeManager.TYPE_NONE, null);
						return var;
					}
				}
				FS6Object var = new FS6Object(TypeManager.TYPE_NONE, null);
				int count = 0;
				int endLine = -1;
				for (int i = line; i < program.getLines().size(); i++) {
					count = adjustCount(program.getLines().get(i), count);
					if (count == 0) {
						endLine = i;
						break;
					}
				}
				if (endLine == -1) {
					throw new Exception("End bracket not found for if statement on line " + line);
				}
				var.setSkip(endLine - line);
				return var;
			}
		} else if (methodName.equals("for")) {
			// TODO: for logic
			return null;
		} else if (methodName.equals("while")) {
			// TODO: while logic
			return null;
		} else {
			if (program.containsMethod(methodName)) {
				Method method = program.getMethod(methodName);
				String methodArgs2 = method.getArgs();
				Map<String, FS6Object> args = parseArgs(methodArgs, methodArgs2, program);
				Map<String, FS6Object> localVars = program.getLocalVars();
				program.setLocalVars(args);
				ProgramResult result = execMethod(method, program);
				program.setLocalVars(localVars);
				return result.getReturnVal();
			} else if (program.getEnv().containsLibMethod(methodName)) {
				return program.getEnv().getLibMethods().get(methodName).execMethod(methodName,
						splitNonQuotesA(methodArgs, ","));
			} else {
				throw new Exception("Method " + methodName + " not found!!!");
			}
		}
		// TODO: add import statements so it runs quicker
	}

	private static Map<String, FS6Object> parseArgs(String args, String methodArgs, Program program) {
		Map<String, FS6Object> returnVal = new HashMap<String, FS6Object>();
		List<String> args2 = splitNonQuotes(args, ",");
		String[] methodArgs2 = methodArgs.trim().split(",");
		if (methodArgs2[0].equals("")) {
			methodArgs2 = new String[0];
		}
		for (int i = 0; i < methodArgs2.length; i++) {
			String name = methodArgs2[i];
			String arg = args2.get(i);
			boolean localVariable = program.getLocalVars().containsKey(arg);
			boolean globalVariable = program.getGlobalVars().containsKey(arg);

			if (localVariable) {
				FS6Object var = program.getLocalVars().get(arg);
				returnVal.put(name, var);
			} else if (globalVariable) {
				FS6Object var = program.getGlobalVars().get(arg);
				returnVal.put(name, var);
			} else {
				boolean integer = isInt(arg);
				if (integer) {
					FS6Object var = new FS6Object(TypeManager.TYPE_INTEGER, Integer.valueOf(arg));
					returnVal.put(name, var);
				} else {
					FS6Object var = new FS6Object(TypeManager.TYPE_STRING, arg.replaceAll("\"", ""));
					returnVal.put(name, var);
				}
			}
		}
		return returnVal;
	}

	@SuppressWarnings("unused")
	static boolean isInt(String s) {
		try {
			int i = Integer.valueOf(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	static List<String> splitNonQuotes(String s, String s1) {
		List<String> ret = new ArrayList<String>();
		if (s.length() == 0) {
			return ret;
		}
		boolean quotes = false;
		int start = 0;
		String[] sA = s.split("");
		for (int i = 0; i < sA.length; i++) {
			String s2 = sA[i];
			if (s2.equals("\"")) {
				quotes = !quotes;
			} else if (s2.equals(s1)) {
				String s3 = s.substring(start, i);
				start = i + 1;
				ret.add(s3);
			}
		}
		ret.add(s.substring(start, s.length() - 1));
		return ret;
	}

	static String[] splitNonQuotesA(String s, String s1) {
		List<String> list = splitNonQuotes(s, s1);
		String[] array = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}
		return array;
	}

	public static void setupProgram(Program p) throws Exception {
		p.getMethods().clear();
		List<String> nonMethodLines = new ArrayList<String>();
		int count = 0;
		for (int i = 0; i < p.getLines().size(); i++) {
			String line = p.getLines().get(i);
			if (count == 0 && line.contains("{")) {
				String name = line.split("\\(", 2)[0].trim();
				String args = line.split("\\(", 2)[1].trim();
				args = args.substring(0, args.lastIndexOf(")"));
				count = adjustCount(line, count);
				int endLine = -1;
				for (int i1 = i + 1; i1 < p.getLines().size(); i1++) {
					count = adjustCount(p.getLines().get(i1), count);
					if (count == 0) {
						endLine = i1;
						break;
					}
				}
				if (endLine == -1) {
					throw new Exception("Closing bracket not found for method declaration on line " + (i + 1));
				}
				List<String> lines = new ArrayList<String>();
				for (int i1 = i + 1; i1 < endLine; i1++) {
					lines.add(p.getLines().get(i1));
				}
				Method method = new Method(lines, name, args);
				p.getMethods().add(method);
				count = 0;
				i += lines.size() + 1;
				continue;
			} else if (count == 0) {
				nonMethodLines.add(line);
			}
			count = adjustCount(line, count);
		}
		Method main = new Method(nonMethodLines, "", "");
		p.getMethods().add(main);
	}

	private static int adjustCount(String line, int count) {
		int diff = 0;
		for (String s : line.split("")) {
			if (s.equals("{")) {
				diff++;
			} else if (s.equals("}")) {
				diff--;
			}
		}
		return count + diff;
	}

	public static void close(Program program) {
		for (Module m : program.getEnv().getModules()) {
			try {
				m.join();
				m.getChildProc().destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}