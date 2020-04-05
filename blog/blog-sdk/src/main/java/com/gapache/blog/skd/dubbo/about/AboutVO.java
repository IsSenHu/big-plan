package com.gapache.blog.skd.dubbo.about;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HuSen
 * create on 2020/4/5 20:25
 */
@Data
public class AboutVO implements Serializable {
    private static final long serialVersionUID = 352538903292474589L;
    private byte[] content;
    private LocalDateTime lastOpTime;
}
