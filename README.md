# Course Finder Project

A backend project that keeps track of Students and Courses, built with Java and Neo4j. 

There are two node labels in the database:
1. Student, with the following properties:
    - `id`: (int) student id
    - `name`: (string) student name
    - `cGPA`: (int) cGPA of student 
2. Course, with the following properties:
    - `code` (string): course code
    - `name` (string): course name
    - `average` (double): average of the course
    - `professor` (string): professor who teaches the course
    - `status` (string): status of the course. Can be one of "Running", "Done", "Scheduled"  

There are three types of relationships:
1. Between Courses and Students: A Student and a Course have a relationship of "TAKEN".
2. Between Course and Course: A Course can be similar to another Course. Therefore a relationship of "SIMILAR" can exist between them.
3. Between Course and Course: A Course can be a requisite of another Course. Therefore, a relationship of "REQUISITE" can exist between them.

## Running the project:
To run the project:
1. Ensure you have Java 11, Maven and Neo4j Community Version installed.
2. Run the [Neo4j driver](https://neo4j.com/download-center/#community). To see the changes made in the database, go to localhost:7474.
3. `cd` into the folder that has the file `pom.xml`. Run the command: `mvn compile; mvn exec:java`
4. You can run the APIs using Postman or `curl` commands given in each API description, on `localhost:8080`.

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
       http://localhost:8080/addCourse'
```

### Read

- description: retrieves all the courses in the database
- request: `GET /allCourses`
- response: 200
    - content-type: `application/json`
    - body: list of objects
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

## Relationship between Course and Student

### Create

* description: Creates a `TAKEN` relationship between the student, with the given `studentId`, and course, with the given `courseCode`. The relationship created is from student to course.
* request: PATCH `/studentCourseRelation?student={studentId}&course={courseCode}`
* response: 200
    * content-type: `application/json`
    * body: object
        - `studentId` (int)
        - `courseCode` (string)
        - `relationship` (string): Type of the relationship created. For this call, it will be `TAKEN`
* response: 400
    - body: Bad Request
* response: 500 
    - body: Server Side Error
* example:
``` 
$ curl -X PATCH http://localhost:8080/studentCourseRelation?student=1004&courseCode=A48'
```

* description: Creates a `SIMILAR` relationship between the courses, with the given `courseCode`. The relationship is both ways, from courseCodeA to courseCodeB, and courseCodeB and courseCodeA.
* request: PATCH `/requisiteCourseRelation?courseA={courseCodeA}&courseB={courseCodeB}`
* response: 200
    * content-type: `application/json`
    * body: object
        - `courseCode` (string)
        - `courseCode` (string)
        - `relationship` (string): Type of the relationship created. For this call, it will be `SIMILAR`
* response: 400
    - body: Bad Request
* response: 500 
    - body: Server Side Error
* example:
``` 
$ curl -X PATCH http://localhost:8080/similarCourseRelation?courseA=A48&courseB=B07'
```

* description: Creates a `REQUISITE` relationship between the courses, with the given `courseCode`. The relationship created is from `courseCodeA` to `courseCodeB`.
* request: PATCH `/requisiteCourseRelation?courseA={courseCodeA}&courseB={courseCodeB}`
* response: 200
    * content-type: `application/json`
    * body: object
        - `courseCode` (string)
        - `courseCode` (string)
        - `relationship` (string): Type of the relationship created. For this call, it will be `REQUISITE`
* response: 400
    - body: Bad Request
* response: 500 
    - body: Server Side Error
* example:
``` 
$ curl -X PATCH http://localhost:8080/requisiteCourseRelation?courseA=A48&courseB=B07'
```

### Read

* description: Returns all courses that `courseCode` has `SIMILAR` relationship with.
* request: GET `/similarCourses?course={courseCode}`
* response: 200
    * content-type: `application/json`
    * body: object
        - `relationship` (string): Type of the relationship created. For this call, it will be `SIMILAR`
        - `courses`: List of objects:
            - `name` (string): name of the course
            - `code` (string): code of the course
* response: 400
    - body: Bad Request
* response: 500 
    - body: Server Side Error
* example:
``` 
$ curl -X PATCH http://localhost:8080/similarCourses?course=A48'
```

* description: Returns all courses that `studentId` is eligible to take. To be eligible to take a course, a student needs to have taken a course's requisite courses.
* request: GET `/eligibleCourses?student={studentId}`
* response: 200
    * content-type: `application/json`
    * body: list of objects
        - `name` (string): name of the course
        - `code` (string): code of the course
* response: 400
    - body: Bad Request
* response: 500 
    - body: Server Side Error
* example:
``` 
$ curl -X PATCH http://localhost:8080/eligibleCourses?student=1004'
```

* description: Returns all courses that `courseCode` has `REQUISITE` relationship with.
* request: GET `/requisiteCourses?course={courseCode}`
* response: 200
    * content-type: `application/json`
    * body: object
        - `relationship` (string): Type of the relationship created. For this call, it will be `REQUISITE`
        - `courses`: List of objects
            - `name` (string): name of the course
            - `code` (string): code of the course
* response: 400
    - body: Bad Request
* response: 500 
    - body: Server Side Error
* example:
``` 
$ curl -X PATCH http://localhost:8080/requisiteCourses?course=A48'
```

References:
* https://www.programcreek.com/java-api-examples/?code=promregator%2Fpromregator%2Fpromregator-master%2Fsrc%2Ftest%2Fjava%2Forg%2Fcloudfoundry%2Fpromregator%2FmockServer%2FDefaultMetricsEndpointHttpHandler.java
* https://neo4j.com/docs/api/java-driver/current/
* https://www.rgagnon.com/javadetails/java-get-url-parameters-using-jdk-http-server.html
* https://www.geeksforgeeks.org/singleton-class-java/