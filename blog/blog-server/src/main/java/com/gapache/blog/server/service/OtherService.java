package com.gapache.blog.server.service;

import com.gapache.blog.common.model.dto.FriendlyLinkDTO;
import com.gapache.commons.model.JsonResult;

import java.util.List;

/**
 * @author HuSen
 * @since 2020/8/28 9:29 上午
 */
public interface OtherService {

    /**
     * 获取我的微信二维码
     *
     * @return 我的微信二维码
     */
    JsonResult<String> wxQrCode();

    /**
     * 返回所有的友情链接
     *
     * @return 所有的友情链接
     */
    JsonResult<List<FriendlyLinkDTO>> friendlyLinks();

    /**
     * 保存微信二维码
     *
     * @param link 链接
     * @return 微信二维码
     */
    JsonResult<Boolean> saveWxQrCode(String link);

    /**
     * 保存友情链接
     *
     * @param list 友情链接列表
     * @return 保存结果
     */
    JsonResult<Boolean> saveFriendlyLinks(List<FriendlyLinkDTO> list);
}
