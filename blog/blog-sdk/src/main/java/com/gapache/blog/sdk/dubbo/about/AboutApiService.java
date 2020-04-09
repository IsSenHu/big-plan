package com.gapache.blog.sdk.dubbo.about;

/**
 * @author HuSen
 * create on 2020/4/5 20:27
 */
public interface AboutApiService {

    /**
     * 保存关于我
     *
     * @param vo 关于我
     */
    void save(AboutVO vo);
}
