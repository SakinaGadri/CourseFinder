# Course Finder Project

Intro...

# Course Finder REST API Documentation

## Students APIs:

### Create
* description: Creates and stores a Student in the database with the following properties:
    - `id`: student id
    - `name`: student name
    - `cGPA`: cGPA of student 
* request: POST `/addStudent`
    - content-type: `application/json`
    - body: object
        - id: (int) student id
        - name (string) student name
        - cGPA (int) cGPA of student 
* response: 200
    * content-type: `application/json`
    - body: TODO
    * TODO: what do I return???
* response: 404 TODO: elaborate
    - body: TODO
* response: 500 TODO: elaborate
    - body: TODO
* example:
``` 
$ curl -X POST 
       -H "Content-Type: `application/json`"  
       -d '{"id": 1004 ,"name":"Sakina", "cGPA":3.0 }'
       http://localhost:8080/addStudent'
```

### Read

- description: retrieves all the students in the database
- request: `GET /allStudents`   
- response: 200
    - content-type: `application/json`
    - body: object
        - id: (int) student id
        - name (string) student name
        - cGPA (int) cGPA of student 
- response: 404
    - body: TODO
- response: 400
    - body: TODO
- response: 500
    - body: server side error
``` 
$ curl -X GET http://localhost:8080/allStudents
``` 

- description: retrieves the students with `studentName` in the database
- request: `GET /findStudent/:studentName`
- response: 200
    - content-type: `application/json`
    - body: list of objects
        - id: (int) student id
        - name (string) student name
        - cGPA (int) cGPA of student 
- response: 404
    - body: student with `studentName` not found
- response: 400
    - body: TODO
- response: 500
    - body: server side error
``` 
$ curl -X GET http://localhost:8080/findStudent?studentName=Sakina
```

- description: retrieves the students with `studentId` in the database
- request: `GET /findStudent/:studentId`
- response: 200
    - content-type: `application/json`
    - body: list of object
        - id: (int) student id
        - name (string) student name
        - cGPA (int) cGPA of student 
- response: 404
    - body: student with `studentId` not found
- response: 400
    - body: TODO
- response: 500
    - body: server side error
``` 
$ curl -X GET http://localhost:8080/findStudent?studentId=1004
```

- description: retrieves the cGPA of a student with `studentId` in the database
- request: `GET /cGPA/:studentId`
- response: 200
    - content-type: `application/json`
    - body: object
        - cGPA (int) cGPA of student 
- response: 404
    - body: student with `studentId` not found
- response: 400
    - body: TODO
- response: 500
    - body: server side error
``` 
$ curl -X GET http://localhost:8080/cGPA?studentId=1004
```

- description: retrieves the cGPA of a student with `studentName` in the database
- request: `GET /cGPA/:studentName`
- response: 200
    - content-type: `application/json`
    - body: object
        - cGPA (int) cGPA of student 
- response: 404
    - body: student with `studentName` not found
- response: 400
    - body: TODO
- response: 500
    - body: server side error
``` 
$ curl -X GET http://localhost:8080/cGPA?studentName=Sakina
```

Course End Points
1. /addCourse
2. /findCourse/{course-code}
3. /findCourse/{course-name}
4. /allCourses
5. /courseInstructor/{course}
6. /courseAverage/{course}
7. /courseStatus/{course}
8. /eligibleCourses/{course} // given a course what other courses can I take?
9. /intersection/{courseA}/{courseB} // intersetion between two courses returns a list of students who've taken both the courses

Relationship between Course and Student
1. /similarCourses/{course}
2. /eligibleCourses/{student} // given a student, what courses can they take based on what they've taken so far


References:
// https://www.programcreek.com/java-api-examples/?code=promregator%2Fpromregator%2Fpromregator-master%2Fsrc%2Ftest%2Fjava%2Forg%2Fcloudfoundry%2Fpromregator%2FmockServer%2FDefaultMetricsEndpointHttpHandler.java

https://neo4j.com/docs/api/java-driver/current/


https://www.rgagnon.com/javadetails/java-get-url-parameters-using-jdk-http-server.html