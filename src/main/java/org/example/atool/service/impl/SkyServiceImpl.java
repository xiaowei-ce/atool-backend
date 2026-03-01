package org.example.atool.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.example.atool.HttpClient.SkyApiClient;
import org.example.atool.entity.po.Record;
import org.example.atool.entity.po.UserDetail;
import org.example.atool.mapper.RecordMapper;
import org.example.atool.mapper.UserDetailMapper;
import org.example.atool.props.SkyApiProp;
import org.example.atool.service.SkyService;
import org.example.atool.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SkyServiceImpl implements SkyService {

    private final SkyApiProp skyApiProp;
    private final UserDetailMapper userDetailMapper;
    private final RecordMapper recordMapper;


    @Resource
    private SkyApiClient skyApiClient;

    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public String data(String id) {

        if(!RegexUtil.matchAny(id,"^[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{4}$","^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")){
            Throw.BizExp("请检查输入格式是否正确！");
        }
        Long userId = PrincipalUtil.user().getId();
        UserDetail detail = userDetailMapper.getByUserId(userId);
        Long points = detail.getPoints();
        if(points < 50){
            Throw.BizExp("积分不足!");
        }

        String dataStr = skyApiClient.data(id); //todo

//        String dataStr = """
//                身高解析结果：
//                体型值: -0.04459
//                身高值: -1.94260
//                最高身高: 1.97010
//                最矮身高: 13.97010
//                当前身高: 13.79790
//                当前身高描述: 非常矮身高
//                距离最低还差: 0.1722
//                光崽是较瘦体型
//
//                当前角色服装：
//                发型: 樱花发型
//                面具: 黑脸面具
//                发饰: 未穿戴
//                斗篷: 樱花斗篷
//                背饰: 长笛
//                裤子: 武士裤
//                查询耗时: 1.65687 s
//                查询时间：15时35分25秒""";

        //这里是api供应商太傻逼了，正常数据用text报错用json....
        if (JSONUtil.isTypeJSON(dataStr)) {
            JSONObject object = JSONUtil.parseObj(dataStr);
            if(object.getInt("code") != 200) {
                String msg = object.getStr("msg");
                Throw.BizExp(msg);
            }else {
                Throw.BizExp("api出错，请联系管理员");
            }
        }

        double similar = StrUtil.similar(dataStr, """
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
        if(similar > 0.5D && StrUtil.containsAll(dataStr,"身高解析结果","当前角色服装","查询耗时","查询时间","身高值","当前身高")){
            dataStr = StrUtil.replace(dataStr, "：", ":");
            Long cast = 50L;
            detail.setPoints(detail.getPoints() - cast);
            detail.setCount(detail.getCount() + 1);
            userDetailMapper.update(detail);

            Record record = new Record();
            record.setUserId(userId);
            record.setTypeId(1L);
            record.setTime(Timestamp.valueOf(LocalDateTime.now()));
            record.setAbstr("查询身高");
            record.setDetail(dataStr);
            record.setChange(-cast);
            recordMapper.add(record);
        }else{
            Throw.BizExp(dataStr);
        }
        return dataStr;
    }
}
