# A list of cURL's to check functionality of Meals API

## get meals
curl --location 'http://localhost:8080/topjava/rest/meals'

## get meals filtered
curl --location 'http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-31&startTime=13%3A00&endDate=2020-01-31&endTime=21%3A00'

## get meals filtered no parameters
curl --location 'http://localhost:8080/topjava/rest/meals/filter'

## get meals filtered only dates
curl --location 'http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&endDate=2020-01-30'

## get meals filtered only time
curl --location 'http://localhost:8080/topjava/rest/meals/filter?startTime=09%3A00&endTime=15%3A00'

## get meals filtered only start parameters
curl --location 'http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-31&startTime=00%3A00'

## get meals filtered only end parameters
curl --location 'http://localhost:8080/topjava/rest/meals/filter?endDate=2020-01-30&endTime=14%3A00'

## create meal
curl --location 'http://localhost:8080/topjava/rest/meals' \
--header 'Content-Type: application/json' \
--data '{
"dateTime":"2020-02-01T18:00:00",
"description":"Созданный ужин",
"calories":300
}'

## update meal
curl --location --request PUT 'http://localhost:8080/topjava/rest/meals/100003' \
--header 'Content-Type: application/json' \
--data '{
"id":100003,
"dateTime":"2020-01-30T10:02:00",
"description":"Обновленный завтрак",
"calories":200
}'

## delete meal
curl --location --request DELETE 'http://localhost:8080/topjava/rest/meals/100003'