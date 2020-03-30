package com.gapache.commons.jvm.bytecode.parse.attribute;

import com.gapache.commons.jvm.bytecode.parse.ByteCode;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/3/30 4:55 下午
 */
public class LineNumberTableAttributeInfo extends AbstractAttributeInfo {
    private Integer attributeNameIndex;
    private Integer attributeLength;
    private Integer lineNumberTableLength;
    private List<LineNumberTable> lineNumberTables;

    @Override
    public void parsing(ByteCode byteCode) {

    }

    @Override
    public void printing(ByteCode byteCode) {

    }
}
