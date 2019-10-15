#Netflix Conductor Client

1) Start the conductor server as per https://netflix.github.io/conductor/server/#installing

2) Run the springboot application

curl -X POST http://localhost:8080/api/workflow/lang-decision-workflow -H 'Content-Type: application/json' -d '{"lang": "DOTNET","name": "Test"}'

 


