package com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo.service;

import com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo.document.SecuenciaDocument;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SecuenciaServiceImpl implements SecuenciaService {

    private final ReactiveMongoOperations mongoOperations;

    public SecuenciaServiceImpl(ReactiveMongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public Mono<String> siguienteValor(String secuenciaId) {
        Query query = new Query(Criteria.where("_id").is(secuenciaId));
        Update update = new Update().inc("valor", 1);
        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true).upsert(true);

        return mongoOperations.findAndModify(query, update, options, SecuenciaDocument.class)
                .map(secuencia -> String.valueOf(secuencia.getValor()));
    }
}
