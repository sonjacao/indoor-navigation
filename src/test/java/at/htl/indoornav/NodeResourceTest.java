package at.htl.indoornav;

import at.htl.indoornav.entity.Node;
import at.htl.indoornav.entity.NodeType;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NodeResourceTest {

    Jsonb jsonb;

    @BeforeAll
    void init() {
        jsonb = JsonbBuilder.create();
        RestAssured.defaultParser = Parser.JSON;
    }

    @Test
    void testGetAllNodes() {
        given()
                .when().get("/node")
                .then()
                .statusCode(200);
    }

    @Test
    void testCreateNode() {
        Node node = new Node(null, "5ahif", NodeType.FLOOR, false, 125f, 25f, 890f);
        given()
            .when()
                .contentType("application/json")
                .body(jsonb.toJson(node))
                .post("/node")
            .then()
                .statusCode(200);
    }

    @Test
    void testCreateNodeWithoutName() {
        Node node = new Node(null, null, NodeType.FLOOR, false, 125f, 25f, 890f);
        given()
            .when()
                .contentType("application/json")
                .body(jsonb.toJson(node))
                .post("/node")
            .then()
                .statusCode(400)
                .body("parameterViolations[0].message", is("Name may not be blank!"));
    }

    @Test
    void testCreateNodeWithBlankName() {
        Node node = new Node(null, "", NodeType.FLOOR, false, 125f, 25f, 890f);
        given()
            .when()
                .contentType("application/json")
                .body(jsonb.toJson(node))
                .post("/node")
            .then()
                .statusCode(400)
                .body("parameterViolations[0].message", is("Name may not be blank!"));
    }

}
