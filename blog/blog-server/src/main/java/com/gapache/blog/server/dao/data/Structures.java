package com.gapache.blog.server.dao.data;

import com.gapache.redis.BaseDataStructure;

/**
 * @author HuSen
 * create on 2020/4/8 23:04
 */
public class Structures {

    public static final BaseDataStructure<String> BLOG = new BaseDataStructure<String>() {
        @Override
        protected String prefix() {
            return "Blog:Blog:";
        }
    };

    public static final BaseDataStructure<String> CONTENT = new BaseDataStructure<String>() {
        @Override
        protected String prefix() {
            return "Blog:Content:";
        }
    };

    public static final BaseDataStructure<String> IDS = new BaseDataStructure<String>() {
        @Override
        protected String prefix() {
            return "Blog:Ids";
        }
    };

    public static final BaseDataStructure<String> ABOUT = new BaseDataStructure<String>() {
        @Override
        protected String prefix() {
            return "Blog:About";
        }
    };

    public static final BaseDataStructure<String> CATEGORIES = new BaseDataStructure<String>() {
        @Override
        public String prefix() {
            return "Blog:Categories";
        }
    };

    public static final BaseDataStructure<String> TAGS = new BaseDataStructure<String>() {
        @Override
        protected String prefix() {
            return "Blog:Tags";
        }
    };

    public static final BaseDataStructure<String> VIEWS = new BaseDataStructure<String>() {
        @Override
        protected String prefix() {
            return "Blog:Views";
        }
    };
}
