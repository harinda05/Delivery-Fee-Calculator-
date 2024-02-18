### Run with Docker
Run
```sh
  ./start-service.bat
```
or
```sh
  ./start-service.sh
```

### API Specification

Request Type
```
  POST
```

Local Endpoint Url
```
  http://localhost:8080/api/fees/delivery-fee
```

Sample Request PayLoad
```
  {"cart_value": 1000, "delivery_distance": 1000, "number_of_iems": 4, "time": "2024-01-27T15:00:00Z"}
```

Sample Response PayLoad
```
{ "delivery_fee": 200 }
```
Using curl:
```sh
 curl -X POST "http://localhost:8080/api/fees/delivery-fee" -H "Content-Type: application/json" -d '{"cart_value": 1000, "delivery_distance": 1000, "number_of_iems": 4, "time": "2024-01-27T15:00:00Z"}'
```