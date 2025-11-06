package com.bazinga.eg.orderservice.persistence.repository.r2dbc;

import com.bazinga.eg.orderservice.persistence.repository.model.OrderPersistable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface OrderR2dbcRepository extends ReactiveCrudRepository<OrderPersistable, Long> {
}
