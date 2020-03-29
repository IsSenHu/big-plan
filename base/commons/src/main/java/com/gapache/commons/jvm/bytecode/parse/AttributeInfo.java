package com.gapache.commons.jvm.bytecode.parse;

import lombok.Data;

/**
 * @author HuSen
 * create on 2020/3/28 00:03
 */
@Data
public class AttributeInfo {
    private Integer attributeNameIndex;
    private Integer attributeLength;
    private Object info;
}
