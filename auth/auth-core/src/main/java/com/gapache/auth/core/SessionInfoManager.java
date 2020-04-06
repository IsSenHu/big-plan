package com.gapache.auth.core;

/**
 * @author HuSen
 * create on 2020/4/6 22:47
 */
public interface SessionInfoManager {

    /**
     * 保存会话信息
     *
     * @param token   令牌
     * @param session 会话
     */
    void save(String token, SessionInfo session);

    /**
     * 注销令牌
     *
     * @param token 令牌
     */
    void invalidate(String token);

    /**
     * 获取会话
     *
     * @param token 令牌
     * @return 会话
     */
    SessionInfo get(String token);

    /**
     * 修改用户的锁定状态
     *
     * @param token              Token
     * @param isAccountNonLocked 是否没有被锁定
     * @return 是否修改成功
     */
    boolean changeLockState(String token, boolean isAccountNonLocked);

    /**
     * 续租令牌
     *
     * @param token 令牌
     */
    void renew(String token);
}
