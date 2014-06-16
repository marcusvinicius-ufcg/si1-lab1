package models.DAO;

import java.util.List;

import javax.persistence.Query;

public interface DAO {
	
	boolean persist(Object e);

	void flush();
	
	void merge(Object e);

	<T> T findByEntityId(Class<T> clazz, Long id);

	<T> List<T> findAllByClassName(String className);

	<T> void removeById(Class<T> classe, Long id);
	
	void remove(Object objeto);

	
	<T> List<T> findByAttributeName(String className, String attributeName,
			String attributeValue);

	Query createQuery(String query);
}
