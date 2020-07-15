package edu.fjnu.online.dao;

import java.util.List;

import edu.fjnu.online.domain.Course;

public interface CourseDao extends BaseDao<Course>{

	public List<Course> findActive(Course course);
}
