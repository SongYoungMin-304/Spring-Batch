package com.project.batch.repository.auto;

import com.project.batch.domain.AutoQueue;
import com.project.batch.model.AutoQueSchdDto;
import com.project.batch.model.QAutoQueSchdDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.project.batch.domain.QAutoQueue.autoQueue;
import static io.micrometer.core.instrument.util.StringUtils.isEmpty;

@Transactional
public class AutoQueueRepositoryImpl implements AutoQueueRepositoryCustom{

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public AutoQueueRepositoryImpl(EntityManager em){
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public JPAQuery<AutoQueue> getAutoQueue(Long minSeq, Long maxSeq, String pollKey, String serverId) {
        return queryFactory.select(autoQueue)
                .from(autoQueue)
                .where(
                        pollKeyEq(pollKey),
                        betweenMinSeqAndMaxSeq(minSeq, maxSeq),
                        flagEq("I")
                );
    }

    @Override
    public List<String> findList() {
        return queryFactory.select(autoQueue.templateMsgId).distinct()
                .from(autoQueue)
                .fetch();
    }

    @Override
    public long updatePreAutoQueue(String pollKey, String templateMsgId) {
        return queryFactory.update(autoQueue)
                .set(autoQueue.pollKey, pollKey)
                .set(autoQueue.templateMsgId, templateMsgId)
                .set(autoQueue.flag,"I")
                .where(
                        flagEq("N"),
                        pollKeyEq(null),
                        templateMsgIdEq(templateMsgId)
                ).execute();
    }

    @Override
    public List<AutoQueSchdDto> findBySchdByPollKey(String pollKey) {

        return queryFactory
                .select(new QAutoQueSchdDto(autoQueue.queueId.min(), autoQueue.queueId.max(), autoQueue.pollKey, autoQueue.templateMsgId))
                .from(autoQueue)
                .where(
                        flagEq("I"),
                        pollKeyEq(pollKey)
                ).groupBy(autoQueue.pollKey, autoQueue.templateMsgId).fetch();
    }

    private BooleanExpression templateMsgIdEq(String templateMsgId){
        return autoQueue.templateMsgId.eq(templateMsgId);
    }

    private BooleanExpression flagEq(String flag){
        return isEmpty(flag) ? null : autoQueue.flag.eq(flag);
    }

    private BooleanExpression pollKeyEq(String pollKey){
        return isEmpty(pollKey) ? autoQueue.pollKey.isNull() :autoQueue.pollKey.eq(pollKey);
    }

    private BooleanExpression betweenMinSeqAndMaxSeq(Long minSeq, Long maxSeq){
        if(minSeq!=null && maxSeq!=null){
            return autoQueue.queueId.between(minSeq, maxSeq);
        }else{
            return null;
        }
    }


}
