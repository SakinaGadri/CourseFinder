package coursefinder;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

/* Singleton Class for the Graph Database */
public class GraphDriver {
    private static Driver driver = null;

    /* Initializes the driver for the database */
    private GraphDriver() {
        driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "password"));
    }

    /* Get a singleton instance of the driver for the database */
    public static Driver getDriver() {
        if (driver == null) {
            new GraphDriver();
        }
        return driver;
    }
}