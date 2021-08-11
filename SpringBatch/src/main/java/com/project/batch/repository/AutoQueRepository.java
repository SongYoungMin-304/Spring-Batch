package com.project.batch.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.batch.domain.AutoQueue;

public interface AutoQueRepository extends JpaRepository<AutoQueue, Long>{
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value="update AutoQueue a set a.flag = 'I', a.pollKey = :pollKey " +
			" where 1 = 1 " +
			" and a.targetFlag = 'N' " +
			" and rownum <= :limitCount " +
			"")
	int updatePreAutoQueue(
        @Param("pollKey") String pollKey,
        @Param("limitCount") long limitCount
    );
}
