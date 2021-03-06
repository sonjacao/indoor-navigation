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
public class RelationshipResourceTest {

    Jsonb jsonb;

    @BeforeAll
    void init() {
        jsonb = JsonbBuilder.create();
        RestAssured.defaultParser = Parser.JSON;
    }

    @Test
    void testCreateRelationship() {
        Node startNode = new Node("4ahitm", NodeType.FLOOR, false, 125f, 25f, 890f);
        createNode(startNode);

        Node endNode = new Node("4bhitm", NodeType.FLOOR, false, 300f, 25f, 450f);
        createNode(endNode);

        given()
            .when()
                .contentType("application/json")
                .queryParam("start", startNode.getName())
                .queryParam("end", endNode.getName())
                .post("/relationship")
            .then()
                .statusCode(200);

        deleteNode(startNode.getName());
        deleteNode(endNode.getName());
    }

    @Test
    void testCreateRelationshipWithoutStart() {
        given()
            .when()
                .contentType("application/json")
                .queryParam("end", "4ahitm")
                .post("/relationship")
            .then()
                .statusCode(400);
    }

    @Test
    void testCreateRelationshipWithoutEnd() {
        given()
            .when()
                .contentType("application/json")
                .queryParam("start", "4ahitm")
                .post("/relationship")
            .then()
                .statusCode(400);
    }

    @Test
    void testCreateRelationshipWithNotExistingNames() {
        given()
            .when()
                .contentType("application/json")
                .queryParam("start", "69ahitm")
                .queryParam("end", "96ahitm")
                .post("/relationship")
            .then()
                .statusCode(404);
    }

    @Test
    void testDeleteRelationship() {
        Node startNode = new Node("4ahitm", NodeType.FLOOR, false, 125f, 25f, 890f);
        createNode(startNode);

        Node endNode = new Node("4bhitm", NodeType.FLOOR, false, 300f, 25f, 450f);
        createNode(endNode);

        given()
            .when()
                .contentType("application/json")
                .queryParam("start", "4ahitm")
                .queryParam("end", "4bhitm")
                .delete("/relationship")
            .then()
                .statusCode(204);

        deleteNode("4ahitm");
        deleteNode("4bhitm");
    }

    @Test
    void testDeleteRelationshipWithoutStart() {
        given()
            .when()
                .contentType("application/json")
                .queryParam("end", "4ahitm")
                .post("/relationship")
            .then()
                .statusCode(400);
    }

    @Test
    void testDeleteRelationshipWithoutEnd() {
        given()
            .when()
                .contentType("application/json")
                .queryParam("start", "4ahitm")
                .post("/relationship")
            .then()
                .statusCode(400);
    }

    @Test
    void testDeleteRelationshipWithNotExistingNames() {
        given()
            .when()
                .contentType("application/json")
                .queryParam("start", "69ahitm")
                .queryParam("end", "96ahitm")
            .post("/relationship")
                .then()
                .statusCode(404);
    }

    void createNode(Node node) {
        given()
            .when()
                .contentType("application/json")
                .body(jsonb.toJson(node))
                .post("/node")
            .then()
                .statusCode(200)
                .body("name", is(node.getName()));
    }

    void deleteNode(String name) {
        given()
            .when()
                .pathParam("name", name)
                .delete("/node/{name}")
            .then()
                .statusCode(200);
    }
}
