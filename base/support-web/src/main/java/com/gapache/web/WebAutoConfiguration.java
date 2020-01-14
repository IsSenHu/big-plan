package com.gapache.web;

import com.gapache.commons.model.IException;
import com.gapache.commons.model.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author HuSen
 * create on 2020/1/14 17:54
 */
@Slf4j
public class WebAutoConfiguration {

    @ExceptionHandler(Exception.class)
    public JsonResult<Object> exceptionHandler(Exception e) {
        log.error("请求发生异常:", e);
        if (e instanceof IException) {
            IException iE = (IException) e;
            return JsonResult.of(iE.getError());
        } else {
            return JsonResult.of(999999999, "未知异常");
        }
    }
}
