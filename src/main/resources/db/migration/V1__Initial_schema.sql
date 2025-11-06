CREATE SCHEMA IF NOT EXISTS ordersvc;

DROP TABLE IF EXISTS ordersvc.tbl_order;

CREATE TABLE IF NOT EXISTS ordersvc.tbl_order (
    id                 BIGSERIAL PRIMARY KEY NOT NULL,
    order_number       VARCHAR(50) NOT NULL UNIQUE,
    book_isbn          VARCHAR(255) NOT NULL,
    book_name          VARCHAR(255),
    book_price         float8,
    quantity           int NOT NULL,
    status             VARCHAR(255) NOT NULL,
    version            BIGINT NOT NULL DEFAULT 0,
    created_by         VARCHAR(225),
    last_modified_by   VARCHAR(225),
    created_date       TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_tbl_order_isbn ON ordersvc.tbl_order (order_number);