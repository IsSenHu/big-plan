package com.gapahce.gateway.auth;

/**
 * @author HuSen
 * create on 2020/4/7 10:32 上午
 */
public class RedisSessionInfoManager implements SessionInfoManager {

    @Override
    public void save(String token, SessionInfo session) {

    }

    @Override
    public void invalidate(String token) {

    }

    @Override
    public SessionInfo get(String token) {
        return null;
    }

    @Override
    public boolean changeLockState(String token, boolean isAccountNonLocked) {
        return false;
    }

    @Override
    public void renew(String token) {

    }
}
