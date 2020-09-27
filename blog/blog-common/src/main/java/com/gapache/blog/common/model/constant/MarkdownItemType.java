package com.gapache.blog.common.model.constant;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

/**
 * @author HuSen
 * @since 2020/8/26 3:17 下午
 */
@Getter
public enum MarkdownItemType {
    //
    NONE("", null),
    QUOTE("> ", text -> StringUtils.isNotBlank(text) && text.trim().startsWith("> ")),
    UNSORTED_LIST("- ", text -> StringUtils.isNotBlank(text) && text.trim().startsWith("- ")),
    SORTED_LIST("1. ", text -> StringUtils.isNotBlank(text) && text.trim().startsWith("1. ")),
    TASK_SELECTED_LIST("- [x] ", text -> StringUtils.isNotBlank(text) && text.trim().startsWith("- [x] ")),
    TASK_UNSELECTED_LIST("- [ ] ", text -> StringUtils.isNotBlank(text) && text.trim().startsWith("- [ ] ")),
    TITLE1("# ", text -> StringUtils.isNotBlank(text) && text.trim().startsWith("# ")),
    TITLE2("## ", text -> StringUtils.isNotBlank(text) && text.trim().startsWith("## ")),
    TITLE3("### ", text -> StringUtils.isNotBlank(text) && text.trim().startsWith("### ")),
    TITLE4("#### ", text -> StringUtils.isNotBlank(text) && text.trim().startsWith("#### ")),
    TITLE5("##### ", text -> StringUtils.isNotBlank(text) && text.trim().startsWith("##### ")),
    TITLE6("###### ", text -> StringUtils.isNotBlank(text) && text.trim().startsWith("###### ")),
    CODE_LINE("`", text -> StringUtils.isNotBlank(text) && text.trim().startsWith("`")),
    CODE_BLACK("~~~", text -> StringUtils.isNotBlank(text) && text.trim().startsWith("~~~")),
    CODE_BLACK2("```", text -> StringUtils.isNotBlank(text) && text.trim().startsWith("```")),
    FORMULA("$$", text -> StringUtils.isNotBlank(text) && text.trim().startsWith("$$")),
    TABLE("|", text -> StringUtils.isNotBlank(text) && text.trim().startsWith("|") && text.trim().endsWith("|")),
    LINK_QUOTE("[", text -> StringUtils.isNotBlank(text) && text.trim().startsWith("[")),
    FOOTNOTE("[^", text -> StringUtils.isNotBlank(text) && text.trim().startsWith("[^")),
    LINE("______", text -> StringUtils.equals("______", text.trim())),
    BOLD("**", text -> StringUtils.isNotBlank(text) && text.trim().startsWith("**")),
    ITALIC("*", text -> StringUtils.isNotBlank(text) && text.trim().startsWith("*") && !text.trim().startsWith("**")),
    U("<u>", text -> StringUtils.isNotBlank(text) && text.trim().startsWith("<u>")),
    DELETE("~~", text -> StringUtils.isNotBlank(text) && text.trim().startsWith("~~") && !text.trim().startsWith("~~~")),
    DOC("<!--", text -> StringUtils.isNotBlank(text) && text.trim().startsWith("<!--")),
    SUPER_LINK("[^_^](", text -> {
        if (StringUtils.isBlank(text)) {
            return false;
        }

        String[] signs = StringUtils.split("[^_^](", "^_^");
        if (!text.trim().startsWith(signs[0])) {
            return false;
        }
        return text.contains(signs[1]);
    }),
    IMAGE("![^_^](", text -> {
        if (StringUtils.isBlank(text)) {
            return false;
        }

        String[] signs = StringUtils.split("![^_^](", "^_^");
        if (!text.trim().startsWith(signs[0])) {
            return false;
        }
        return text.contains(signs[1]);
    });
    private final String start;
    private final Function<String, Boolean> checker;

    MarkdownItemType(String start, Function<String, Boolean> checker) {
        this.start = start;
        this.checker = checker;
    }
}
