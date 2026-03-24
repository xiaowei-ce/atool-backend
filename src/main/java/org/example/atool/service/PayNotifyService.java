package org.example.atool.service;

import org.example.atool.entity.dto.PayNotifyDTO;
import org.springframework.transaction.annotation.Transactional;

public interface PayNotifyService {
    @Transactional(rollbackFor = {Error.class, Exception.class})
    void payment(PayNotifyDTO dto);
}
