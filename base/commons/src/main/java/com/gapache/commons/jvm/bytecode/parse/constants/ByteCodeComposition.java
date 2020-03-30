package com.gapache.commons.jvm.bytecode.parse.constants;

import com.gapache.commons.jvm.bytecode.parse.ByteCode;
import com.gapache.commons.jvm.bytecode.parse.func.ByteCodeCompositionParser;
import com.gapache.commons.jvm.bytecode.parse.func.ByteCodeCompositionPrinter;
import lombok.Getter;

import static com.gapache.commons.jvm.bytecode.parse.func.impl.ByteCodeCompositionParserImpls.*;

/**
 * 字节码组成
 *
 * @author HuSen
 * create on 2020/3/28 00:16
 */
@Getter
public enum ByteCodeComposition {
    // 魔数
    MAGIC_NUMBER(4, MAGIC_NUMBER_PARSER),
    // 小版本
    MINOR_VERSION(2, MINOR_VERSION_PARSER),
    // 主版本
    MAJOR_VERSION(2, MAJOR_VERSION_PARSER),
    // 常量池中常量池项数量
    CONSTANT_POOL_COUNT(2, CONSTANT_POOL_COUNT_PARSER),
    // 常量池
    CONSTANT_POOL(-1, CONSTANT_POOL_PARSER),
    // 访问修饰符
    ACCESS_FLAGS(2, ACCESS_FLAGS_PARSER),
    // 当前类
    THIS_CLASS(2, THIS_CLASS_PARSER),
    // 父类
    SUPER_CLASS(2, SUPER_CLASS_PARSER),
    // 接口数量
    INTERFACES_COUNT(2, INTERFACES_COUNT_PARSER),
    // 接口列表
    INTERFACES(2, INTERFACES_PARSER),
    // 域数量
    FIELDS_COUNT(2, FIELDS_COUNT_PARSER),
    // 域列表
    FIELDS(-1, FIELDS_PARSER),
    // 方法数量
    METHODS_COUNT(2, METHODS_COUNT_PARSER),
    // 方法
    METHODS(-1, METHODS_PARSER),
    // 额外的属性数量
    ATTRIBUTES_COUNT(2, ATTRIBUTES_COUNT_PARSER),
    // 额外的属性列表
    ATTRIBUTES(-1, ATTRIBUTES_PARSER);

    private int length;
    private ByteCodeCompositionParser parser;
    private ByteCodeCompositionPrinter printer;

    ByteCodeComposition(int length, ByteCodeCompositionParser parser) {
        this.length = length;
        this.parser = parser;
    }

    public int parsing(String content, int point, ByteCode byteCode) {
        return this.parser.parsing(this, content, point, byteCode);
    }
}
