package com.gapache.commons.jvm.bytecode.parse.attribute;

import com.gapache.commons.jvm.bytecode.parse.ByteCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author HuSen
 * create on 2020/3/28 00:03
 */
@Setter
@Getter
public abstract class AbstractAttributeInfo {
    private Integer attributeNameIndex;
    private Integer attributeLength;
    private Object info;

    public abstract void parsing(ByteCode byteCode);

    public abstract void printing(ByteCode byteCode);
}
