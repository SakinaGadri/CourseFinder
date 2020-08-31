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

import static org.neo4j.driver.Values.parameters;

import com.sun.net.httpserver.HttpExchange;
import java.net.URI;
import com.sun.net.httpserver.HttpHandler;

public class RelationEndPoints implements HttpHandler {
    private Driver driver;

    public RelationEndPoints() {
        driver = GraphDriver.getDriver();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("PATCH")) {
            try {
                URI path = exchange.getRequestURI();
                if (path.getPath().startsWith("/studentCourseRelation"))
                    studentCourseRelation(exchange);
                else if (path.getPath().startsWith("/similarCourseRelation"))
                    similarCourseRelation(exchange);
                else if (path.getPath().startsWith("/requisiteCourseRelation"))
                    requisiteCourseRelation(exchange);
                else
                    exchange.sendResponseHeaders(500, -1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (exchange.getRequestMethod().equals("GET")) {
            try {
                URI path = exchange.getRequestURI();
                if (path.getPath().startsWith("/similarCourses"))
                    getSimilarCourses(exchange);
                else if (path.getPath().startsWith("/eligibleCourses"))
                    getEligibleCourses(exchange);
                else if (path.getPath().startsWith("/requisiteCourses"))
                    getRequisiteCourses(exchange);
                else
                    exchange.sendResponseHeaders(500, -1);
            } catch (Exception e) {

            }
        } else {
            exchange.sendResponseHeaders(500, -1);
        }
    }

    // /studentCourseRelation?student={studentId}&course={courseCode}
    private void studentCourseRelation(HttpExchange exchange) throws IOException, JSONException {
        // get the student id and course code from the query parameter passed in
        Map<String, String> params = Utils.convertFromGetToMap(exchange, new String[] { "student", "course" });
        // error check => 400 Bad Request
        if (!params.containsKey("student") || !params.containsKey("course")) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }
        try (Session session = driver.session()) {
            // make the cypher query
            Result result = session.run(
                    "MATCH (s:Student) where s.id = $id MATCH (c:Course) where c.code = $code "
                            + "MERGE (s)-[r:TAKEN]->(c) RETURN type(r) as type",
                    parameters("id", Integer.parseInt(params.get("student")), "code", params.get("course")));

            // error check => 404
            if (!result.hasNext()) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            // create the respone
            String response = "{ \"studentId\" : " + params.get("student") + ", \"courseCode\" : "
                    + params.get("course") + ", \"relationship\" : " + result.single().get("type").asString() + " }";
            // send back appropriate responses
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

        } catch (Exception e) {
            // error while making query => 500 server side error
            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1);
            return;
        }
    }

    // /similarCourseRelation?courseA={courseCodeA}&courseB={courseCodeB}
    private void similarCourseRelation(HttpExchange exchange) throws IOException {
        // get the student id and course code from the query parameter passed in
        Map<String, String> params = Utils.convertFromGetToMap(exchange, new String[] { "courseA", "courseB" });
        // error check => 400 Bad Request
        if (!params.containsKey("courseA") || !params.containsKey("courseB")
                || params.get("courseA").compareTo(params.get("courseB")) == 0) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }
        try (Session session = driver.session()) {
            // make the cypher query
            Result result = session.run(
                    "MATCH (a:Course) where a.code = $codeA MATCH (b:Course) where b.code = $codeB "
                            + "MERGE (a)-[r:SIMILAR]->(b) MERGE (a)<-[:SIMILAR]-(b) RETURN type(r) as type",
                    parameters("codeA", params.get("courseA"), "codeB", params.get("courseB")));

            // error check => 404
            if (!result.hasNext()) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            // create the respone
            String response = "{ \"courseCode\" : " + params.get("courseA") + ", \"courseCode\" : "
                    + params.get("courseB") + ", \"relationship\" : " + result.single().get("type").asString() + " }";
            // send back appropriate responses
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

        } catch (Exception e) {
            // error while making query => 500 server side error
            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1);
            return;
        }
    }

    // /requisiteCourseRelation?courseA={courseCodeA}&courseB={courseCodeB}
    private void requisiteCourseRelation(HttpExchange exchange) throws IOException {
        // get the student id and course code from the query parameter passed in
        Map<String, String> params = Utils.convertFromGetToMap(exchange, new String[] { "courseA", "courseB" });
        // error check => 400 Bad Request
        if (!params.containsKey("courseA") || !params.containsKey("courseB")
                || params.get("courseA").compareTo(params.get("courseB")) == 0) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }
        try (Session session = driver.session()) {
            // make the cypher query
            Result result = session.run(
                    "MATCH (a:Course) where a.code = $codeA MATCH (b:Course) where b.code = $codeB "
                            + "MERGE (a)-[r:REQUISITE]->(b) RETURN type(r) as type",
                    parameters("codeA", params.get("courseA"), "codeB", params.get("courseB")));

            // error check => 404
            if (!result.hasNext()) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            // create the respone
            String response = "{ \"courseCode\" : " + params.get("courseA") + ", \"courseCode\" : "
                    + params.get("courseB") + ", \"relationship\" : " + result.single().get("type").asString() + " }";
            // send back appropriate responses
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

        } catch (Exception e) {
            // error while making query => 500 server side error
            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1);
            return;
        }
    }

    // /similarCourses?course={courseCode}
    // {relationship: "", courses: [{name, code}]}
    private void getSimilarCourses(HttpExchange exchange) throws IOException {
        getRelationCourses(exchange, "SIMILAR");
    }

    // /eligibleCourses?student={studentId}
    private void getEligibleCourses(HttpExchange exchange) throws IOException {
        // get the query values
        Map<String, String> params = Utils.convertFromGetToMap(exchange, new String[] { "student" });
        if (!params.containsKey("student")) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }
        // make the cypher query
        try (Session session = driver.session()) {
            /*
             * match (:Student)-[:TAKEN]->(:Course)-[:REQUISITE]->(c:Course) Return c.name
             * as name, c.code as code
             */
            Result result = session.run(
                    "MATCH (:Student)-[:TAKEN]->(:Course)-[:REQUISITE]->(c:Course) Return c.name as name, c.code as code",
                    parameters("id", params.get("student")));

            if (!result.hasNext()) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }
            List<Course> courses = new ArrayList<Course>();
            while (result.hasNext()) {
                Record rec = result.next();
                Course course = new Course(rec.get("name").asString(), rec.get("code").asString(), "", 0, "");
                courses.add(course);
            }
            // create json response
            String response = "[";
            for (Course c : courses) {
                // create the student info and concat it to the response
                String course_info = "{ \"name\" : " + c.name + ", \"code\" : " + c.courseCode + " },";
                response = response.concat(course_info);
            }
            // replace the last comma and add the closing bracket
            response = response.replaceAll(",$", "");
            response = response.concat("]");
            // send back appropriate responses
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

        } catch (Exception e) {
            // server side error
            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1);
        }
    }

    // /requisiteCourses?course={courseCode} requisite
    // {relationship: "REQUISITE", courses: [{name, code}]}
    private void getRequisiteCourses(HttpExchange exchange) throws IOException {
        getRelationCourses(exchange, "REQUISITE");
    }

    private void getRelationCourses(HttpExchange exchange, String rel) throws IOException {
        // get the query paramters
        Map<String, String> params = Utils.convertFromGetToMap(exchange, new String[] { "course" });
        // error check => 400 Bad Request
        if (!params.containsKey("course")) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }
        try (Session session = driver.session()) {
            // make the cypher query
            Result result = session.run("MATCH (a:Course {code: $code})-[r: " + rel
                    + "]->(c:Course) RETURN type(r) as relation," + " c.name as name, c.code as code",
                    parameters("code", params.get("course")));

            // error check => 404
            if (!result.hasNext()) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }
            // create a json string
            String relation = "";
            List<Course> courses = new ArrayList<Course>();
            while (result.hasNext()) {
                Record rec = result.next();
                relation = rec.get("relation").asString();
                Course course = new Course(rec.get("name").asString(), rec.get("code").asString(), "", 0, "");
                courses.add(course);
            }
            String response = "{ \"relationship\" : " + relation + ", [";
            for (Course c : courses) {
                String course_info = "{ \"name\" : " + c.name + ", \"code\" : " + c.courseCode + " },";
                response = response.concat(course_info);

            }
            response = response.replaceAll(",$", "");
            response = response.concat("]");
            // return the response accordingly
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1);
        }
    }

}