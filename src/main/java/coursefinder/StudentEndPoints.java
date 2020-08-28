package coursefinder;

import java.io.IOException;
// import java.util.ArrayList;
import java.io.OutputStream;

import org.json.*;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Driver;
// import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
// import org.neo4j.driver.Value;

import static org.neo4j.driver.Values.parameters;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class StudentEndPoints implements HttpHandler {

    private Driver driver;

    public StudentEndPoints(String username, String password) {
        driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic(username, password));
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
                // handleGet(exchange);
                System.out.printf("handling GET..\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            exchange.sendResponseHeaders(500, 0);
        }
    }

    private void handlePut(HttpExchange exchange) throws IOException, JSONException {
        // convert the body to a JSON
        String body = "", response = "";
        String name = "";
        int statusCode = 200, studentId = 0;
        double cgpa = 0.0;

        JSONObject deserialized = new JSONObject();
        try {
            body = Utils.convert(exchange.getRequestBody());
            deserialized = new JSONObject(body);
        } catch (JSONException e) {
            exchange.sendResponseHeaders(400, response.length());
        } catch (IOException e) {
            exchange.sendResponseHeaders(500, response.length());
        }

        /* get the data from the JSON */
        // name
        if (deserialized.has("name"))
            name = deserialized.getString("name");
        else {
            exchange.sendResponseHeaders(400, response.length());
        }
        // student id
        if (deserialized.has("studentId"))
            studentId = deserialized.getInt("studentId");
        else {
            exchange.sendResponseHeaders(400, response.length());
        }
        // cgpa
        if (deserialized.has("cGPA"))
            cgpa = deserialized.getDouble("cGPA");
        else {
            exchange.sendResponseHeaders(400, response.length());
        }

        // create a node; if already exists, then update the name
        int errorcheck = checkAndCreateNode(name, studentId, cgpa);

        // failure to excute query => status code = 400
        if (errorcheck == 0) {
            statusCode = 400;
        }

        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private int checkAndCreateNode(String name, int studentId, double cgpa) {
        System.out.printf("name %s, id %d cgpa %f\n", name, studentId, cgpa);
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
}