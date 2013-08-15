/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microstrategy.database.hbase.dao;

import com.microstrategy.database.hbase.mapper.RegionServerStatusMapper;
import com.microstrategy.database.hbase.model.RegionServerStatus;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 *
 * @author wlu
 */
public class RegionServerStatusDAO {

    private SqlSessionFactory sqlSessionFactory;
    private SqlSession session;

    public RegionServerStatusDAO() {
        sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();
        session = sqlSessionFactory.openSession();
    }

    public void insert(RegionServerStatus contact) {

        try {

            RegionServerStatusMapper mapper = session
                    .getMapper(RegionServerStatusMapper.class);
            mapper.insert(contact);

            session.commit();
        } finally {
            session.close();
        }
    }

    public void insertSimple(RegionServerStatus s) {
        try {
            RegionServerStatusMapper mapper = session.getMapper(RegionServerStatusMapper.class);
            mapper.insertSimple(s);
            session.commit();
        } finally {
            session.close();
        }
    }

    public RegionServerStatus selectById(String sn) {

        try {

            RegionServerStatusMapper mapper = session
                    .getMapper(RegionServerStatusMapper.class);
            RegionServerStatus list = mapper.selectByTS(sn);

            return list;
        } finally {
            session.close();
        }
    }

    public List<RegionServerStatus> selectRecentN(int N) {
        try {

            RegionServerStatusMapper mapper = session
                    .getMapper(RegionServerStatusMapper.class);
            return mapper.selectRecent(N);

        } finally {
            session.close();
        }
    }

    private List<RegionServerStatus> mCachedAll;
    // always re get data from database and return ...................
    public List<RegionServerStatus> selectAll() {
        try {
            RegionServerStatusMapper mapper = session.getMapper(RegionServerStatusMapper.class);
            mCachedAll = mapper.selectAll();
            return mCachedAll;
        } finally {
            session.close();
        }
    }
    
    public List<RegionServerStatus> selectAll(boolean refresh){
        if(!refresh && mCachedAll != null){
            return mCachedAll;
        }
        return selectAll();
    }

    public List<RegionServerStatus> selectAfter(String ts){
         try {
            RegionServerStatusMapper mapper = session.getMapper(RegionServerStatusMapper.class);
            return mapper.selectAfter(ts);
        } finally {
            session.close();
        }
    }
    public String selectRecentSimple() {
        try {

            RegionServerStatusMapper mapper = session
                    .getMapper(RegionServerStatusMapper.class);
            return mapper.selectRecentSimple();

        } finally {
            session.close();
        }
    }

    public int getCount() {
        try {

            RegionServerStatusMapper mapper = session
                    .getMapper(RegionServerStatusMapper.class);
            return mapper.getCount();

        } finally {
            session.close();
        }
    }
}
