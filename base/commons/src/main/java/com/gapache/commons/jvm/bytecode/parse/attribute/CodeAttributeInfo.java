package com.gapache.commons.jvm.bytecode.parse.attribute;

import com.gapache.commons.jvm.bytecode.parse.ByteCode;
import com.gapache.commons.jvm.bytecode.parse.ExceptionTable;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/3/28 00:04
 */
public class CodeAttributeInfo extends AbstractAttributeInfo {
    private Integer attributeNameIndex;
    private Integer attributeLength;
    private Integer maxStacks;
    private Integer maxLocals;
    private Integer codeLength;
    private Object code;
    private Integer exceptionTableLength;
    private List<ExceptionTable> exceptionTables;
    private Integer attributesCount;
    private List<AbstractAttributeInfo> attributes;

    @Override
    public void parsing(ByteCode byteCode) {

    }

    @Override
    public void printing(ByteCode byteCode) {

    }
}
