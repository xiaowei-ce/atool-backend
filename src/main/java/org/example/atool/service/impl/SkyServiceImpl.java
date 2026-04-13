package org.example.atool.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.dtflys.forest.http.ForestResponse;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.example.atool.HttpClient.SkyApiClient;
import org.example.atool.entity.po.Record;
import org.example.atool.entity.po.UserDetail;
import org.example.atool.entity.vo.SkyGiftVO;
import org.example.atool.mapper.RecordMapper;
import org.example.atool.mapper.UserDetailMapper;
import org.example.atool.service.SkyService;
import org.example.atool.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SkyServiceImpl implements SkyService {

    private final UserDetailMapper userDetailMapper;
    private final RecordMapper recordMapper;
    private final RedisClient redisClient;


    @Resource
    private SkyApiClient skyApiClient;

    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public String data(String id) {
        Long userId = PrincipalUtil.user().getId();
        RedisLock lock = new RedisLock("sky:"+userId);
        if (!lock.tryLock()) {
            Throw.BizExp("并发请求!");
        }
        try {
            UserDetail detail = userDetailMapper.getByUserId(userId);
            Long points = detail.getPoints();
            if (points < 50) {
                Throw.BizExp("积分不足!");
            }

            //这里是api供应商太傻逼了，正常数据用text报错json和text混用....
            ForestResponse<String> dataResponse = skyApiClient.data(id);
            String responseContent = dataResponse.getContent();
            if (StrUtil.isBlank(responseContent)) {
                Throw.BizExp("查询到了空白数据");
            }
            if (dataResponse.getContentType().isJson()) {
                JSONObject parsed = JSONUtil.parseObj(responseContent);
                if (parsed.containsKey("msg")) {
                    Throw.BizExp(parsed.getStr("msg"));
                } else {
                    Throw.BizExp(parsed.toString());
                }
            }
            double similar = StrUtil.similar(responseContent, """
                    身高解析结果：
                    体型值: -0.04459
                    身高值: -1.94260
                    最高身高: 1.97010
                    最矮身高: 13.97010
                    当前身高: 13.79790
                    当前身高描述: 非常矮身高
                    距离最低还差: 0.1722
                    光崽是较瘦体型
                    
                    当前角色服装：
                    发型: 樱花发型
                    面具: 黑脸面具
                    发饰: 未穿戴
                    斗篷: 樱花斗篷
                    背饰: 长笛
                    裤子: 武士裤
                    查询耗时: 1.65687 s
                    查询时间：15时35分25秒""");
            if (!(similar > 0.5D && StrUtil.containsAll(responseContent, "身高解析结果", "当前角色服装", "查询耗时", "查询时间", "身高值", "当前身高"))) {
                Throw.BizExp(responseContent);
            }

            responseContent = StrUtil.replace(responseContent, "：", ":");
            Long cast = 500L;
            detail.setPoints(detail.getPoints() - cast);
            detail.setCount(detail.getCount() + 1);
            userDetailMapper.update(detail);

            org.example.atool.entity.po.Record record = new Record();
            record.setUserId(userId);
            record.setTypeId(1L);
            record.setTime(Timestamp.valueOf(LocalDateTime.now()));
            record.setAbstr("查询身高");
            record.setDetail(responseContent);
            record.setChange(-cast);
            recordMapper.add(record);

            redisClient.del(StrUtil.format("cache:user_detail:{}", userId));
            redisClient.del(StrUtil.format("cache:records:{}", userId));
            return responseContent;
        }finally {
            lock.unlock();
        }
    }

    @Override
    public SkyGiftVO gift(String id) {
        ForestResponse<String> giftResponse = skyApiClient.gift(id);
        String responseContent = giftResponse.getContent();
        if (StrUtil.isBlank(responseContent)){
            Throw.BizExp("查询到了空白数据");
        }
        if (!giftResponse.getContentType().isJson()) {
            Throw.BizExp(responseContent);
        }
        JSONObject parsed = JSONUtil.parseObj(responseContent);
        if (!parsed.containsKey("totalCount")) {
            if (parsed.containsKey("error")){
                Throw.BizExp(parsed.getStr("error"));
            }else {
                Throw.BizExp(parsed.toString());
            }
        }
        return JSONUtil.toBean(parsed, SkyGiftVO.class);
    }
}
