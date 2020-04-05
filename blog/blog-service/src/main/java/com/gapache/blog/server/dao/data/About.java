package com.gapache.blog.server.dao.data;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 关于我 也是用md写的然后转html的二进制文件
 *
 * @author HuSen
 * create on 2020/4/5 20:19
 */
@Data
public class About implements Serializable {
    private static final long serialVersionUID = 489793814892217821L;
    private byte[] content;
    private LocalDateTime lastOpTime;
}
