package edu.fjnu.online.dao;

import java.util.List;

import edu.fjnu.online.domain.Grade;


public interface GradeDao extends BaseDao<Grade>{
	
	public List<Grade> findActive(Grade grade);

}
