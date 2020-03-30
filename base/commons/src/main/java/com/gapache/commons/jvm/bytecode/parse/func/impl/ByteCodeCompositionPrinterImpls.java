package com.gapache.commons.jvm.bytecode.parse.func.impl;

import com.gapache.commons.jvm.bytecode.parse.cp.ConstantItem;
import com.gapache.commons.jvm.bytecode.parse.cp.ValueConstantItem;
import com.gapache.commons.jvm.bytecode.parse.func.ByteCodeCompositionPrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author HuSen
 * create on 2020/3/30 09:54
 */
public class ByteCodeCompositionPrinterImpls {

    public static final ByteCodeCompositionPrinter MAGIC_NUMBER_PRINTER =
            byteCode -> System.out.println("魔数: " + byteCode.getMagicNumber());

    public static final ByteCodeCompositionPrinter MINOR_VERSION_PRINTER =
            byteCode -> System.out.println("次版本号: " + byteCode.getMajorVersion());

    public static final ByteCodeCompositionPrinter MAJOR_VERSION_PRINTER =
            byteCode -> System.out.println("主版本号: " + byteCode.getMajorVersion());

    public static final ByteCodeCompositionPrinter CONSTANT_POOL_COUNT_PRINTER =
            byteCode -> System.out.println("常量池项数量: " + byteCode.getConstantPoolCount());

    public static final ByteCodeCompositionPrinter CONSTANT_POOL_PRINTER =
            byteCode ->
            {
                Map<Integer, ConstantItem> constantPool = byteCode.getConstantPool();
                constantPool.forEach((index, constantItem) -> constantItem.getTag().getConstantInfoPrinter().print(constantPool, index, constantItem));
            };

    public static final ByteCodeCompositionPrinter ACCESS_FLAGS_PRINTER =
            byteCode -> System.out.println("访问修饰符: " + byteCode.getAccessFlags());

    public static final ByteCodeCompositionPrinter THIS_CLASS_PRINTER =
            byteCode ->
            {
                Integer thisClassIndex = byteCode.getThisClass();
                ConstantItem classNameItem = byteCode.getConstantPool().get(thisClassIndex);
                System.out.println("当前类: " + checkValueFromConstantItem(classNameItem));
            };

    public static final ByteCodeCompositionPrinter SUPER_CLASS_PRINTER =
            byteCode ->
            {
                Integer superClassIndex = byteCode.getSuperClass();
                ConstantItem classNameItem = byteCode.getConstantPool().get(superClassIndex);
                System.out.println("父类: " + checkValueFromConstantItem(classNameItem));
            };

    public static final ByteCodeCompositionPrinter INTERFACES_COUNT_PRINTER =
            byteCode -> System.out.println("接口数量: " + byteCode.getInterfacesCount());

    public static final ByteCodeCompositionPrinter INTERFACES_PRINTER =
            byteCode ->
            {
                List<String> interfaces = new ArrayList<>(byteCode.getInterfacesCount());
                for (Integer nameIndex : byteCode.getInterfaces()) {
                    ConstantItem classNameItem = byteCode.getConstantPool().get(nameIndex);
                    interfaces.add(checkValueFromConstantItem(classNameItem).toString());
                }
                System.out.println("接口: " + interfaces);
            };

    public static final ByteCodeCompositionPrinter FIELDS_COUNT_PRINTER =
            byteCode -> System.out.println("域数量: " + byteCode.getFieldsCount());

    public static final ByteCodeCompositionPrinter FIELDS_PRINTER =
            byteCode ->
                    byteCode.getFields()
                            .forEach(fieldInfo ->
                            {
                                System.out.println("访问修饰符: " + fieldInfo.getAccessFlags());
                                System.out.println("描述符: " + checkValueFromConstantItem(byteCode.getConstantPool().get(fieldInfo.getDescriptorIndex())));
                                System.out.println("名称: " + checkValueFromConstantItem(byteCode.getConstantPool().get(fieldInfo.getNameIndex())));
                                // TODO 属性
                            });

    public static final ByteCodeCompositionPrinter METHODS_COUNT_PRINTER =
            byteCode -> System.out.println("方法数量: " + byteCode.getMethodsCount());

    public static final ByteCodeCompositionPrinter METHODS_PRINTER =
            byteCode ->
                    byteCode.getMethods()
                            .forEach(methodInfo ->
                            {
                                System.out.println("访问修饰符: " + methodInfo.getAccessFlags());
                                System.out.println("描述符: " + checkValueFromConstantItem(byteCode.getConstantPool().get(methodInfo.getDescriptorIndex())));
                                System.out.println("名称: " + checkValueFromConstantItem(byteCode.getConstantPool().get(methodInfo.getNameIndex())));
                                // TODO 属性
                            });

    public static final ByteCodeCompositionPrinter ATTRIBUTES_COUNT_PRINTER =
            byteCode -> System.out.println("额外属性数量: " + byteCode.getAttributesCount());

    public static final ByteCodeCompositionPrinter ATTRIBUTES_PRINTER =
            byteCode ->
                    byteCode.getAttributes()
                            .forEach(attributeInfo ->
                            {
                                System.out.println("属性名称: " + checkValueFromConstantItem(byteCode.getConstantPool().get(attributeInfo.getAttributeNameIndex())));
                                System.out.println("属性长度: " + attributeInfo.getAttributeNameIndex());
                                System.out.println("属性信息: " + attributeInfo.getInfo());
                            });

    public static Object checkValueFromConstantItem(ConstantItem constantItem) {
        return ((ValueConstantItem) constantItem).getValue();
    }
}
