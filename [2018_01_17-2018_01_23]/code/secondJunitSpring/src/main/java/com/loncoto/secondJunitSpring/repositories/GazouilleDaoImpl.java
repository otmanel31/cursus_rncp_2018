package com.loncoto.secondJunitSpring.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.loncoto.secondJunitSpring.metier.Gazouille;

public class GazouilleDaoImpl implements RowMapper<Gazouille>, GazouilleDao
{
	public static final String FIND_ALL_SQL
			= "select id,titre,corps from gazouille";
	public static final String FIND_BY_ID_SQL
			= "select id,titre,corps from gazouille where id=?";
	public static final String INSERT_ONE_SQL 
			= "insert into gazouille (titre,corps) VALUES(?,?)";
	public static final String UPDATE_ONE_SQL 
			= "update gazouille set titre=? ,corps=?  where id=?";
	
	
	private JdbcTemplate jdbcTemplate;
	public JdbcTemplate getJdbcTemplate() {return jdbcTemplate;}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}
	
	@Override
	public List<Gazouille> findAll() {
		return jdbcTemplate.query(FIND_ALL_SQL, this);
	}
	
	@Override
	public Gazouille findById(int id) {
		return jdbcTemplate.queryForObject(FIND_BY_ID_SQL,
										   new Object[] {id},
										   this);
	}
	
	@Override
	public int save(Gazouille gazouille) {
		if (gazouille.getId() == 0)
			return jdbcTemplate.update(INSERT_ONE_SQL,
										gazouille.getTitre(),
										gazouille.getCorps());
		else
			return jdbcTemplate.update(UPDATE_ONE_SQL,
										gazouille.getTitre(),
										gazouille.getCorps(),
										gazouille.getId());
	}
	
	@Override
	public Gazouille mapRow(ResultSet rs, int pos) throws SQLException {
		return new Gazouille(rs.getInt("id"), rs.getString("titre"), rs.getString("corps"));
	}
}
