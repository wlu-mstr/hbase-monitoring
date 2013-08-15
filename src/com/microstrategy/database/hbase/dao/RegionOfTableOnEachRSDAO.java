package com.microstrategy.database.hbase.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.microstrategy.database.hbase.mapper.RegionOfTableOnEachRSMapper;
import com.microstrategy.database.hbase.model.RegionOfTableOnEachRSServer;

public class RegionOfTableOnEachRSDAO {

	private SqlSessionFactory sqlSessionFactory;
	private SqlSession session;

	public RegionOfTableOnEachRSDAO() {
		sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();
		session = sqlSessionFactory.openSession();
	}

	public void insert(RegionOfTableOnEachRSServer contact) {

		try {

			RegionOfTableOnEachRSMapper mapper = session
					.getMapper(RegionOfTableOnEachRSMapper.class);
			mapper.insert(contact);

			session.commit();
		} finally {
			session.close();
		}
	}

	public RegionOfTableOnEachRSServer selectById(String sn) {

		try {

			RegionOfTableOnEachRSMapper mapper = session
					.getMapper(RegionOfTableOnEachRSMapper.class);
			RegionOfTableOnEachRSServer list = mapper.selectByTS(sn);

			return list;
		} finally {
			session.close();
		}
	}

	public List<RegionOfTableOnEachRSServer> selectRecentN(int N) {
		try {

			RegionOfTableOnEachRSMapper mapper = session
					.getMapper(RegionOfTableOnEachRSMapper.class);
			return mapper.selectRecent(N);

		} finally {
			session.close();
		}
	}

}
