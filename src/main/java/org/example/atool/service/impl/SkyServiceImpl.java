package org.example.atool.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.example.atool.HttpClient.SkyApiClient;
import org.example.atool.mapper.UserDetailMapper;
import org.example.atool.props.SkyApiProp;
import org.example.atool.service.SkyService;
import org.example.atool.utils.JSONUtil;
import org.example.atool.utils.PrincipalUtil;
import org.example.atool.utils.Throw;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SkyServiceImpl implements SkyService {

    @Resource
    private SkyApiClient skyApiClient;

    private final SkyApiProp skyApiProp;
    private final UserDetailMapper userDetailMapper;

    @Override
    @Transactional
    public String data(String code) {
        String dataStr = skyApiClient.data(skyApiProp.getKey(), code);
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


//        Map<String, String> data = TextParser.hutoolParseTextContent(dataStr);
//        boolean hasHeightData = data.containsKey("身高解析结果_体型值") ||
//                data.containsKey("身高解析结果_身高值") ||
//                data.containsKey("当前角色服装_发型");
//
//        boolean hasOutfitData = data.containsKey("当前角色服装_发型") ||
//                data.containsKey("当前角色服装_面具");
//        if (hasHeightData && hasOutfitData) {
//            userDetailMapper.deductPoint(PrincipalUtil.user().getId(),120);
//            //todo
//        }

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
            userDetailMapper.deductPoint(PrincipalUtil.user().getId(),120);
        }else{
            Throw.BizExp(dataStr);
        }

        return dataStr;
    }
}
