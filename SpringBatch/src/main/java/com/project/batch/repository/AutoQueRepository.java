package com.project.batch.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.batch.domain.AutoQueue;
import com.project.batch.model.AutoQueSchdDto;

public interface AutoQueRepository extends JpaRepository<AutoQueue, Long>{
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value="update AutoQueue a set a.flag = 'I', a.pollKey = :pollKey " +
			" where 1 = 1 " +
			" and a.flag = 'N' " +
			" and a.queueId <= :limitCount " +
			"")
	int updatePreAutoQueue(
        @Param("pollKey") String pollKey,
        @Param("limitCount") long limitCount
    );

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value="update AutoQueue a set a.flag = 'Y' "+
			" where 1 = 1 " +
			" and a.flag = 'I' " +
			" and a.pollKey = :pollKey " +
			"")
	int updateAutoQueue(
			@Param("pollKey") String pollKey
	);

	@Query(value="select " +
			" new com.project.batch.model.AutoQueSchdDto(coalesce(MIN(a.id),0), coalesce(MAX(a.id),0), " +
			" a.pollKey )  " +
			" from AutoQueue a" +
			" where a.flag = 'I' and a.pollKey = :pollKey" +
			" group by a.pollKey" +
			"")
	List<AutoQueSchdDto> findBySchdByPollKey(
        @Param("pollKey") String pollKey);
}
