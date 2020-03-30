package com.gapache.commons.jvm.bytecode.parse.attribute;

import com.gapache.commons.jvm.bytecode.parse.ByteCode;

import java.util.Map;

/**
 * @author HuSen
 * create on 2020/3/30 4:53 下午
 */
public class BootstrapMethodsAttributeInfo extends AbstractAttributeInfo {
    private Integer attributeNameIndex;
    private Integer attributeLength;
    private Integer numBootstrapMethods;
    private Map<Integer, BootstrapMethod> bootstrapMethods;

    @Override
    public void parsing(ByteCode byteCode) {

    }

    @Override
    public void printing(ByteCode byteCode) {

    }
}
