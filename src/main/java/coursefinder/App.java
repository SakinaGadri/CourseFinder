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
        server.createContext("/addStudent", new StudentEndPoints("neo4j", "password"));
        // server.createContext("/allStudents", new StudentEndPoints("neo4j", "password"));
        // server.createContext("/findStudent/{student-name}", new StudentEndPoints("neo4j", "password"));
        // server.createContext("/findStudent/{student-id}", new StudentEndPoints("neo4j", "password"));
        // server.createContext("/cGPA/{student-id}", new StudentEndPoints("neo4j", "password"));
        // server.createContext("/cGPA/{student-name}", new StudentEndPoints("neo4j", "password"));

        /* Course APIs */
        // server.createContext("/addCourse", new CoursesEndPoints("neo4j", "password"));

        /* Relationship between Student and Courses */

        System.out.printf("Server started on port %d...\n", PORT);
    }
}