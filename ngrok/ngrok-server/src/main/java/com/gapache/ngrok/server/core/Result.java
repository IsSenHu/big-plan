package com.gapache.ngrok.server.core;

import lombok.Data;

/**
 * @author HuSen
 * @since 2020/8/14 10:11 上午
 */
@Data
public class Result {

    public int rc;

    public String out;

    public String errorMsg;

    public boolean isSuccess;
}
