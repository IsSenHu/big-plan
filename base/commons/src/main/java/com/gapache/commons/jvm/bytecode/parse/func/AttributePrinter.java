package com.gapache.commons.jvm.bytecode.parse.func;

import com.gapache.commons.jvm.bytecode.parse.AttributeInfo;
import com.gapache.commons.jvm.bytecode.parse.ByteCode;

/**
 * @author HuSen
 * create on 2020/3/30 2:09 下午
 */
public interface AttributePrinter {

    void printing(ByteCode byteCode, String attributeName, AttributeInfo attribute);
}
