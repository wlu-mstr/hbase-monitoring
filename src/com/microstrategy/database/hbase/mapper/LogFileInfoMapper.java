package com.microstrategy.database.hbase.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.microstrategy.database.hbase.model.LogFileInfo;

public interface LogFileInfoMapper {
	final String INSERT = "REPLACE INTO rs_log (server, url, check_point, update_time, status) VALUES (#{obj.server}, #{obj.url}, #{obj.check_point}, #{obj.update_time}, #{obj.status})";

	@Insert(INSERT)
	@Options(useGeneratedKeys = true, keyProperty = "obj.server")
	void insert(@Param("obj") LogFileInfo loginfo);
	
	final String SELECT_BY_ID = "SELECT * FROM rs_log WHERE server = #{server_name}";
	@Select(SELECT_BY_ID)
	@Results(value = { @Result(property = "server", column = "server"),
			@Result(property = "url", column = "url"),
			@Result(property = "check_point", column = "check_point"),
			@Result(property = "update_time", column = "update_time"),
			@Result(property = "status", column = "status")})
	LogFileInfo selectById(@Param("server_name") String server_name);

}
