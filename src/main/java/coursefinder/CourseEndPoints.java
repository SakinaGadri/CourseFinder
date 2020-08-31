package coursefinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.OutputStream;

import org.json.*;
import org.neo4j.driver.Result;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;

// import coursefinder.Course.Status;

import static org.neo4j.driver.Values.parameters;

import com.sun.net.httpserver.HttpExchange;
import java.net.URI;
import com.sun.net.httpserver.HttpHandler;

public class CourseEndPoints implements HttpHandler {

    private Driver driver;

    public CourseEndPoints() {
        driver = GraphDriver.getDriver();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("PUT")) {
            try {
                handlePut(exchange);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (exchange.getRequestMethod().equals("GET")) {
            try {
                URI path = exchange.getRequestURI();
                if (path.getPath().compareTo("/allCourses") == 0)
                    allCourses(exchange);
                else if (path.getPath().startsWith("/findCourse"))
                    findCourse(exchange);
                else if (path.getPath().startsWith("/courseProfessor"))
                    findProfessor(exchange);
                else if (path.getPath().startsWith("/courseAverage"))
                    getAverage(exchange);
                else if (path.getPath().startsWith("/courseStatus"))
                    getStatus(exchange);
                else
                    exchange.sendResponseHeaders(500, -1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            exchange.sendResponseHeaders(500, -1);
        }
    }

    private void handlePut(HttpExchange exchange) throws IOException, JSONException {
        // convert the body to a JSON
        String body = "";
        /* Items in a course */
        String name = "", courseCode = "", prof = "";
        String status = "";
        double average = 0.0;

        JSONObject deserialized = new JSONObject();
        try {
            body = Utils.convert(exchange.getRequestBody());
            deserialized = new JSONObject(body);
        } catch (JSONException e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(400, -1);
            return;
        } catch (IOException e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1);
            return;
        }

        /* get the data from the JSON */
        // name
        if (deserialized.has("name"))
            name = deserialized.getString("name");
        else {
            exchange.sendResponseHeaders(400, -1);
            return;
        }
        // course code
        if (deserialized.has("code"))
            courseCode = deserialized.getString("code");
        else {
            exchange.sendResponseHeaders(400, -1);
            return;
        }
        // average
        if (deserialized.has("average"))
            average = deserialized.getDouble("average");
        else {
            exchange.sendResponseHeaders(400, -1);
            return;
        }
        // prof name
        if (deserialized.has("professor"))
            prof = deserialized.getString("professor");
        else {
            exchange.sendResponseHeaders(400, -1);
            return;
        }
        // status
        if (deserialized.has("status"))
            status = deserialized.getString("status");
        else {
            exchange.sendResponseHeaders(400, -1);
            return;
        }
        // create a node; if already exists, then update the name
        int errorcheck = checkAndCreateNode(name, courseCode, average, prof, status);

        // failure to excute query => status code = 400
        if (errorcheck == 0) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }
        // all good, send 200
        exchange.sendResponseHeaders(200, 1);
        OutputStream os = exchange.getResponseBody();
        os.write("".getBytes());
        os.close();
    }

    private int checkAndCreateNode(String name, String courseCode, double average, String profName, String status) {
        try (Session session = driver.session()) {
            // making our cypher query
            session.run("MERGE (course:Course {code: $courseCode}) "
                    + "ON MATCH SET course.name = $name, course.average = $average, course.professor = $prof, course.status = $status "
                    + "ON CREATE SET course.name = $name, course.average = $average, course.code = $courseCode, "
                    + "course.professor = $prof, course.status = $status",
                    parameters("name", name, "courseCode", courseCode, "average", average, "prof", profName, "status",
                            status));
            return (1);
        } catch (Exception e) {
            e.printStackTrace();
            return (0);
        }
    }

    private void allCourses(HttpExchange exchange) throws IOException {
        String response = "";
        // get all the students
        try (Session session = driver.session()) {
            // cypher query
            Result result = session.run("MATCH (n: Course)" + " RETURN n.name as name, n.code as code,"
                    + " n.professor as prof, n.average as average, n.status as status");
            // if no results found, then set the status to 404
            if (result.hasNext() == false) {
                exchange.sendResponseHeaders(404, -1);
                return;
            } else {
                // otherwise, loop through all the results and create your json string
                List<Course> courses = new ArrayList<Course>();
                while (result.hasNext()) {
                    Record rec = result.next();
                    Course course = new Course(rec.get("name").asString(), rec.get("code").asString(),
                            rec.get("prof").asString(), rec.get("average").asDouble(), rec.get("status").asString());
                    courses.add(course);
                }
                // create the json response
                response = "[";
                for (Course c : courses) {
                    // create the student info and concat it to the response
                    String course_info = "{ \"name\" : " + c.name + ", \"code\" : " + c.courseCode + ", \"average\" : "
                            + c.average + ", \"professor\" : " + c.profName + ", \"status\" : " + c.status + " },";
                    response = response.concat(course_info);
                }
                // replace the last comma and add the closing bracket
                response = response.replaceAll(",$", "");
                response = response.concat("]");
            }
            // send back appropriate responses
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (Exception e) {
            // return with a 400 if there is an exception anywhere
            e.printStackTrace();
            exchange.sendResponseHeaders(400, response.length());
        }
    }

    private void findCourse(HttpExchange exchange) throws IOException {
        String response = "";
        // get the id or name in the uri
        Map<String, String> params = Utils.convertFromGetToMap(exchange, new String[] { "courseCode", "courseName" });
        // cannot find the student if the id and name isn't provided
        if (!params.containsKey("courseCode") && !params.containsKey("courseName")) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }
        try (Session session = driver.session()) {
            // cypher query
            Result result;
            if (params.containsKey("courseCode")) {
                result = session.run("MATCH (c: Course) where c.code = $code"
                        + " RETURN c.name as name, c.code as code, c.average as average, c.professor as prof, c.status as status",
                        parameters("code", params.get("courseCode")));
            } else {
                result = session.run("MATCH (c: Course) where c.name = $name"
                        + " RETURN c.name as name, c.code as code, c.average as average, c.professor as prof, c.status as status",
                        parameters("name", params.get("courseName")));
            }
            // if no results found, then set the status to 404
            if (result.hasNext() == false) {
                exchange.sendResponseHeaders(404, -1);
                return;
            } else {
                // otherwise, loop through all the results and create your json string
                List<Course> courses = new ArrayList<Course>();
                while (result.hasNext()) {
                    Record rec = result.next();
                    Course course = new Course(rec.get("name").asString(), rec.get("code").asString(),
                            rec.get("prof").asString(), rec.get("average").asDouble(), rec.get("status").asString());
                    courses.add(course);
                }
                // create the json response
                response = "[";
                for (Course c : courses) {
                    // create the student info and concat it to the response
                    String course_info = "{ \"name\" : " + c.name + ", \"code\" : " + c.courseCode + ", \"average\" : "
                            + c.average + ", \"professor\" : " + c.profName + ", \"status\" : " + c.status + " },";
                    response = response.concat(course_info);
                }
                // replace the last comma and add the closing bracket
                response = response.replaceAll(",$", "");
                response = response.concat("]");
            }
            // send back appropriate responses
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (Exception e) {
            // return with a 400 if there is an exception anywhere
            e.printStackTrace();
            exchange.sendResponseHeaders(400, response.length());
        }
    }

    private void findProfessor(HttpExchange exchange) throws IOException {
        String response = "";
        // get the id or name in the uri
        Map<String, String> params = Utils.convertFromGetToMap(exchange, new String[] { "courseName", "courseCode" });
        // cannot find the student if the id and name isn't provided
        if (!params.containsKey("courseCode") && !params.containsKey("courseName")) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }
        try (Session session = driver.session()) {
            // cypher query
            Result result;
            if (params.containsKey("courseCode")) {
                result = session.run("MATCH (n: Course) where n.code = $code RETURN n.professor as prof",
                        parameters("code", params.get("courseCode")));
            } else {
                result = session.run("MATCH (n: Course) where n.name = $name RETURN n.professor as prof",
                        parameters("name", params.get("courseName")));
            }
            // if no results found, then set the status to 404
            if (result.hasNext() == false) {
                exchange.sendResponseHeaders(404, -1);
                return;
            } else {
                // create the json response
                String student_info = "{ \"professor\" : " + result.single().get("prof").asString() + " }";
                response = response.concat(student_info);
            }
            // send back appropriate responses
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (Exception e) {
            // return with a 400 if there is an exception anywhere
            e.printStackTrace();
            exchange.sendResponseHeaders(400, response.length());
        }
    }

    private void getAverage(HttpExchange exchange) throws IOException {
        String response = "";
        // get the id or name in the uri
        Map<String, String> params = Utils.convertFromGetToMap(exchange, new String[] { "courseName", "courseCode" });
        // cannot find the student if the id and name isn't provided
        if (!params.containsKey("courseCode") && !params.containsKey("courseName")) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }
        try (Session session = driver.session()) {
            // cypher query
            Result result;
            if (params.containsKey("courseCode")) {
                result = session.run("MATCH (n: Course) where n.code = $code RETURN n.average as avg",
                        parameters("code", params.get("courseCode")));
            } else {
                result = session.run("MATCH (n: Course) where n.name = $name RETURN n.average as avg",
                        parameters("name", params.get("courseName")));
            }
            // if no results found, then set the status to 404
            if (result.hasNext() == false) {
                exchange.sendResponseHeaders(404, -1);
                return;
            } else {
                // create the json response
                String student_info = "{ \"average\" : " + result.single().get("avg").asString() + " }";
                response = response.concat(student_info);
            }
            // send back appropriate responses
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (Exception e) {
            // return with a 400 if there is an exception anywhere
            e.printStackTrace();
            exchange.sendResponseHeaders(400, response.length());
        }
    }

    private void getStatus(HttpExchange exchange) throws IOException {
        String response = "";
        // get the id or name in the uri
        Map<String, String> params = Utils.convertFromGetToMap(exchange, new String[] { "courseName", "courseCode" });
        // cannot find the student if the id and name isn't provided
        if (!params.containsKey("courseCode") && !params.containsKey("courseName")) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }
        try (Session session = driver.session()) {
            // cypher query
            Result result;
            if (params.containsKey("courseCode")) {
                result = session.run("MATCH (n: Course) where n.code = $code RETURN n.status as status",
                        parameters("code", params.get("courseCode")));
            } else {
                result = session.run("MATCH (n: Course) where n.name = $name RETURN n.status as status",
                        parameters("name", params.get("courseName")));
            }
            // if no results found, then set the status to 404
            if (result.hasNext() == false) {
                exchange.sendResponseHeaders(404, -1);
                return;
            } else {
                // create the json response
                String student_info = "{ \"status\" : " + result.single().get("status").asString() + " }";
                response = response.concat(student_info);
            }
            // send back appropriate responses
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (Exception e) {
            // return with a 400 if there is an exception anywhere
            e.printStackTrace();
            exchange.sendResponseHeaders(400, response.length());
        }
    }
}