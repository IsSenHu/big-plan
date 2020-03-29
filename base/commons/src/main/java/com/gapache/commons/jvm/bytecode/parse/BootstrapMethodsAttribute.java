package com.gapache.commons.jvm.bytecode.parse;

import lombok.Data;

import java.util.Map;

/**
 * @author HuSen
 * create on 2020/3/28 00:06
 */
@Data
public class BootstrapMethodsAttribute {
    private Integer attributeNameIndex;
    private Integer attributeLength;
    private Integer numBootstrapMethods;
    private Map<Integer, BootstrapMethod> bootstrapMethods;
}
