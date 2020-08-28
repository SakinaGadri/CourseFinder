# Course Finder Project

APIs:
Student End Points
1. /addStudent done
8. /allStudents
9. /findStudent/{student-name}
10. /findStudent/{student-id}
17. /cGPA/{student-id}
18. /cGPA/{student-name}

Course End Points
2. /addCourse
5. /findCourse/{course-code}
6. /findCourse/{course-name}
7. /allCourses
13. /courseInstructor/{course}
14. /courseAverage/{course}
15. /courseStatus/{course}
4. /eligibleCourses/{course} // given a course what other courses can I take?
16. /intersection/{courseA}/{courseB} // intersetion between two courses returns a list of students who've taken both the courses

Relationship between Course and Student
11. /similarCourses/{course}
12. /eligibleCourses/{student} // given a student, what courses can they take based on what they've taken so far

