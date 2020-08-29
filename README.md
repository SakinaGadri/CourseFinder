# Course Finder Project

APIs:
Student End Points
1. /addStudent done
2. /allStudents done
3. /findStudent/{student-name} done
4. /findStudent/{student-id} done
5. /cGPA/{student-id} done
6. /cGPA/{student-name} done

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