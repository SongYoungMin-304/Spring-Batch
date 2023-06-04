package com.project.batch.repository;

import com.project.batch.domain.AutoQueue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutoQueueRepository extends JpaRepository<AutoQueue, Long>, AutoQueueRepositoryCustom{
	
/*	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value="update AutoQueue a set a.flag = 'I', a.pollKey = :pollKey " +
			" where 1 = 1 " +
			" and a.flag = 'N' " +
			" and a.pollKey IS NULL" +
			" and a.templateMsgId = :templateMsgId" +
			"")
	int updatePreAutoQueue(
        @Param("pollKey") String pollKey,
		@Param("templateMsgId") String templateMsgId
    );

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value="update AutoQueue a set a.flag = 'Y' "+
			" where 1 = 1 " +
			" and a.flag = 'I' " +
			" and a.pollKey = :pollKey " +
			" and a.queueId = :queueId " +
			"")
	int updateAutoQueue(
			@Param("pollKey") String pollKey,
			@Param("queueId") long queueId
	);

	@Query(value="select " +
			" new com.project.batch.model.AutoQueSchdDto(coalesce(MIN(a.id),0), coalesce(MAX(a.id),0), " +
			" a.pollKey, " +
			" a.templateMsgId " +
			")  " +
			" from AutoQueue a" +
			" where a.flag = 'I' and a.pollKey = :pollKey" +
			" group by a.pollKey, a.templateMsgId" +
			"")
	List<AutoQueSchdDto> findBySchdByPollKey(
        @Param("pollKey") String pollKey);

	@Query(value="select distinct a.templateMsgId" +
			" from AutoQueue a" +
			" where a.flag = 'N' and a.pollKey IS NULL")
	List<String> findlist();*/
}
