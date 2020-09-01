package com.gapache.ngrok.server.task.impl;

import com.gapache.ngrok.server.task.CustomTask;

/**
 * The class extends from CustomCode class. <br>
 * <p>
 * This task is used to execute commands. You can wrap the command you want to execute into quote, and the constructor accepts multi-commands.<br>
 * Example:<p>
 * 1) CustomCode cc1 = new CommonCmd("ls");<p>
 * 2) CustomCode cc2 = new CommonCmd("CP * /opt/ibm","dir");<p>
 *
 * @author zxucdl
 */
public class ExecCommand extends CustomTask {

    protected String command = "";

    private ExecCommand() {

    }

    public ExecCommand(String... args) {
		for (String arg : args) {
			command = command + arg + DELIMETER;
		}
        command = (command.length() == 0 ? "" : command.substring(0, command.length() - 1));
    }

    @Override
	public Boolean checkStdOut(String stdout) {
		for (String s : err_sysout_keyword_list) {
			if (stdout.contains(s)) {
				return false;
			}
		}
        return true;
    }

    @Override
	public Boolean checkExitCode(int exitCode) {
		return exitCode == 0;
    }

    @Override
	public String getCommand() {
        return command;
    }

    @Override
	public String getInfo() {
        return "Exec command " + getCommand();
    }
}


