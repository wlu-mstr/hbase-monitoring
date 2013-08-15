package com.microstrategy.database.hbase.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.microstrategy.database.hbase.mapper.ContactMapper;
import com.microstrategy.database.hbase.mapper.LogFileInfoMapper;
import com.microstrategy.database.hbase.mapper.RegionOfTableOnEachRSMapper;
import com.microstrategy.database.hbase.mapper.RegionServerStatusMapper;

/**
 * MyBatis Connection Factory, which reads the configuration data from a XML
 * file.
 *
 * @author Loiane Groner http://loianegroner.com (English) http://loiane.com
 * (Portuguese)
 */
public class MyBatisConnectionFactory {

    private static SqlSessionFactory sqlSessionFactory;

    static {

        try {

            String resource = "SqlMapConfig.xml";
            Reader reader = Resources.getResourceAsReader(resource);

            if (sqlSessionFactory == null) {
                sqlSessionFactory = new SqlSessionFactoryBuilder()
                        .build(reader);

                sqlSessionFactory.getConfiguration().addMapper(
                        ContactMapper.class);
                sqlSessionFactory.getConfiguration().addMapper(
                        LogFileInfoMapper.class);
                sqlSessionFactory.getConfiguration().addMapper(
                        RegionOfTableOnEachRSMapper.class);
                sqlSessionFactory.getConfiguration().addMapper(
                        RegionServerStatusMapper.class);

            }
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public static SqlSessionFactory getSqlSessionFactory() {

        return sqlSessionFactory;
    }
}
