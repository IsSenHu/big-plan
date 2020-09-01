package com.gapache.ngrok.server.core;

import com.jcraft.jsch.UserInfo;

/**
 * @author HuSen
 * @since 2020/8/14 10:04 上午
 */
public class ConnCredential implements UserInfo {

    private String password;

    public ConnCredential(String password) {
        this.password = password;
    }

    @Override
    public String getPassphrase() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean promptPassword(String s) {
        return true;
    }

    @Override
    public boolean promptPassphrase(String s) {
        return true;
    }

    @Override
    public boolean promptYesNo(String s) {
        return true;
    }

    @Override
    public void showMessage(String s) {

    }
}
