package com.gapache.commons.jvm.bytecode.parse.attribute;

import com.gapache.commons.jvm.bytecode.parse.AbstractTableView;
import com.gapache.commons.jvm.bytecode.parse.ByteCode;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/3/30 4:53 下午
 */
public class InnerClassesAttributeInfo extends AbstractTableView {

    @Override
    public void parsing(ByteCode byteCode) {

    }

    @Override
    public void printing(ByteCode byteCode) {
        
    }

    @Override
    public int rowsHexLength() {
        return 0;
    }

    @Override
    public List<String> th() {
        return null;
    }

    @Override
    public List<List<String>> trs() {
        return null;
    }


}
