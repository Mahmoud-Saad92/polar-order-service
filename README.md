# `Bazinga (BZA)` order-service
This application is part of the Polar Bookshop system and provides the functionality for ordering books.

## REST API

| Endpoint	      | Method   | Req. body    | Status | Resp. body     | Description    		   	         |
|:---------------:|:--------:|:------------:|:------:|:--------------:|:---------------------------------|
| `/orders`       | `GET`    |              | 200    | Orders         | Get all the orders.              |
| `/orders`       | `POST`   | OrderRequest | 200    | Order          | Submit a new order.              |

After building the application, you can also run it from the Java CLI:

```bash
$ java -jar target/order-service-0.0.1.jar
```

Run to build docker container locally

```bash
$ DOCKER_BUILDKIT=1 docker compose up -d
```

