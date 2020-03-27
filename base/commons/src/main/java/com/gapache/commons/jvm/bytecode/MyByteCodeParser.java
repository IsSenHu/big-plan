package com.gapache.commons.jvm.bytecode;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 我的字节码解析工具
 *
 * @author HuSen
 * create on 2020/3/24 6:06 下午
 */
public class MyByteCodeParser {

    @Data
    private static final class ByteCode {
        private String magicNumber;
        private Integer minorVersion;
        private Integer majorVersion;
        private Integer constantPoolCount;
        private Map<Integer, ConstantItem> constantPool;
        private List<AccessFlag> accessFlags;
        private Integer thisClass;
        private Integer superClass;
        private Integer interfacesCount;
        private List<Integer> interfaces;
        private Integer fieldsCount;
        private List<FieldInfo> fields;
        private Integer methodsCount;
        private List<MethodInfo> methods;
        private Integer attributesCount;
        private List<AttributeInfo> attributes;
    }

    @Data
    private static final class FieldInfo {
        private List<AccessFlag> accessFlags;
        private Integer nameIndex;
        private Integer descriptorIndex;
        private Integer attributesCount;
        private List<AttributeInfo> attributes;
    }

    @Data
    private static final class MethodInfo {
        private List<AccessFlag> accessFlags;
        private Integer nameIndex;
        private Integer descriptorIndex;
        private Integer attributesCount;
        private List<AttributeInfo> attributes;
    }

    @Data
    private static final class AttributeInfo {
        private Integer attributeNameIndex;
        private Integer attributeLength;
        private Object info;
    }

    @Data
    private static final class CodeAttribute {
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

    @Data
    private static final class ExceptionTable {
        private Integer startPc;
        private Integer endPc;
        private Integer handlerPc;
        private Integer catchType;
    }

    @Data
    private static final class LineNumberTableAttribute {
        private Integer attributeNameIndex;
        private Integer attributeLength;
        private Integer lineNumberTableLength;
        private List<LineNumberTable> lineNumberTables;
    }

    @Data
    private static final class LineNumberTable {
        private Integer startPc;
        private Integer lineNumber;
    }

    @Data
    private static final class BootstrapMethodsAttribute {
        private Integer attributeNameIndex;
        private Integer attributeLength;
        private Integer numBootstrapMethods;
        private Map<Integer, BootstrapMethod> bootstrapMethods;
    }

    @Data
    private static final class BootstrapMethod {
        private Integer bootstrapMethodRef;
        private Integer numBootstrapArguments;
        private List<String> bootstrapArguments;
    }

    @FunctionalInterface
    private interface ConstantInfoParser {
        int parsing(CpTag cpTag, int point, String context, ByteCode byteCode);
    }

    @FunctionalInterface
    private interface ConstantInfoPrinter {
        void print(Map<Integer, ConstantItem> constantPool, int index, ConstantItem item);
    }

    @Getter
    private enum CpTag {
        //
        CONSTANT_UTF8_INFO(1, 2, "Utf8", valueConstantInfoParse, valueConstantInfoPrinter),
        CONSTANT_INTEGER_INFO(3, 4, "Integer", valueConstantInfoParse, valueConstantInfoPrinter),
        CONSTANT_FLOAT_INFO(4, 4, "Float", valueConstantInfoParse, valueConstantInfoPrinter),
        CONSTANT_LONG_INFO(5, 8, "Long", valueConstantInfoParse, valueConstantInfoPrinter),
        CONSTANT_DOUBLE_INFO(6, 8, "Double", valueConstantInfoParse, valueConstantInfoPrinter),
        CONSTANT_CLASS_INFO(7, 2, "Class", indexConstantInfoParser, classAndStringConstantInfoPrinter),
        CONSTANT_STRING_INFO(8, 2, "String", indexConstantInfoParser, classAndStringConstantInfoPrinter),
        CONSTANT_FILED_REF_INFO(9, 4, "Fieldref", indexConstantInfoParser, fieldAndMethodRefConstantInfoPrinter),
        CONSTANT_METHOD_REF_INFO(10, 4, "Methodref", indexConstantInfoParser, fieldAndMethodRefConstantInfoPrinter),
        CONSTANT_INTERFACE_METHOD_REF_INFO(11, 4, "InterfaceMethodref", indexConstantInfoParser, fieldAndMethodRefConstantInfoPrinter),
        CONSTANT_NAME_AND_TYPE_INFO(12, 4, "NameAndType", indexConstantInfoParser, nameAndTypeConstantInfoPrinter),
        CONSTANT_METHOD_HANDLE_INFO(15, 3, "MethodHandle", methodHandleConstantInfoParser, null),
        CONSTANT_METHOD_TYPE_INFO(16, 2, "MethodType", indexConstantInfoParser, null),
        CONSTANT_INVOKE_DYNAMIC_INFO(18, 4, "InvokeDynamic", indexConstantInfoParser, null),
        UNKNOWN(99, 0, "", null, null);

        private int value;
        private int bytesNumber;
        private String showValue;
        private ConstantInfoParser constantInfoParser;
        private ConstantInfoPrinter constantInfoPrinter;

        CpTag(int value, int bytesNumber, String showValue, ConstantInfoParser constantInfoParser, ConstantInfoPrinter constantInfoPrinter) {
            this.value = value;
            this.bytesNumber = bytesNumber;
            this.showValue = showValue;
            this.constantInfoParser = constantInfoParser;
            this.constantInfoPrinter = constantInfoPrinter;
        }

        public static CpTag valueOf(int value) {
            return Arrays.stream(CpTag.values())
                    .filter(tag -> tag.getValue() == value)
                    .findFirst()
                    .orElse(CpTag.UNKNOWN);
        }
    }

    @Getter
    private enum AccessFlag {
        //
        ACC_PUBLIC(0x0001),
        ACC_PRIVATE(0x0002),
        ACC_PROTECTED(0x0004),
        ACC_STATIC(0x0008),
        ACC_FINAL(0x0010),
        ACC_SUPER(0x0020),
        ACC_VOLATILE(0x0040),
        ACC_TRANSIENT(0x0080),
        ACC_INTERFACE(0x0200),
        ACC_ABSTRACT(0x0400),
        ACC_SYNTHETIC(0x1000),
        ACC_ANNOTATION(0x2000),
        ACC_ENUM(0x4000);

        private int value;

        AccessFlag(int value) {
            this.value = value;
        }

        public static List<AccessFlag> checkAccessFlags(int accessFlags) {
            return Arrays.stream(AccessFlag.values())
                    .filter(accessFlag -> (accessFlags & accessFlag.getValue()) == accessFlag.getValue())
                    .collect(Collectors.toList());
        }
    }

    @Setter
    @Getter
    private static class ConstantItem {
        private CpTag tag;
        private String description;
    }

    @Setter
    @Getter
    private static class ValueConstantItem extends ConstantItem {
        private Object value;
    }

    @Setter
    @Getter
    private static class IndexConstantItem extends ConstantItem {
        private int[] indexes;
    }

    @Setter
    @Getter
    private static class MethodHandleConstantItem extends ConstantItem {
        private int referenceKind;
        private int referenceIndex;
    }

    private String filePath;
    private int point;
    private ByteCode byteCode;

    public MyByteCodeParser(String filePath) {
        this.filePath = filePath;
        this.byteCode = new ByteCode();
    }

    public void parse() {
        byte[] data;
        try (InputStream is = new FileInputStream(new File(this.filePath)); ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            int ch;
            while (-1 != (ch = is.read())) {
                byteArrayOutputStream.write(ch);
            }
            data = byteArrayOutputStream.toByteArray();
            String content = new BigInteger(1, data).toString(16);

            point = printInfo(content, 8, point, magicNumber -> {
                byteCode.setMagicNumber(magicNumber);
            });

            point = printInfo(content, 4, point, minorVersionHex -> {
                int minorVersion = hexToInt(minorVersionHex);
                byteCode.setMinorVersion(minorVersion);
            });

            point = printInfo(content, 4, point, majorVersionHex -> {
                int majorVersion = hexToInt(majorVersionHex);
                byteCode.setMajorVersion(majorVersion);
            });

            point = printInfo(content, 4, point, constantPoolCountHex -> {
                int constantPoolCount = hexToInt(constantPoolCountHex);
                byteCode.setConstantPoolCount(constantPoolCount);
                byteCode.setConstantPool(new HashMap<>(constantPoolCount));
            });

            int constantPoolRealCount = byteCode.getConstantPoolCount() - 1;
            for (int i = 0; i < constantPoolRealCount; i++) {
                String tagHex = content.substring(point, point + 2);
                point += 2;
                CpTag cpTag = CpTag.valueOf(hexToInt(tagHex));
                point = cpTag.getConstantInfoParser().parsing(cpTag, point, content, byteCode);
            }
            Map<Integer, ConstantItem> constantPool = byteCode.getConstantPool();

            point = printInfo(content, 4, point, accessFlagsHex -> {
                int accessFlags = hexToInt(accessFlagsHex);
                List<AccessFlag> flags = AccessFlag.checkAccessFlags(accessFlags);
                byteCode.setAccessFlags(flags);
            });

            point = printInfo(content, 4, point, thisClassHex -> {
                int thisClassIndex = hexToInt(thisClassHex);
                ConstantItem classInfo = constantPool.get(thisClassIndex);
                int utf8InfoIndex = ((IndexConstantItem) classInfo).getIndexes()[0];
                ConstantItem utf8Info = constantPool.get(utf8InfoIndex);
                byteCode.setThisClass(thisClassIndex);
            });

            point = printInfo(content, 4, point, superClassHex -> {
                int superClassIndex = hexToInt(superClassHex);
                ConstantItem classInfo = constantPool.get(superClassIndex);
                int utf8InfoIndex = ((IndexConstantItem) classInfo).getIndexes()[0];
                ConstantItem utf8Info = constantPool.get(utf8InfoIndex);
                byteCode.setThisClass(superClassIndex);
            });

            point = printInfo(content, 4, point, interfacesCountHex -> {
                int interfacesCount = hexToInt(interfacesCountHex);
                byteCode.setInterfacesCount(interfacesCount);
            });

            byteCode.setInterfaces(new ArrayList<>(byteCode.getInterfacesCount()));
            List<String> interfaces = new ArrayList<>(byteCode.getInterfacesCount());
            for (int i = 0; i < byteCode.getInterfacesCount(); i++) {
                point = printInfo(content, 4, point, interfaceIndexHex -> {
                    int interfaceIndex = hexToInt(interfaceIndexHex);
                    ConstantItem classInfo = constantPool.get(interfaceIndex);
                    int utf8InfoIndex = ((IndexConstantItem) classInfo).getIndexes()[0];
                    ConstantItem utf8Info = constantPool.get(utf8InfoIndex);
                    byteCode.getInterfaces().add(interfaceIndex);
                    interfaces.add(((ValueConstantItem) utf8Info).getValue().toString());
                });
            }

            point = printInfo(content, 4, point, fieldsCountHex -> {
                int fieldsCount = hexToInt(fieldsCountHex);
                byteCode.setFieldsCount(fieldsCount);
            });

            List<FieldInfo> fields = new ArrayList<>(byteCode.getFieldsCount());
            for (int i = 0; i < byteCode.getFieldsCount(); i++) {
                FieldInfo fieldInfo = new FieldInfo();
                String accessFlagsHex = content.substring(point, (point = point + 4));
                fieldInfo.setAccessFlags(AccessFlag.checkAccessFlags(hexToInt(accessFlagsHex)));

                String nameIndexHex = content.substring(point, (point = point + 4));
                fieldInfo.setNameIndex(hexToInt(nameIndexHex));

                String descriptorIndexHex = content.substring(point, (point = point + 4));
                fieldInfo.setDescriptorIndex(hexToInt(descriptorIndexHex));

                String attributesCountHex = content.substring(point, (point = point + 4));
                fieldInfo.setAttributesCount(hexToInt(attributesCountHex));

                List<AttributeInfo> attributes = new ArrayList<>(fieldInfo.getAttributesCount());
                for (int j = 0; j < fieldInfo.getAttributesCount(); j++) {
                    AttributeInfo attributeInfo = new AttributeInfo();
                    String attributeNameIndexHex = content.substring(point, (point = point + 4));
                    attributeInfo.setAttributeNameIndex(hexToInt(attributeNameIndexHex));

                    String attributeLengthHex = content.substring(point, (point = point + 8));
                    attributeInfo.setAttributeLength(hexToInt(attributeLengthHex));

                    String infoHex = content.substring(point, (point = point + attributeInfo.getAttributeLength() * 2));
                    attributeInfo.setInfo(infoHex);

                    attributes.add(attributeInfo);
                }
                fieldInfo.setAttributes(attributes);

                fields.add(fieldInfo);
            }
            byteCode.setFields(fields);

            point = printInfo(content, 4, point, methodsCountHex -> {
                int methodsCount = hexToInt(methodsCountHex);
                byteCode.setMethodsCount(methodsCount);
            });

            List<MethodInfo> methods = new ArrayList<>(byteCode.getMethodsCount());
            for (int i = 0; i < byteCode.getMethodsCount(); i++) {
                MethodInfo methodInfo = new MethodInfo();
                String accessFlagsHex = content.substring(point, (point = point + 4));
                methodInfo.setAccessFlags(AccessFlag.checkAccessFlags(hexToInt(accessFlagsHex)));

                String nameIndexHex = content.substring(point, (point = point + 4));
                methodInfo.setNameIndex(hexToInt(nameIndexHex));

                String descriptorIndexHex = content.substring(point, (point = point + 4));
                methodInfo.setDescriptorIndex(hexToInt(descriptorIndexHex));

                String attributesCountHex = content.substring(point, (point = point + 4));
                methodInfo.setAttributesCount(hexToInt(attributesCountHex));

                List<AttributeInfo> attributes = new ArrayList<>(methodInfo.getAttributesCount());
                for (int j = 0; j < methodInfo.getAttributesCount(); j++) {
                    AttributeInfo attributeInfo = new AttributeInfo();
                    String attributeNameIndexHex = content.substring(point, (point = point + 4));
                    attributeInfo.setAttributeNameIndex(hexToInt(attributeNameIndexHex));

                    String attributeLengthHex = content.substring(point, (point = point + 8));
                    attributeInfo.setAttributeLength(hexToInt(attributeLengthHex));

                    String infoHex = content.substring(point, (point = point + attributeInfo.getAttributeLength() * 2));
                    attributeInfo.setInfo(infoHex);

                    attributes.add(attributeInfo);
                }
                methodInfo.setAttributes(attributes);

                methods.add(methodInfo);
            }
            byteCode.setMethods(methods);

            point = printInfo(content, 4, point, attributesCountHex -> {
                int attributesCount = hexToInt(attributesCountHex);
                byteCode.setAttributesCount(attributesCount);
            });

            List<AttributeInfo> attributes = new ArrayList<>(byteCode.getAttributesCount());
            for (int i = 0; i < byteCode.getAttributesCount(); i++) {
                AttributeInfo attributeInfo = new AttributeInfo();
                String attributeNameIndexHex = content.substring(point, (point = point + 4));
                attributeInfo.setAttributeNameIndex(hexToInt(attributeNameIndexHex));

                String attributeLengthHex = content.substring(point, (point = point + 8));
                attributeInfo.setAttributeLength(hexToInt(attributeLengthHex));

                String infoHex = content.substring(point, (point = point + attributeInfo.getAttributeLength() * 2));
                attributeInfo.setInfo(infoHex);

                attributes.add(attributeInfo);
            }
            byteCode.setAttributes(attributes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String formatIndex(int index) {
        return (index >= 10 ? "  #" : "   #") + index;
    }

    private static String formatValueOrIndex(String type, String value) {
        String maxDistance =  "                    ";
        return maxDistance.substring(type.length()) + value;
    }

    private static String formatDescription(String pre, String description) {
        String maxDistance =  "                    ";
        return maxDistance.substring(pre.length()) + description;
    }

    private int printInfo(String content, int count, int point, Consumer<String> hexConsumer) {
        int newPoint = point + count;
        String hexString = content.substring(point, newPoint);
        hexConsumer.accept(hexString);
        return newPoint;
    }

    private static int hexToInt(String hex) {
        return Integer.valueOf(hex, 16);
    }

    private static long hexToLong(String hex) {
        return Long.valueOf(hex, 16);
    }

    public static void main(String[] args) {
        new MyByteCodeParser("/Users/macos/Documents/codes/mine/big-plan/base/commons/target/classes/com/gapache/commons/jvm/bytecode/MyByteCodeParser.class").parse();
    }

    private static ConstantInfoParser valueConstantInfoParse = (cpTag, point, context, byteCode) -> {
        String valueHex = context.substring(point, (point = point + cpTag.getBytesNumber() * 2));

        ValueConstantItem item = new ValueConstantItem();
        item.setTag(cpTag);
        Object value;
        switch (cpTag) {
            case CONSTANT_INTEGER_INFO:
                value = hexToInt(valueHex);
                break;
            case CONSTANT_FLOAT_INFO:
                value = Float.intBitsToFloat(hexToInt(valueHex));
                break;
            case CONSTANT_LONG_INFO:
                value = hexToLong(valueHex);
                break;
            case CONSTANT_DOUBLE_INFO:
                value = Double.longBitsToDouble(hexToLong(valueHex));
                break;
            case CONSTANT_UTF8_INFO: {
                int length = hexToInt(valueHex);
                String infoHex = context.substring(point, (point = point + length * 2));

                byte[] bytes = new byte[length];
                for (int i = 0; i < length; i++) {
                    String single = infoHex.substring(i * 2, i * 2 + 2);
                    bytes[i] = Integer.valueOf(hexToInt(single)).byteValue();
                }
                value = new String(bytes, StandardCharsets.UTF_8);
                break;
            }
            default:
                value = null;
        }
        item.setValue(value);
        byteCode.getConstantPool().put(byteCode.getConstantPool().size() + 1, item);
        return point;
    };

    private static ConstantInfoPrinter valueConstantInfoPrinter = (pool, index, item) -> {
        ValueConstantItem valueConstantItem = (ValueConstantItem) item;
        System.out.println(formatIndex(index) + " = " + item.getTag().getShowValue() +
                formatValueOrIndex(item.getTag().getShowValue(), valueConstantItem.getValue().toString()));
    };

    private static ConstantInfoPrinter classAndStringConstantInfoPrinter = (pool, index, item) -> {
        IndexConstantItem indexItem = (IndexConstantItem) item;
        int utf8InfoIndex = indexItem.getIndexes()[0];
        ConstantItem utf8Info = pool.get(utf8InfoIndex);
        indexItem.setDescription("// " + ((ValueConstantItem) utf8Info).getValue());
        System.out.println(formatIndex(index) + " = " + indexItem.getTag().getShowValue() +
                formatValueOrIndex(item.getTag().getShowValue(), "#" + utf8InfoIndex) +
                formatDescription("#" + utf8InfoIndex, indexItem.getDescription()));
    };

    private static ConstantInfoPrinter fieldAndMethodRefConstantInfoPrinter = (pool, index, item) -> {
        IndexConstantItem indexItem = (IndexConstantItem) item;
        int classIndex = indexItem.getIndexes()[0];
        int nameAndTypeIndex = indexItem.getIndexes()[1];
        IndexConstantItem classInfo = (IndexConstantItem) pool.get(classIndex);
        ValueConstantItem className = (ValueConstantItem) pool.get(classInfo.getIndexes()[0]);

        IndexConstantItem nameAndType = (IndexConstantItem) pool.get(nameAndTypeIndex);
        int nameIndex = nameAndType.getIndexes()[0];
        int descriptorIndex = nameAndType.getIndexes()[1];
        ValueConstantItem fieldName = (ValueConstantItem) pool.get(nameIndex);
        ValueConstantItem descriptor = (ValueConstantItem) pool.get(descriptorIndex);

        indexItem.setDescription("// " + className.getValue() + "." + fieldName.getValue() + ":" + descriptor.getValue());
        System.out.println(formatIndex(index) + " = " + indexItem.getTag().getShowValue() +
                formatValueOrIndex(indexItem.getTag().getShowValue(), "#" + classIndex + "." + "#" + nameAndTypeIndex) +
                formatDescription("#" + classIndex + "." + "#" + nameAndTypeIndex, indexItem.getDescription()));
    };

    private static ConstantInfoPrinter nameAndTypeConstantInfoPrinter = (pool, index, item) -> {
        IndexConstantItem indexItem = (IndexConstantItem) item;
        int nameIndex = indexItem.getIndexes()[0];
        int descriptorIndex = indexItem.getIndexes()[1];
        ValueConstantItem name = (ValueConstantItem) pool.get(nameIndex);
        ValueConstantItem type = (ValueConstantItem) pool.get(descriptorIndex);
        indexItem.setDescription("// " + name.getValue() + ":" + type.getValue());
        System.out.println(formatIndex(index) + " = " + indexItem.getTag().getShowValue() +
                formatValueOrIndex(indexItem.getTag().getShowValue(), "#" + nameIndex + ":" + "#" + descriptorIndex) +
                formatDescription("#" + nameIndex + ":" + "#" + descriptorIndex, indexItem.getDescription()));
    };

    private static ConstantInfoParser indexConstantInfoParser = (cpTag, point, context, byteCode) -> {
        String valueHex = context.substring(point, (point = point + cpTag.getBytesNumber() * 2));
        int indexCount = valueHex.length() / 4;
        int[] indexes = new int[indexCount];
        for (int i = 0; i < indexCount; i++) {
            indexes[i] = hexToInt(valueHex.substring(i * 4, i * 4 + 4));
        }

        IndexConstantItem item = new IndexConstantItem();
        item.setTag(cpTag);
        item.setIndexes(indexes);
        byteCode.getConstantPool().put(byteCode.getConstantPool().size() + 1, item);
        return point;
    };

    private static ConstantInfoParser methodHandleConstantInfoParser = (cpTag, point, context, byteCode) -> {
        String valueHex = context.substring(point, (point = point + cpTag.getBytesNumber() * 2));
        String referenceKindHex = valueHex.substring(0, 2);
        String referenceIndexHex = valueHex.substring(2);

        MethodHandleConstantItem item = new MethodHandleConstantItem();
        item.setReferenceKind(hexToInt(referenceKindHex));
        item.setReferenceIndex(hexToInt(referenceIndexHex));
        byteCode.getConstantPool().put(byteCode.getConstantPool().size() + 1, item);
        return point;
    };
}
