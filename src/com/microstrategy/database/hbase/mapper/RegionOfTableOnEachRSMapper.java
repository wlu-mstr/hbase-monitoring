package com.microstrategy.database.hbase.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.microstrategy.database.hbase.model.RegionOfTableOnEachRSServer;

/**
 * private String memstoresize; // memstore size private String storesize; //
 * total size of store file size private String storenumber;
 * 
 * @author wlu
 * 
 */
public interface RegionOfTableOnEachRSMapper {
	final String INSERT = "REPLACE INTO RTRS (ts, metrix) "
			+ "VALUES ( #{obj.timestamp}, #{obj.metrics})";

	@Insert(INSERT)
	@Options(useGeneratedKeys = true, keyProperty = "obj.timestamp")
	void insert(@Param("obj") RegionOfTableOnEachRSServer rtrs);

	final String SELECT_BY_ID = "SELECT * FROM RTRS WHERE ts = #{sel_ts}";

	@Select(SELECT_BY_ID)
	@Results(value = { @Result(property = "timestamp", column = "ts"),
			@Result(property = "metrics", column = "metrix") })
	RegionOfTableOnEachRSServer selectByTS(@Param("sel_ts") String ts);

	final String SELECT_RECENT_N = "SELECT * FROM RTRS ORDER BY ts DESC LIMIT #{limit}";

	@Select(SELECT_RECENT_N)
	@Results(value = { @Result(property = "timestamp", column = "ts"),
			@Result(property = "metrics", column = "metrix") })
	List<RegionOfTableOnEachRSServer> selectRecent(@Param("limit") Integer limit);
}
