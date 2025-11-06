package com.bazinga.eg.orderservice.persistence.mapper;

import com.bazinga.eg.orderservice.application.domain.model.Order;
import com.bazinga.eg.orderservice.persistence.repository.model.OrderPersistable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Collection;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderPersistableMapper {

    @Mapping(source = "orderId", target = "id")
    OrderPersistable toOrderPersistable(Order order);

    @Mapping(source = "id", target = "orderId")
    Order toOrder(OrderPersistable orderPersistable);

    Collection<OrderPersistable> toOrderPersistableList(Collection<Order> orders);
}
