package com.fmss.edd.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.fmss.edd.domain.Location;
import com.fmss.edd.repository.LocationRepository;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link Location} entity.
 */
public interface LocationSearchRepository extends ReactiveElasticsearchRepository<Location, Long>, LocationSearchRepositoryInternal {}

interface LocationSearchRepositoryInternal {
    Flux<Location> search(String query);

    Flux<Location> search(Query query);
}

class LocationSearchRepositoryInternalImpl implements LocationSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    LocationSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<Location> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery);
    }

    @Override
    public Flux<Location> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, Location.class).map(SearchHit::getContent);
    }
}
