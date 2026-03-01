package org.example.atool.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.atool.entity.vo.RecordVO;
import org.example.atool.entity.po.Record;

import java.util.List;

@Mapper
public interface RecordMapper {
    void add(@Param("record") Record record);

    List<RecordVO> pageGet(@Param("userId") Long userId, @Param("now") Integer now, @Param("size") Integer size);
}
