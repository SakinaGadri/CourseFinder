package coursefinder;

public class Course {
    
    // public enum Status {Running, Done, Planned};

    String name;
    String courseCode;
    double average;
    String profName;
    String status;

    public Course(String courseName, String courseCode, String professor, double average, String status) {
        this.name = courseName;
        this.courseCode = courseCode;
        this.average = average;
        this.profName = professor;
        this.status = status;
    }
}