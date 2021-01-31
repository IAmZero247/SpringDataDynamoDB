Step 1 : 
docker run -d -p 8000:8000 amazon/dynamodb-local

Step 2 : Create Table 

aws dynamodb create-table --table-name Tenant1Customer --attribute-definitions AttributeName=Id,AttributeType=S --key-schema AttributeName=Id,KeyType=HASH --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 --endpoint-url http://localhost:8000

aws dynamodb create-table --table-name Tenant2Customer --attribute-definitions AttributeName=Id,AttributeType=S --key-schema AttributeName=Id,KeyType=HASH --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 --endpoint-url http://localhost:8000

aws dynamodb create-table --table-name Tenant3Customer --attribute-definitions AttributeName=Id,AttributeType=S --key-schema AttributeName=Id,KeyType=HASH --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 --endpoint-url http://localhost:8000

PS - config aws profile before this stpe in aws cli 

Step 3 : Verify tables 

aws dynamodb list-tables --endpoint-url http://localhost:8000

aws dynamodb scan --table-name Tenant1Customer --endpoint-url http://localhost:8000

Step 4 : Run spring boot in local 


Steps to update - Pushing to ECS 
