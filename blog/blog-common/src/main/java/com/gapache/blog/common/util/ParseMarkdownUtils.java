package com.gapache.blog.common.util;

import com.alibaba.fastjson.JSON;
import com.gapache.blog.common.model.constant.MarkdownItemType;
import com.gapache.blog.common.model.dto.*;
import com.gapache.commons.utils.IStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.FileCopyUtils;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HuSen
 * @since 2020/8/26 3:52 下午
 */
public class ParseMarkdownUtils {

    public static void main(String[] args) throws Exception {
        FileInputStream inputStream = new FileInputStream("/Users/husen/develop/codes/mine/be_richer/java/java知识总结之Type.md");
        byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);
        String s = IStringUtils.newString(bytes);
        List<MarkdownItemDTO> parse = parse(s);
        System.out.println(JSON.toJSONString(parse, true));
    }

    public static List<MarkdownItemDTO> parse(String content) {
        List<MarkdownItemDTO> items = new ArrayList<>();
        String[] lines = content.split(System.lineSeparator());
        int length = lines.length;
        for (int i = 0; i < length; i++) {
            String line = lines[i];
            // 目录自动生成
            MarkdownItemType type = checkType(line);
            MarkdownItemDTO markdownItemDTO = new MarkdownItemDTO();
            items.add(markdownItemDTO);
            markdownItemDTO.setOrder(items.size());
            markdownItemDTO.setType(type.name());
            switch (type) {
                case TITLE1:
                case TITLE2:
                case TITLE3:
                case TITLE4:
                case TITLE5:
                case TITLE6: {
                    String title = StringUtils.substringAfter(line, type.getStart());
                    markdownItemDTO.setContent(title.trim());
                    break;
                }
                case QUOTE: {
                    StringBuilder quote = new StringBuilder(StringUtils.substringAfter(line, type.getStart()));
                    i = parseNone(i, quote, lines);
                    markdownItemDTO.setContent(quote.toString());
                    break;
                }
                case CODE_BLACK2:
                case CODE_BLACK: {
                    StringBuilder code = new StringBuilder();
                    while (true) {
                        i++;
                        if (i > length - 1) {
                            break;
                        }
                        String codeLine = lines[i];
                        if (StringUtils.equals(codeLine, type.getStart())) {
                            break;
                        }
                        code.append(codeLine).append("\r\n");
                    }

                    markdownItemDTO.setContent(StringUtils.substringBeforeLast(code.toString(), "\r\n"));
                    break;
                }
                case UNSORTED_LIST:
                case SORTED_LIST: {
                    i = parseList(lines, i, line, type, markdownItemDTO);
                    break;
                }
                case LINE: {
                    markdownItemDTO.setContent(type.getStart());
                    break;
                }
                case IMAGE:
                case SUPER_LINK: {
                    parseLink(line, type, markdownItemDTO);
                    break;
                }
                case NONE: {
                    if (StringUtils.isBlank(line)) {
                        items.remove(markdownItemDTO);
                        break;
                    }
                    StringBuilder none = new StringBuilder(line);
                    i = parseNone(i, none, lines);
                    markdownItemDTO.setContent(none.toString());
                    break;
                }
                default:
            }
        }
        return items;
    }

    private static int parseNone(int i, StringBuilder quote, String[] lines) {
        while (true) {
            i++;
            if (i > lines.length - 1) {
                break;
            }
            String next = lines[i];
            if (!checkType(next).equals(MarkdownItemType.NONE) || StringUtils.isBlank(next)) {
                i--;
                break;
            }
            quote.append(next);
        }
        return i;
    }

    private static void parseLink(String line, MarkdownItemType type, MarkdownItemDTO markdownItemDTO) {
        String[] split = StringUtils.split(type.getStart(), "^_^");
        String name = StringUtils.substringBetween(line, split[0], split[1]);
        String link = StringUtils.substringBetween(line, "(", ")");
        MarkdownLinkDTO markdownLinkDTO = new MarkdownLinkDTO();
        markdownLinkDTO.setName(name);
        markdownLinkDTO.setLink(link);
        markdownItemDTO.setContent(markdownLinkDTO);
    }

    private static int parseList(String[] lines, int i, String line, MarkdownItemType type, MarkdownItemDTO markdownItemDTO) {
        MarkdownListDTO listDTO = new MarkdownListDTO();
        markdownItemDTO.setContent(listDTO);
        List<ListItemDTO> list = new ArrayList<>();
        listDTO.setItems(list);
        String item = StringUtils.substringAfter(line, type.getStart());
        ListItemDTO listItemDTO = new ListItemDTO();
        list.add(listItemDTO);
        listItemDTO.setOrder(list.size());
        listItemDTO.setText(item);
        switch (type) {
            case UNSORTED_LIST: {
                while (true) {
                    i++;
                    if (i > lines.length - 1) {
                        break;
                    }
                    String itemLine = lines[i];
                    if (!checkType(itemLine).equals(MarkdownItemType.UNSORTED_LIST)) {
                        i--;
                        break;
                    }
                    ListItemDTO dto = new ListItemDTO();
                    dto.setText(StringUtils.substringAfter(itemLine, type.getStart()).trim());
                    list.add(dto);
                    dto.setOrder(list.size());
                }
                break;
            }
            case SORTED_LIST: {
                int start = 2;
                while (true) {
                    i++;
                    if (i > lines.length - 1) {
                        break;
                    }
                    String itemLine = lines[i];
                    String listStart = start + ". ";
                    if (StringUtils.isNotBlank(itemLine) && itemLine.trim().startsWith(listStart)) {
                        String innerItem = StringUtils.substringAfter(itemLine, listStart);
                        start++;
                        ListItemDTO dto = new ListItemDTO();
                        dto.setText(innerItem);
                        list.add(dto);
                        dto.setOrder(list.size());
                    } else {
                        i--;
                        break;
                    }
                }
                break;
            }
            default:
        }
        return i;
    }

    private static MarkdownItemType checkType(String text) {
        MarkdownItemType[] types = MarkdownItemType.values();
        MarkdownItemType textType = MarkdownItemType.NONE;
        for (MarkdownItemType type : types) {
            if (type.equals(MarkdownItemType.NONE)) {
                continue;
            }
            if (type.getChecker().apply(text)) {
                textType = type;
            }
        }
        return textType;
    }
}
