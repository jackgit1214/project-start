package com.project.core.mybatis.dao.Base;

import java.util.Map;

import com.project.core.mybatis.model.CommSeq;
import org.apache.ibatis.annotations.Mapper;

import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CommSeqMapper extends IDataMapperByPage<CommSeq>,
        IDataMapperCRUD<CommSeq> {
    /**
     * 根据序列名，取得当前值
     *
     * @param sequenceName 序列名
     * @return 指定序列的当前值，key 序列名，value为值
     */
    Map<String, Integer> getCurrVal(String sequenceName);

    /**
     * 根据序列名，取得下一个值
     *
     * @param sequenceName 序列名
     * @return 指定序列的下一个值，key 序列名，value为值 <br>
     * 取得序列下一个值时，序列当前值发生更改
     */

    Map<String, Integer> getNextVal(String sequenceName);

}