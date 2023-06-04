package com.project.batch.sender.reader;

import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class QuerydslPagingItemReader<T> extends AbstractPagingItemReader<T> {

    protected Map<String, Object> jpaPropertyMap = new HashMap<>();
    protected EntityManagerFactory emf;
    protected EntityManager em;
    protected JPAQuery<T> queryFunction;
    protected boolean transacted = true;

    public QuerydslPagingItemReader(
            EntityManagerFactory emf,
            int pageSize,
            JPAQuery<T> queryFunction
    ){
        this();
        this.emf = emf;
        this.queryFunction = queryFunction;
        setPageSize(pageSize);
    }

    public QuerydslPagingItemReader() {

    }

    @Override
    protected void doOpen() throws Exception{
        super.doOpen();

        em = emf.createEntityManager(jpaPropertyMap);
        if(em == null){
            throw new DataAccessResourceFailureException("Unable to obtain an EntityManager");
        }
    }

    @Override
    protected void doReadPage() {
        JPAQuery<T> query = createQuery()
                .offset(0)
                .limit(getPageSize());

        initResults();

        fetchQuery(query);
    }

    @Override
    protected void doJumpToPage(int i) {

    }

    protected JPAQuery<T> createQuery() {
        //JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFunction;
    }

    protected void initResults() {
        if (CollectionUtils.isEmpty(results)) {
            results = new CopyOnWriteArrayList<>();
        } else {
            results.clear();
        }
    }

    protected void fetchQuery(JPAQuery<T> query) {
        if (!transacted) {
            List<T> queryResult = query.fetch();
            for (T entity : queryResult) {
                em.detach(entity);
                results.add(entity);
            }
        } else {
            results.addAll(query.fetch());
        }
    }

}
