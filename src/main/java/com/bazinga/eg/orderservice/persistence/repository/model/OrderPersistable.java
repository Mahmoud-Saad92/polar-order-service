package com.bazinga.eg.orderservice.persistence.repository.model;

import com.bazinga.eg.orderservice.common.enums.OrderStatus;
import com.bazinga.eg.orderservice.common.util.AuditingBaseEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_order", schema = "ordersvc")
public class OrderPersistable extends AuditingBaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1248461927L;

    @Id
    private Long id;

    @NotBlank
    private String orderNumber;

    private String bookIsbn;

    private String bookName;

    private Double bookPrice;

    private Integer quantity;

    private OrderStatus status;

    @Version
    private Long version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderPersistable bookPersistable = (OrderPersistable) o;
        return orderNumber != null && Objects.equals(orderNumber, bookPersistable.orderNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber);
    }
}
