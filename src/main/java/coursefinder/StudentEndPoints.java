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

public class StudentEndPoints implements HttpHandler {

    private Driver driver;

    /* Establishes the Driver of the database  */
    public StudentEndPoints() {
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
                if (path.getPath().compareTo("/allStudents") == 0)
                    allStudents(exchange);
                else if (path.getPath().startsWith("/findStudent"))
                    findStudent(exchange);
                else if (path.getPath().startsWith("/cGPA"))
                    findStudentwithGPA(exchange);
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
        String name = "";
        int studentId = 0;
        double cgpa = 0.0;

        JSONObject deserialized = new JSONObject();
        try {
            body = Utils.convert(exchange.getRequestBody());
            deserialized = new JSONObject(body);
        } catch (JSONException e) {
            exchange.sendResponseHeaders(400, -1);
            return ;
        } catch (IOException e) {
            exchange.sendResponseHeaders(500, -1);
            return ;
        }

        /* get the data from the JSON */
        // name
        if (deserialized.has("name"))
            name = deserialized.getString("name");
        else {
            exchange.sendResponseHeaders(400, -1);
            return ;
        }
        // student id
        if (deserialized.has("studentId"))
            studentId = deserialized.getInt("studentId");
        else {
            exchange.sendResponseHeaders(400, -1);
            return ;
        }
        // cgpa
        if (deserialized.has("cGPA"))
            cgpa = deserialized.getDouble("cGPA");
        else {
            exchange.sendResponseHeaders(400, -1);
            return ;
        }

        // create a node; if already exists, then update the name
        int errorcheck = checkAndCreateNode(name, studentId, cgpa);

        // failure to excute query => status code = 400
        if (errorcheck == 0) {
            exchange.sendResponseHeaders(404, -1);
            return ;
        }

        exchange.sendResponseHeaders(200, 1);
        OutputStream os = exchange.getResponseBody();
        os.write("".getBytes());
        os.close();
    }

    private int checkAndCreateNode(String name, int studentId, double cgpa) {
        if (cgpa < 0.0 || cgpa > 4.0)
            return (0);
        try (Session session = driver.session()) {
            // making our cypher query
            session.run(
                    "MERGE (student:Student {id: $studentId}) "
                            + "ON MATCH SET student.name = $name, student.cgpa = $cgpa"
                            + " ON CREATE SET student.name = $name, student.id = $studentId, student.cgpa = $cgpa",
                    parameters("name", name, "studentId", studentId, "cgpa", cgpa));
            return (1);
        } catch (Exception e) {
            e.printStackTrace();
            return (0);
        }
    }

    private void allStudents(HttpExchange exchange) throws IOException {
        String response = "";
        // get all the students
        try (Session session = driver.session()) {
            // cypher query
            Result result = session.run("MATCH (n: Student) RETURN n.name as name, n.id as id, n.cgpa as cgpa");
            // if no results found, then set the status to 404
            if (result.hasNext() == false) {
                exchange.sendResponseHeaders(404, -1);
                return ;
            }
            else {
                // otherwise, loop through all the results and create your json string
                List<Student> students = new ArrayList<Student>();
                while (result.hasNext()) {
                    Record rec = result.next();
                    Student student = new Student(rec.get("name").asString(), rec.get("id").asInt(),
                            rec.get("cgpa").asDouble());
                    students.add(student);
                }
                // create the json response
                response = "[";
                for (Student s : students) {
                    // create the student info and concat it to the response
                    String student_info = "{ \"name\" : " + s.name + ", \"id\" : " + s.id + ", \"cGPA\" : " + s.cgpa
                            + " },";
                    response = response.concat(student_info);
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
            exchange.sendResponseHeaders(400, response.length());
        }
    }

    private void findStudent(HttpExchange exchange) throws IOException {
        String response = "";
        // get the id or name in the uri
        Map<String, String> params = Utils.convertFromGetToMap(exchange, new String[] { "studentId", "studentName" });
        // cannot find the student if the id and name isn't provided
        if (!params.containsKey("studentId") && !params.containsKey("studentName")) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }
        try (Session session = driver.session()) {
            // cypher query
            Result result;
            if (params.containsKey("studentId")) {
                result = session.run(
                        "MATCH (n: Student) where n.id = $id RETURN n.name as name, n.id as id, n.cgpa as cgpa",
                        parameters("id", Integer.parseInt(params.get("studentId"))));
            } else {
                result = session.run(
                        "MATCH (n: Student) where n.name = $name RETURN n.name as name, n.id as id, n.cgpa as cgpa",
                        parameters("name", params.get("studentName")));
            }
            // if no results found, then set the status to 404
            if (result.hasNext() == false) {
                exchange.sendResponseHeaders(404, -1);
                return;
            } else {
                // otherwise, loop through all the results and create your json string
                List<Student> students = new ArrayList<Student>();
                while (result.hasNext()) {
                    Record rec = result.next();
                    Student student = new Student(rec.get("name").asString(), rec.get("id").asInt(),
                            rec.get("cgpa").asDouble());
                    students.add(student);
                }
                // create the json response
                response = "[";
                for (Student s : students) {
                    // create the student info and concat it to the response
                    String student_info = "{ \"name\" : " + s.name + ", \"id\" : " + s.id + ", \"cGPA\" : " + s.cgpa
                            + " },";
                    response = response.concat(student_info);
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

    private void findStudentwithGPA(HttpExchange exchange) throws IOException {
        String response = "";
        // get the id or name in the uri
        Map<String, String> params = Utils.convertFromGetToMap(exchange, new String[] { "studentId", "studentName" });
        // cannot find the student if the id and name isn't provided
        if (!params.containsKey("studentId") && !params.containsKey("studentName")) {
            exchange.sendResponseHeaders(404, -1);
            return;
        }
        try (Session session = driver.session()) {
            // cypher query
            Result result;
            if (params.containsKey("studentId")) {
                result = session.run("MATCH (n: Student) where n.id = $id RETURN n.cgpa as cgpa",
                        parameters("id", Integer.parseInt(params.get("studentId"))));
            } else {
                result = session.run("MATCH (n: Student) where n.name = $name RETURN n.cgpa as cgpa",
                        parameters("name", params.get("studentName")));
            }
            // if no results found, then set the status to 404
            if (result.hasNext() == false) {
                exchange.sendResponseHeaders(404, -1);
                return;
            } else {
                // create the json response
                String student_info = "{ \"cGPA\" : " + result.single().get("cgpa").asDouble() + " }";
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