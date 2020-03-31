package com.gapache.commons.jvm.bytecode.parse.attribute;

import com.gapache.commons.jvm.bytecode.parse.AbstractTableView;
import com.gapache.commons.jvm.bytecode.parse.ByteCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author HuSen
 * create on 2020/3/30 4:53 下午
 */
public class BootstrapMethodsAttributeInfo extends AbstractTableView {

    private List<List<String>> trs;

    @Override
    public void parsing(ByteCode byteCode) {
        String hexString = super.getInfo().toString();
        int rows = rows(hexString);
        int point = 4;
        trs = new ArrayList<>(rows);
        for (int i = 0; i < rows; i++) {
            List<String> tr = new ArrayList<>(3);
            tr.add(String.valueOf(i));
            trs.add(tr);
        }
    }

    @Override
    public void printing(ByteCode byteCode) {

    }

    @Override
    public int rowsHexLength() {
        return 2;
    }

    @Override
    public List<String> th() {
        return Arrays.asList("Nr.", "Bootstrap Method", "Arguments");
    }

    @Override
    public List<List<String>> trs() {
        return this.trs;
    }
}
