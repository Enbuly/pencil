package com.rain.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * FlowerMapper
 *
 * @author lazy cat
 * @since 2020-08-01
 **/
@Mapper
public interface FlowerMapper {

    @Update("update book set count = count + #{count} where id = #{id}")
    int update(@Param("id") int id, @Param("count") int count);
}
