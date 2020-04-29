package com.gapache.jpa;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author HuSen
 * create on 2020/4/29 3:47 下午
 */
public class EntityAuditorAware<OP_MAN extends Serializable> implements AuditorAware<OP_MAN> {

    @NonNull
    @Override
    public Optional<OP_MAN> getCurrentAuditor() {
        return Optional.empty();
    }
}
