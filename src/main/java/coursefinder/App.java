package coursefinder;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class App 
{
    static int PORT = 8080;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", PORT), 0);
        server.start();
        
        /* Student APIs */
        server.createContext("/addStudent", new StudentEndPoints());
        server.createContext("/allStudents", new StudentEndPoints());
        server.createContext("/findStudent", new StudentEndPoints());
        server.createContext("/cGPA", new StudentEndPoints());


        /* Course APIs */
        server.createContext("/addCourse", new CourseEndPoints());
        server.createContext("/allCourses", new CourseEndPoints());
        
        server.createContext("/findCourse", new CourseEndPoints());
        server.createContext("/courseInstructor", new CourseEndPoints());
        server.createContext("/courseAverage", new CourseEndPoints());
        server.createContext("/courseStatus", new CourseEndPoints());
        server.createContext("/requisteCourses", new CourseEndPoints());
        server.createContext("/commonCourses", new CourseEndPoints());

        /* Relationship between Student and Courses */

        System.out.printf("Server started on port %d...\n", PORT);
    }
}