package com.gapache.security.holder;

import com.gapache.security.interfaces.AccessCardHolderStrategy;
import com.gapache.security.model.AccessCard;

/**
 * @author HuSen
 * @since 2020/8/9 6:26 下午
 */
public class AccessCardHolder {

    private static AccessCardHolderStrategy strategy = new InheritableThreadLocalAccessCardHolderStrategy();

    public static void clearContext() {
        strategy.clearContext();
    }

    public static AccessCard getContext() {
        return strategy.getContext();
    }

    public static void setContext(AccessCard context) {
        strategy.setContext(context);
    }

    public static AccessCard createEmptyContext() {
        return strategy.createEmptyContext();
    }
}
