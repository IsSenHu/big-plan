package com.gapache.commons.jvm.bytecode.parse;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author HuSen
 * create on 2020/3/30 4:50 下午
 */
public abstract class AbstractTableView {

    private static final String SPACING = "  ";

    public abstract int rowsHexLength();

    public abstract List<String> th();

    public abstract Map<Integer, List<String>> trs();

    public int rows(String attributeInfo) {
        return Utils.hexToInt(attributeInfo.substring(0, rowsHexLength() * 2));
    }

    public void draw() {
        // 找出每一列最长的内容 方便绘制
        Map<Integer, Integer> maxTdWidthColumn = new HashMap<>(th().size());
        for (int i = 0; i < th().size(); i++) {
            String head = th().get(i);
            int max = head.length();
            for (Map.Entry<Integer, List<String>> entry : trs().entrySet()) {
                int length = entry.getValue().get(i).length();
                if (length > max) {
                    max = length;
                }
            }
            maxTdWidthColumn.put(i, max);
        }

        // 绘制表头
        StringBuilder rowLineBuilder = new StringBuilder("|");
        for (int i = 0; i < th().size(); i++) {
            int tdWidth = maxTdWidthColumn.get(i) + SPACING.length() * 2;
            for (int j = 0; j < tdWidth; j++) {
                rowLineBuilder.append("-");
            }
            rowLineBuilder.append("|");
        }
        String rowLine = rowLineBuilder.toString();
        System.out.println(rowLine);

        drawRow(maxTdWidthColumn, rowLine, th());

        // 绘制表格数据
        for (Map.Entry<Integer, List<String>> entry : trs().entrySet()) {
            List<String> tds = entry.getValue();
            drawRow(maxTdWidthColumn, rowLine, tds);
        }
    }

    private void drawRow(Map<Integer, Integer> maxTdWidthColumn, String rowLine, List<String> th) {
        StringBuilder heads = new StringBuilder("|");
        for (int i = 0; i < th.size(); i++) {
            int maxTdWidth = maxTdWidthColumn.get(i);
            String head = th.get(i);
            int difference = maxTdWidth - head.length();
            heads.append(SPACING).append(drawSpacing(difference / 2))
                    .append(head).append(drawSpacing(difference - (difference / 2))).append(SPACING).append("|");
        }
        System.out.println(heads);
        System.out.println(rowLine);
    }

    private String drawSpacing(int count) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append(" ");
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        new AbstractTableView() {
            @Override
            public int rowsHexLength() {
                return 0;
            }

            @Override
            public List<String> th() {
                List<String> th = new ArrayList<>();
                th.add("ID");
                th.add("name");
                th.add("birthday");
                return th;
            }

            @Override
            public Map<Integer, List<String>> trs() {
                Map<Integer, List<String>> trs = new HashMap<>();
                List<String> tr1 = new ArrayList<>();
                tr1.add("0");
                tr1.add("HuSen");
                tr1.add("1995-05-28");
                trs.put(0, tr1);

                List<String> tr2 = new ArrayList<>();
                tr2.add("1");
                tr2.add("SamSinge");
                tr2.add("1976-05-28");
                trs.put(1, tr2);
                return trs;
            }
        }.draw();
    }
}
