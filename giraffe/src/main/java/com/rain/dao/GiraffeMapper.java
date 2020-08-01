package com.rain.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * GiraffeMapper
 *
 * @author lazy cat
 * @since 2020-08-01
 **/
@Mapper
public interface GiraffeMapper {

    @Update("update book set count = count + #{count} where id = #{id}")
    int update(@Param("id") int id, @Param("count") int count);
}
