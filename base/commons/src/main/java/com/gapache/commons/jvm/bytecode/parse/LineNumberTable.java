package com.gapache.commons.jvm.bytecode.parse;

import lombok.Data;

/**
 * @author HuSen
 * create on 2020/3/28 00:05
 */
@Data
public class LineNumberTable {
    private Integer startPc;
    private Integer lineNumber;
}
