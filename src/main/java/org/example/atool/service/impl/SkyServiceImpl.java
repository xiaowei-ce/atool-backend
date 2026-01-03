package org.example.atool.service.impl;

import cn.hutool.json.JSONObject;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.example.atool.HttpClient.SkyApiClient;
import org.example.atool.mapper.UserDetailMapper;
import org.example.atool.props.SkyApiProp;
import org.example.atool.service.SkyService;
import org.example.atool.utils.Converter.TextParser;
import org.example.atool.utils.JSONUtil;
import org.example.atool.utils.PrincipalUtil;
import org.example.atool.utils.Throw;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

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


        Map<String, String> data = TextParser.hutoolParseTextContent(dataStr);

        boolean hasHeightData = data.containsKey("身高解析结果_体型值") ||
                data.containsKey("身高解析结果_身高值") ||
                data.containsKey("当前角色服装_发型");

        boolean hasOutfitData = data.containsKey("当前角色服装_发型") ||
                data.containsKey("当前角色服装_面具");

        if (hasHeightData || hasOutfitData) {
            userDetailMapper.deductPoint(PrincipalUtil.user().getId(),100);
            //todo
        }

        return dataStr;
    }
}
