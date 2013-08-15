package com.microstrategy.database.hbase.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.microstrategy.database.hbase.mapper.LogFileInfoMapper;
import com.microstrategy.database.hbase.model.LogFileInfo;

public class LogFileInfoDAO {
	private SqlSessionFactory sqlSessionFactory;

	public LogFileInfoDAO() {
		sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();
	}

	public void insert(LogFileInfo contact) {

		SqlSession session = sqlSessionFactory.openSession();

		try {

			LogFileInfoMapper mapper = session
					.getMapper(LogFileInfoMapper.class);
			mapper.insert(contact);

			session.commit();
		} finally {
			session.close();
		}
	}

	public LogFileInfo selectById(String sn) {

		SqlSession session = sqlSessionFactory.openSession();

		try {

			LogFileInfoMapper mapper = session
					.getMapper(LogFileInfoMapper.class);
			LogFileInfo list = mapper.selectById(sn);

			return list;
		} finally {
			session.close();
		}
	}

	public static void main(String args[]) {
		LogFileInfo lfi = new LogFileInfo();
		String servers[] = { "rs.adcaj03.machine.wisdom.com",
				"rs.adcau02.machine.wisdom.com",
				"rs.adcac15.machine.wisdom.com",
				"rs.adcac13.machine.wisdom.com",
				"rs.adcaj05.machine.wisdom.com",
				"rs.adcac14.machine.wisdom.com",
				"rs.adcau03.machine.wisdom.com",
				"rs.adcaj06.machine.wisdom.com",
				"rs.adcaj04.machine.wisdom.com",
				"master.adcac13.machine.wisdom.com" };
		String urls[] = {
				"http://adcaj03.machine.wisdom.com:60030/logs/hbase-cmf-hbase1-REGIONSERVER-adcaj03.machine.wisdom.com.log.out",
				"http://adcau02.machine.wisdom.com:60030/logs/hbase-cmf-hbase1-REGIONSERVER-adcau02.machine.wisdom.com.log.out",
				"http://adcac15.machine.wisdom.com:60030/logs/hbase-cmf-hbase1-REGIONSERVER-adcac15.machine.wisdom.com.log.out",
				"http://adcac13.machine.wisdom.com:60030/logs/hbase-cmf-hbase1-REGIONSERVER-adcac13.machine.wisdom.com.log.out",
				"http://adcaj05.machine.wisdom.com:60030/logs/hbase-cmf-hbase1-REGIONSERVER-adcaj05.machine.wisdom.com.log.out",
				"http://adcac14.machine.wisdom.com:60030/logs/hbase-cmf-hbase1-REGIONSERVER-adcac14.machine.wisdom.com.log.out",
				"http://adcau03.machine.wisdom.com:60030/logs/hbase-cmf-hbase1-REGIONSERVER-adcau03.machine.wisdom.com.log.out",
				"http://adcaj06.machine.wisdom.com:60030/logs/hbase-cmf-hbase1-REGIONSERVER-adcaj06.machine.wisdom.com.log.out",
				"http://adcaj04.machine.wisdom.com:60030/logs/hbase-cmf-hbase1-REGIONSERVER-adcaj06.machine.wisdom.com.log.out",
				"http://adcac13.machine.wisdom.com:60030/logs/hbase-cmf-hbase1-MASTER-adcac13.machine.wisdom.com.log.out" };

		LogFileInfoDAO dao = new LogFileInfoDAO();
		for (int i = 0; i < urls.length; i++) {
			lfi.setServer(servers[i]);
			lfi.setUrl(urls[i]);
			lfi.setCheck_point("0");
			lfi.setUpdate_time("Jan 27, 2013 6:31:46");
			lfi.setStatus("OK");
			dao.insert(lfi);

			lfi = dao.selectById(servers[i]);
			System.out.println(lfi);
		}

	}

}
