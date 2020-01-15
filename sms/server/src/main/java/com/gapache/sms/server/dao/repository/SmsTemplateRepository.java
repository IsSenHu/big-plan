package com.gapache.sms.server.dao.repository;

import com.gapache.sms.server.dao.po.SmsTemplatePO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author HuSen
 * create on 2020/1/15 14:36
 */
public interface SmsTemplateRepository extends JpaRepository<SmsTemplatePO, Long> {

    SmsTemplatePO findByTemplateCode(String templateCode);
}
