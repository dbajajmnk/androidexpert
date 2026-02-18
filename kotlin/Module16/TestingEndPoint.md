# health
curl http://localhost:8080/health

# create
curl -X POST http://localhost:8080/students \
  -H "Content-Type: application/json" \
  -d '{"name":"Deepak","marks":78}'

# list
curl http://localhost:8080/students
curl "http://localhost:8080/students?minMarks=80"

# get by id (replace 1001 with actual id)
curl http://localhost:8080/students/1001

# update
curl -X PUT http://localhost:8080/students/1001 \
  -H "Content-Type: application/json" \
  -d '{"marks":95}'

# delete
curl -X DELETE http://localhost:8080/students/1001 -i
