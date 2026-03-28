package org.example.atool.service.impl;


import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import org.example.atool.entity.dto.PayNotifyDTO;
import org.example.atool.entity.po.EPayOrderRecord;
import org.example.atool.entity.po.Order;
import org.example.atool.entity.po.Record;
import org.example.atool.mapper.EPayOrderRecordMapper;
import org.example.atool.mapper.OrderMapper;
import org.example.atool.mapper.RecordMapper;
import org.example.atool.mapper.UserDetailMapper;
import org.example.atool.props.EPayProp;
import org.example.atool.service.PayNotifyService;
import org.example.atool.utils.RedisClient;
import org.example.atool.utils.SignUtil;
import org.example.atool.utils.Throw;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@AllArgsConstructor
public class PayNotifyServiceImpl implements PayNotifyService {

    private final OrderMapper orderMapper;
    private final UserDetailMapper userDetailMapper;
    private final RecordMapper recordMapper;
    private final EPayOrderRecordMapper ePayOrderRecordMapper;
    private final EPayProp ePayProp;
    private final RedisClient redisClient;

    @Transactional(rollbackFor = {Error.class, Exception.class})
    @Override
    public void payment(PayNotifyDTO dto) {
        if (Objects.nonNull(dto)){
            String md5Sign = SignUtil.md5Sign(dto, ePayProp.getKey());
            if (!StrUtil.equals(md5Sign,dto.getSign())){
                Throw.BizExp("签名校验失败！");
            }
            if (!StrUtil.equals(dto.getTrade_status(),"TRADE_SUCCESS")){
                Throw.BizExp("支付未成功！");
            }
        }

        Order order = orderMapper.orderById(dto.getOut_trade_no());

        if (Objects.isNull(order)){
            Throw.BizExp("订单号不存在！");
        }
        if (Objects.equals(order.getStatus(),Order.PAYED)){
//            Throw.BizExp("该订单已支付！");
            return;
        }
        if (!Objects.equals(order.getStatus(),Order.PENDING)){
            Throw.BizExp("订单状态异常，请联系客服");
        }
        orderMapper.markStatus(order.getId(),Order.PAYED);

        Long totalPoints = orderMapper.totalPoints(order.getId());
        userDetailMapper.changePoints(order.getCreateBy(),totalPoints);

        EPayOrderRecord ePayOrderRecord = new EPayOrderRecord();
        ePayOrderRecord.setPayType(dto.getType());
        ePayOrderRecord.setOrderId(dto.getOut_trade_no());
        ePayOrderRecord.setEpayNo(dto.getTrade_no());
        ePayOrderRecord.setEpayStatus(dto.getTrade_status());
        ePayOrderRecordMapper.addRecord(ePayOrderRecord);

        Record record = new Record();
        record.setUserId(order.getCreateBy());
        record.setTypeId(Record.RECHARGE);
        record.setChange(totalPoints);
        record.setTime(Timestamp.valueOf(LocalDateTime.now()));
        record.setAbstr("购买积分");
        record.setDetail(StrUtil.format("订单号: {}\n金额: {}RMB\n积分: {}",order.getId(), order.getAmount(),totalPoints));

        redisClient.del(StrUtil.format("cache:user_detail:{}",order.getCreateBy()));
        redisClient.del(StrUtil.format("cache:records:{}",order.getCreateBy()));

        recordMapper.add(record);
    }
}
