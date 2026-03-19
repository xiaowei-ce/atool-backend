package org.example.atool.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.atool.entity.po.EPayOrderRecord;

@Mapper
public interface EPayOrderRecordMapper {

    void addRecord(@Param("ePayOrderRecord") EPayOrderRecord ePayOrderRecord);
}
