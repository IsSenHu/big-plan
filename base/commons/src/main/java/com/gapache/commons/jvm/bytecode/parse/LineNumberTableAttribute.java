package com.gapache.commons.jvm.bytecode.parse;

import lombok.Data;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/3/28 00:05
 */
@Data
public class LineNumberTableAttribute {
    private Integer attributeNameIndex;
    private Integer attributeLength;
    private Integer lineNumberTableLength;
    private List<LineNumberTable> lineNumberTables;
}
