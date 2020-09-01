package com.gapache.cloud.money.management.server.controller;

import com.gapache.cloud.money.management.common.model.MoneyManagementError;
import com.gapache.cloud.money.management.common.model.TransactionDTO;
import com.gapache.cloud.money.management.server.service.TransactionService;
import com.gapache.cloud.money.management.server.util.CsvUtils;
import com.gapache.commons.model.IPageRequest;
import com.gapache.commons.model.JsonResult;
import com.gapache.commons.model.PageResult;
import com.gapache.security.annotation.AuthResource;
import com.gapache.security.annotation.NeedAuth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author HuSen
 * @since 2020/8/11 10:23 上午
 */
@Slf4j
@RestController
@NeedAuth("Transaction")
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/upload")
    @AuthResource(scope = "upload", name = "上传交易数据")
    public JsonResult<Boolean> upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        log.info("上传的文件名为:{}", originalFilename);
        if (StringUtils.isBlank(originalFilename)) {
            return JsonResult.of(MoneyManagementError.FILE_EMPTY);
        }

        List<TransactionDTO> read = CsvUtils.read(file.getInputStream());
        transactionService.save(read);
        return JsonResult.of(true);
    }

    @PostMapping("/page")
    @AuthResource(scope = "page", name = "分页查询交易数据")
    public JsonResult<PageResult<TransactionDTO>> page(IPageRequest<TransactionDTO> iPageRequest) {
        return JsonResult.of(transactionService.page(iPageRequest));
    }
}
