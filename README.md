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
* response: 400
    - body: Bad Request
* response: 500 
    - body: Server Side Error
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
    - body: Not Found
- response: 400
    - body: Bad Request
- response: 500
    - body: Server Side Error
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
    - body: Not Found
- response: 400
    - body: Bad Request
- response: 500
    - body: Server Side Error
``` 
$ curl -X GET http://localhost:8080/findStudent?studentName=Sakina
```

- description: retrieves the students with `studentId` in the database
- request: `GET /findStudent?studentId={studentId}`
- response: 200
    - content-type: `application/json`
    - body: list of object
        - id: (int) student id
        - name (string) student name
        - cGPA (int) cGPA of student 
- response: 404
    - body: Not Found
- response: 400
    - body: Bad Request
- response: 500
    - body: Server Side Error
``` 
$ curl -X GET http://localhost:8080/findStudent?studentId=1004
```

- description: retrieves the cGPA of a student with `studentId` in the database
- request: `GET /cGPA?studentId={studentId}`
- response: 200
    - content-type: `application/json`
    - body: object
        - cGPA (int) cGPA of student 
- response: 404
    - body: Not Found
- response: 400
    - body: Bad Request
- response: 500
    - body: Server Side Error
``` 
$ curl -X GET http://localhost:8080/cGPA?studentId=1004
```

- description: retrieves the cGPA of a student with `studentName` in the database
- request: `GET /cGPA?studenName={studentName}`
- response: 200
    - content-type: `application/json`
    - body: object
        - cGPA (int) cGPA of student 
- response: 404
    - body: Not Found
- response: 400
    - body: Bad Request
- response: 500
    - body: Server Side Error
``` 
$ curl -X GET http://localhost:8080/cGPA?studentName=Sakina
```


## Course End Points

### Create
* description: Creates and stores a Course in the database with the following properties:
    - `code`: course code
    - `name`: course name
    - `average`: average of the course
    - `professor`: professor who teaches the course
    - `status`: status of the course. Can be one of "Running", "Done", "Scheduled" 
* request: POST `/addCourse`
    - content-type: `application/json`
    - body: object
        - code (string): course code
        - name (string): course name
        - average (double): average of the course
        - professor (string): professor who teaches the course
        - status (string): status of the course. Can be one of "Running", "Done", "Scheduled" 
* response: 200
    * content-type: `application/json`
* response: 400
    - body: Bad Request
* response: 500 
    - body: Server Side Error
* example:
``` 
$ curl -X POST 
       -H "Content-Type: `application/json`"  
       -d '{"code": "A48" ,"name":"CS", "average": 2.7, "professor" : "Paco", "status" : "Done" }'
       http://localhost:8080/addStudent'
```

### Read

- description: retrieves all the courses in the database
- request: `GET /allCourses`
- response: 200
    - content-type: `application/json`
    - body: object
        - code (string): course code
        - name (string): course name
        - average (double): average of the course
        - professor (string): professor who teaches the course
        - status (string): status of the course. Can be one of "Running", "Done", "Scheduled"  
- response: 404
    - body: Not Found
- response: 400
    - body: Bad Request
- response: 500
    - body: Server Side Error
``` 
$ curl -X GET http://localhost:8080/allCourses
``` 

- description: retrieves the course with `courseCode`
- request: `GET /findCourse?courseCode={courseCode}`
- response: 200
    - content-type: `application/json`
    - body: object
        - code (string): course code
        - name (string): course name
        - average (double): average of the course
        - professor (string): professor who teaches the course
        - status (string): status of the course. Can be one of "Running", "Done", "Scheduled"  
- response: 404
    - body: Not Found
- response: 400
    - body: Bad Request
- response: 500
    - body: Server Side Error
``` 
$ curl -X GET http://localhost:8080/findCourse?courseCode="A48"
``` 

- description: retrieves the course with `courseName`
- request: `GET /findCourse?courseName={courseName}`
- response: 200
    - content-type: `application/json`
    - body: object
        - code (string): course code
        - name (string): course name
        - average (double): average of the course
        - professor (string): professor who teaches the course
        - status (string): status of the course. Can be one of "Running", "Done", "Scheduled"
- response: 404
    - body: Not Found
- response: 400
    - body: Bad Request
- response: 500
    - body: Server Side Error
``` 
$ curl -X GET http://localhost:8080/findCourse?courseName="CS"
``` 

- description: retrieves the professor of the course with `courseCode`
- request: `GET /courseProfessor?courseCode={courseCode}`
- response: 200
    - content-type: `application/json`
    - body: object
        - professor (string): professor who teaches the course
- response: 404
    - body: Not Found
- response: 400
    - body: Bad Request
- response: 500
    - body: Server Side Error
``` 
$ curl -X GET http://localhost:8080/courseProfessor?courseCode="A48"
``` 

- description: retrieves the professor of the course with `courseName`
- request: `GET /courseProfessor?courseName={courseName}`
- response: 200
    - content-type: `application/json`
    - body: object
        - professor (string): professor who teaches the course
- response: 404
    - body: Not Found
- response: 400
    - body: Bad Request
- response: 500
    - body: Server Side Error
``` 
$ curl -X GET http://localhost:8080/courseProfessor?courseName="CS"
``` 

- description: retrieves the average of the course with `courseCode`
- request: `GET /courseAverage?courseCode={courseCode}`
- response: 200
    - content-type: `application/json`
    - body: object
        - average (double): average of the course
- response: 404
    - body: Not Found
- response: 400
    - body: Bad Request
- response: 500
    - body: Server Side Error
``` 
$ curl -X GET http://localhost:8080/courseAverage?courseCode="A48"
``` 

- description: retrieves the average of the course with `courseName`
- request: `GET /courseAverage?courseName={courseName}`
- response: 200
    - content-type: `application/json`
    - body: object
        - average (double): average of the course
- response: 404
    - body: Not Found
- response: 400
    - body: Bad Request
- response: 500
    - body: Server Side Error
``` 
$ curl -X GET http://localhost:8080/courseAverage?courseName="CS"
``` 

- description: retrieves the status of the course with `courseCode`
- request: `GET /courseStatus?courseCode={courseCode}`
- response: 200
    - content-type: `application/json`
    - body: object
        - status (string): status of the course. Can be one of "Running", "Done", "Scheduled"
- response: 404
    - body: Not Found
- response: 400
    - body: Bad Request
- response: 500
    - body: Server Side Error
``` 
$ curl -X GET http://localhost:8080/courseStatus?courseCode="A48"
``` 

- description: retrieves the status of the course with `courseName`
- request: `GET /courseStatus?courseName={courseName}`
- response: 200
    - content-type: `application/json`
    - body: object
        - status (string): status of the course. Can be one of "Running", "Done", "Scheduled"
- response: 404
    - body: Not Found
- response: 400
    - body: Bad Request
- response: 500
    - body: Server Side Error
``` 
$ curl -X GET http://localhost:8080/courseStatus?courseName="CS"
``` 

8. /requisiteCourses/{course} // given a course what other courses can I take?
9. /commonStudents/{courseA}/{courseB} // intersetion between two courses returns a list of students who've taken both the courses
10. /commonCourses/{studentA}/{studentB} // returns courses that both studentA and studentB has taken

## Relationship between Course and Student
1. /similarCourses/{course}
2. /eligibleCourses/{student} // given a student, what courses can they take based on what they've taken so far
3. /studentCourseRelation/{student}/{course}
4. /similarCourseRelation/{courseA}/{courseB}


References:
* https://www.programcreek.com/java-api-examples/?code=promregator%2Fpromregator%2Fpromregator-master%2Fsrc%2Ftest%2Fjava%2Forg%2Fcloudfoundry%2Fpromregator%2FmockServer%2FDefaultMetricsEndpointHttpHandler.java
* https://neo4j.com/docs/api/java-driver/current/
* https://www.rgagnon.com/javadetails/java-get-url-parameters-using-jdk-http-server.html
* https://www.geeksforgeeks.org/singleton-class-java/