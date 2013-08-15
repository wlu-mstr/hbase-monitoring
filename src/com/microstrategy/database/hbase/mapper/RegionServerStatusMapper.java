/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microstrategy.database.hbase.mapper;

import com.microstrategy.database.hbase.model.RegionServerStatus;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 * create table RTRS (ts timestamp, metrix longtext, primary key(ts)); create
 * table RSSTATUS (ts timestamp, metrix longtext, primary key(ts)); create table
 * STATUSSIMPLE (st varchar(10), metrix longtext, primary key(st));
 *
 * @author wlu
 */
public interface RegionServerStatusMapper {

    final String INSERT = "REPLACE INTO RSSTATUS (ts, metrix) "
            + "VALUES ( #{obj.timestamp}, #{obj.metrics})";

    @Insert(INSERT)
    @Options(useGeneratedKeys = true, keyProperty = "obj.timestamp")
    void insert(@Param("obj") RegionServerStatus rtrs);
    final String SELECT_BY_ID = "SELECT * FROM RSSTATUS WHERE ts = #{sel_ts}";
    final String SAVELIVEDEAD = "REPLACE INTO STATUSSIMPLE (st, METRIX) VALUES ('id', #{obj.metricsLiveDead})";

    @Insert(SAVELIVEDEAD)
    void insertSimple(@Param("obj") RegionServerStatus rtrs);

    @Select(SELECT_BY_ID)
    @Results(value = {
        @Result(property = "timestamp", column = "ts"),
        @Result(property = "metrics", column = "metrix")})
    RegionServerStatus selectByTS(@Param("sel_ts") String ts);
    final String SELECT_RECENT_N = "SELECT * FROM RSSTATUS ORDER BY ts DESC LIMIT #{limit}";

    @Select(SELECT_RECENT_N)
    @Results(value = {
        @Result(property = "timestamp", column = "ts"),
        @Result(property = "metrics", column = "metrix")})
    List<RegionServerStatus> selectRecent(@Param("limit") Integer limit);
    final String SELECT_SIMPLE = "Select metrix from STATUSSIMPLE limit 1";

    @Select(SELECT_SIMPLE)
    String selectRecentSimple();
    final String SELECT_COUNT = "SELECT COUNT(*) FROM RSSTATUS";

    @Select(SELECT_COUNT)
    int getCount();
    final String SELECT_ALL = "SELECT * FROM RSSTATUS";

    @Select(SELECT_ALL)
    @Results(value = {
        @Result(property = "timestamp", column = "ts"),
        @Result(property = "metrics", column = "metrix")})
    List<RegionServerStatus> selectAll();
    
    final String SELECT_AFTER = "SELECT * FROM RSSTATUS WHERE TS > #{checkpoint}";
    @Select(SELECT_AFTER)
    @Results(value = {
        @Result(property = "timestamp", column = "ts"),
        @Result(property = "metrics", column = "metrix")})
    List<RegionServerStatus> selectAfter(@Param("checkpoint") String checkpoint);
}
