package com.gapache.ngrok.server.task;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author husen
 */
public abstract class CustomTask {
	
	/**
	 * Command line delimiter 
	 */
	protected static String DELIMETER = ";";
	
	protected List<String> err_sysout_keyword_list = new LinkedList<>();
	
	protected String[] err_sysout_keywords = {
			"Usage", 
			"usage",
			"not found",
			"fail",
			"Fail",
			"error",
			"Error",
			"exception",
			"Exception",
			"not a valid"
	};
	
	{
		resetErrSysoutKeyword(err_sysout_keywords);
	}

	
	public void resetErrSysoutKeyword (String[] str){
		err_sysout_keyword_list.clear();
		Collections.addAll(err_sysout_keyword_list, str);
	}
	
	/**
	 * Check whether task executes successful or not.
	 *
	 * @param stdout 
	 * @param exitCode 
	 * @return If it executes successfully, returns true. Or else returns false.
	 */
	public Boolean isSuccess(String stdout, int exitCode){
		return checkStdOut(stdout) && checkExitCode(exitCode);
	}
	
	/**
	 * Check the sysout that returns from a command or script.
	 * 
	 * @param stdout
	 * @return If it executes successfully, returns true. Or else returns false.
	 */
	protected abstract Boolean checkStdOut(String stdout);
	
	/**
	 * Check the exit code that after running a command or script. 
	 * 
	 * @param exitCode 
	 * @return If it executes successfully, returns true. Or else returns false.
	 */
	protected abstract Boolean checkExitCode(int exitCode);

	/**
	 * Get the command 
	 * 
	 * @return command that used to finish the task
	 */
	public abstract String getCommand();
	
	/**
	 * Get task description
	 * 
	 * @return description of the task 
	 */
	public abstract String getInfo();
	
	/**
	 * Use to concatenate strings with blank
	 * @param args - a list or string
	 * @return String that concatenate with blank
	 */
	protected String cat(String... args) {
		StringBuilder sb = new StringBuilder();
		for (String arg : args) {
			sb.append(arg);
			sb.append(" ");
		}
		return sb.toString();
	}

}
