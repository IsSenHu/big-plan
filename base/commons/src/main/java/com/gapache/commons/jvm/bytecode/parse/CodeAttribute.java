package com.gapache.commons.jvm.bytecode.parse;

import lombok.Data;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/3/28 00:04
 */
@Data
public class CodeAttribute {
    private Integer attributeNameIndex;
    private Integer attributeLength;
    private Integer maxStacks;
    private Integer maxLocals;
    private Integer codeLength;
    private Object code;
    private Integer exceptionTableLength;
    private List<ExceptionTable> exceptionTables;
    private Integer attributesCount;
    private List<AttributeInfo> attributes;
}
