package com.elitecore.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.elitecore.model.DBMaster;

@Repository("DBMasterdao")
public class DBMasterDao {
		private EntityManager em1;
			
		@PersistenceContext
		public void setEntityManager(EntityManager em1)
		{
		    this.em1 = em1;
		}
		@Autowired
		JdbcTemplate template;
		
		public void saveDB(DBMaster dbm) {
			em1.persist(dbm);
		}
		
		public int editDB(DBMaster dbm) {			
			String sql="UPDATE `db_master` SET `description`='"+dbm.getDescription()+"',`url`='"+dbm.getUrl()+"',`username`='"+dbm.getUsername()+"',`password`='"+dbm.getPassword()+"',`min_pool_size`='"+dbm.getMin_pool_size()+"'`max_pool_size`='"+dbm.getMax_pool_size()+"' where `id`= '"+dbm.getId()+"'";	
			return template.update(sql);
		
		}

		public int dltDB(int id) {
			
			String sql="delete from   db_master where `id`='"+id+"'";
			template.update(sql);
			return template.update(sql);
		}
		
		public int getcount() throws DataAccessException{
			
			String sql="select count(`id`) from db_master";
			int total= template.queryForInt(sql);
			return total;
		}
		
		public List<DBMaster> getDBByPage(int pageid,int total){
			String sql="select * from db_master";
			System.out.println(sql);
			return template.query(sql,new RowMapper<DBMaster>(){
					public DBMaster mapRow(ResultSet rs, int row) throws SQLException {
					DBMaster e=new DBMaster();
					e.setId(rs.getInt("id"));
					e.setDescription(rs.getString("description"));
					e.setUrl(rs.getString("url"));
					e.setUsername(rs.getString("username"));
					e.setPassword(rs.getString("password"));
					e.setMax_pool_size(rs.getString("max_pool_size"));	
					e.setMin_pool_size(rs.getString("min_pool_size"));
					return e;
				}
			});
		}

}
