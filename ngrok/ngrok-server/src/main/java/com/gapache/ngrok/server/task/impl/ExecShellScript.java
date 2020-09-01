package com.gapache.ngrok.server.task.impl;

import com.gapache.ngrok.server.task.CustomTask;

/**
 * The class extends from CustomCode class. <br>
 * <p>
 * This task is used to execute shell script. <br>
 * Example:<p>
 * CustomCode setWASSecurityOff = new ExecShellScript("/home/tsadmin/test.sh", "args1 args2");			
 * 
 * @author zxucdl
 *
 */
public class ExecShellScript extends CustomTask {

	protected String workingDir = "";
	
	protected String shellPath = "";
	
	protected String args = "";
	
	private ExecShellScript(){
		
	}
	
	public ExecShellScript(String workingDir, String shellPath, String args){
		this.workingDir = workingDir;
		this.shellPath = shellPath;
		this.args = args;
	}
	
	public ExecShellScript(String shellPath, String args){
		this.workingDir = "";
		this.shellPath = shellPath;
		this.args = args;
	}
	
	public ExecShellScript(String shellPath){
		this.workingDir = "";
		this.shellPath = shellPath;
		this.args = "";
	}
	
	@Override
	public Boolean checkStdOut(String stdout){
		for (String s : err_sysout_keyword_list) {
			if (stdout.contains(s)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public Boolean checkExitCode(int exitCode){
		return exitCode == 0;
	}
	
	@Override
	public String getCommand(){
		if (!"".equals(workingDir)) {
			return cat("cd",workingDir,DELIMETER,shellPath, getArgs());
		} else {
			return cat(shellPath, getArgs());
		}
	}
	
	protected String getArgs(){
		return args;
	}
	
	@Override
	public String getInfo(){
		return "Exec shell script " + getCommand();
	}
}

