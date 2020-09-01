package com.gapache.ngrok.server.core;

/**
 * @author HuSen
 * @since 2020/8/14 10:24 上午
 */
public class TaskExecFailException extends Exception {

    private static final long serialVersionUID = -8501548142007084261L;
    private final String reason;

    public TaskExecFailException (String message) {
        super(message);
        this.reason = "The task has failed to execute : " + message;
    }

    @Override
    public String getMessage() {
        return reason;
    }
}
