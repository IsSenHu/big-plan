package com.gapache.security.cache;

import com.gapache.security.annotation.AuthResource;

import java.util.*;

/**
 * @author HuSen
 * @since 2020/8/6 4:51 下午
 */
public class AuthResourceCache extends HashMap<String, List<AuthResource>> {
    private static final long serialVersionUID = -6469356799640120224L;

    private final Set<AuthResource> all;

    public AuthResourceCache(int initialCapacity) {
        super(initialCapacity);
        this.all = new HashSet<>(initialCapacity * 4);
    }

    private static final AuthResourceCache INSTANCE = new AuthResourceCache(256);

    public static List<AuthResource> check(String category) {
        return INSTANCE.computeIfAbsent(category, key -> new ArrayList<>());
    }

    public static void put(String category, AuthResource authResource) {
        check(category).add(authResource);
        INSTANCE.all.add(authResource);
    }

    public static Set<String> categories() {
        return INSTANCE.keySet();
    }

    public static Set<AuthResource> getAll() {
        return INSTANCE.all;
    }
}
