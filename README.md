## Best Commerce
Microservices for Best Commerce

Implemented with Spring Boot + Java 8 + Postgresql

### Run tests
```
mvn -f bc-authentication/pom.xml clean test
mvn -f bc-product-listing/pom.xml clean test
```

### Run project
```
mvn -f bc-authentication/pom.xml clean install
mvn -f bc-product-listing/pom.xml clean install

docker-compose up -d --build
```

### Service Endpoints
Login endpoint: [localhost:8080/auth/login](http://localhost:8080/auth/login)

Product List endpoint:  [http://localhost:8081/merchant/merchantid/products/?page=0&size=2&sort=unitPrice,asc](http://localhost:8081/merchant/merchantid/products/?page=0&size=2&sort=unitPrice,asc)


#### Sign up (dummy)
POST localhost:8080/auth/sign-up
```json
{
  "username": "testuser",
  "password": "123456"
}
```
returns
```json
{
  "status": "OK",
  "message": "User created"
}
```

#### Login
POST localhost:8080/auth/login
```json
{
  "username": "testuser",
  "password": "123456",
  "rememberMe": false
}
```
returns
```json
{
  "token": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWNlcCIsImV4cCI6MTYxNjEwNzYyMH0.I2IEBq3v4xYtnN42iKV9410zEzfDt-qlZAAQxkv9GI8"
}
```

#### Create Product (dummy)
POST localhost:8081/merchant/testuser/products
```json
{
  "merchantId": "testuser",
  "name": "product-1",
  "inventory": 1000,
  "unitPrice": 100.00,
  "category": "ELECTRONICS",
  "description": "desc",
  "paymentOption": "DIRECT",
  "deliveryOption": "delvry"
}
```
returns
```json
{
  "status": "OK",
  "message": "Product is saved"
}
```

#### Product Listing
GET localhost:8081/merchant/testuser/products?page=0&size=2&sort=unitPrice,asc
returns
```json
{
  "products": [
    {
      "name": "product-3",
      "inventory": 300,
      "merchantId": "testuser",
      "category": "ELECTRONICS",
      "description": "desc",
      "unitPrice": 50.00,
      "paymentOption": "DIRECT",
      "deliveryOption": "delvry"
    },
    {
      "name": "product-1",
      "inventory": 1000,
      "merchantId": "testuser",
      "category": "ELECTRONICS",
      "description": "desc",
      "unitPrice": 100.00,
      "paymentOption": "DIRECT",
      "deliveryOption": "delvry"
    }
  ],
  "totalPages": 2,
  "totalProducts": 3
}
```

###### Other samples for sorting
GET localhost:8081/merchant/testuser/products?page=0&size=10&sort=inventory,desc
