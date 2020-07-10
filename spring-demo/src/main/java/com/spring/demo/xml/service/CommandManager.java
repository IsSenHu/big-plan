package com.spring.demo.xml.service;

/**
 * @author HuSen
 * @since 2020/7/10 4:01 下午
 */
public class CommandManager {

    public String process(String commandState) {
        // grab a new instance of the appropriate Command interface
        Command command = createCommand();
        // set the state on the (hopefully brand new) Command instance
        command.setState(commandState);
        return command.execute();
    }

    public Command createCommand() {
        return null;
    }
}
