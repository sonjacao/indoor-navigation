package at.htl.indoornav;

import at.htl.indoornav.entity.Node;
import at.htl.indoornav.entity.NodeType;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.json.Json;
import javax.json.JsonObject;
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
            .when()
                .get("/node")
            .then()
                .statusCode(200);
    }

    @Test
    void testCreateNode() {
        Node node = new Node(null, "5ahif", NodeType.FLOOR, false, 125f, 25f, 890f);
        long id = given()
            .when()
                .contentType("application/json")
                .body(jsonb.toJson(node))
                .post("/node")
            .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getLong("id");

        given()
            .when()
                .pathParam("id", id)
                .delete("/node/{id}")
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

    @Test
    void testCreateNodeWithoutNodeType() {
        Node node = new Node(null, "5ahif", null, false, 125f, 25f, 890f);
        given()
            .when()
                .contentType("application/json")
                .body(jsonb.toJson(node))
                .post("/node")
            .then()
                .statusCode(400)
                .body("parameterViolations[0].message", is("Type may not be null!"));
    }

    @Test
    void testCreateNodeWithTypeFloorAsText() {
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("name", "4ahitm")
                .add("type", "FLOOR")
                .add("hidden", false)
                .add("x", 5)
                .add("y", 5)
                .add("z", 5)
                .build();

        long id = given()
            .when()
                .contentType("application/json")
                .body(jsonObject.toString())
                .post("/node")
            .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getLong("id");

        given()
            .when()
                .pathParam("id", id)
                .delete("/node/{id}")
            .then()
                .statusCode(200);
    }

    @Test
    void testCreateNodeWithTypeStairsAsText() {
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("name", "4ahitm")
                .add("type", "STAIRS")
                .add("hidden", false)
                .add("x", 5)
                .add("y", 5)
                .add("z", 5)
                .build();

        long id = given()
            .when()
                .contentType("application/json")
                .body(jsonObject.toString())
                .post("/node")
            .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getLong("id");

        given()
            .when()
                .pathParam("id", id)
                .delete("/node/{id}")
            .then()
                .statusCode(200);
    }

    @Test
    void testCreateNodeWithTypeElevatorAsText() {
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("name", "4ahitm")
                .add("type", "ELEVATOR")
                .add("hidden", false)
                .add("x", 5)
                .add("y", 5)
                .add("z", 5)
                .build();

        long id = given()
            .when()
                .contentType("application/json")
                .body(jsonObject.toString())
                .post("/node")
            .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getLong("id");

        given()
            .when()
                .pathParam("id", id)
                .delete("/node/{id}")
            .then()
                .statusCode(200);
    }

    @Test
    void testCreateNodeWithoutHidden() {
        Node node = new Node(null, "5ahif", NodeType.FLOOR, null, 125f, 25f, 890f);
        given()
            .when()
                .contentType("application/json")
                .body(jsonb.toJson(node))
                .post("/node")
            .then()
                .statusCode(400)
                .body("parameterViolations[0].message", is("Hidden may not be null!"));
    }

    @Test
    void testCreateNodeWithoutX() {
        Node node = new Node(null, "5ahif", NodeType.FLOOR, false, null, 25f, 890f);
        given()
            .when()
                .contentType("application/json")
                .body(jsonb.toJson(node))
                .post("/node")
            .then()
                .statusCode(400)
                .body("parameterViolations[0].message", is("X may not be null!"));
    }

    @Test
    void testCreateNodeWithoutY() {
        Node node = new Node(null, "5ahif", NodeType.FLOOR, false, 125f, null, 890f);
        given()
            .when()
                .contentType("application/json")
                .body(jsonb.toJson(node))
                .post("/node")
            .then()
                .statusCode(400)
                .body("parameterViolations[0].message", is("Y may not be null!"));
    }

    @Test
    void testCreateNodeWithoutZ() {
        Node node = new Node(null, "5ahif", NodeType.FLOOR, false, 125f, 25f, null);
        given()
            .when()
                .contentType("application/json")
                .body(jsonb.toJson(node))
                .post("/node")
            .then()
                .statusCode(400)
                .body("parameterViolations[0].message", is("Z may not be null!"));
    }

    @Test
    void testGetNodeById() {
        Node node = new Node(null, "5ahif", NodeType.FLOOR, false, 125f, 25f, 890f);
        long id = given()
            .when()
                .contentType("application/json")
                .body(jsonb.toJson(node))
                .post("/node")
            .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getLong("id");

        given()
            .when()
                .pathParam("id", id)
                .get("/node/{id}")
            .then()
                .statusCode(200);

        given()
            .when()
                .pathParam("id", id)
                .delete("/node/{id}")
            .then()
                .statusCode(200);
    }

    @Test
    void testGetNodeByNotExistingId() {
        given()
            .when()
                .pathParam("id", 170302)
                .get("/node/{id}")
            .then()
                .statusCode(404);
    }

    @Test
    void testCreateRelationship() {
        Node startNode = new Node(null, "4ahitm", NodeType.FLOOR, false, 125f, 25f, 890f);
        long startId = given()
            .when()
                .contentType("application/json")
                .body(jsonb.toJson(startNode))
                .post("/node")
            .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getLong("id");

        Node endNode = new Node(null, "5ahif", NodeType.FLOOR, false, 125f, 25f, 890f);
        long endId = given()
            .when()
                .contentType("application/json")
                .body(jsonb.toJson(endNode))
                .post("/node")
            .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getLong("id");

        given()
            .when()
                .contentType("application/json")
                .queryParam("start", startId)
                .queryParam("end", endId)
                .post("/node/relationship")
            .then()
                .statusCode(200);

        given()
            .when()
                .pathParam("id", startId)
                .delete("/node/{id}")
            .then()
                .statusCode(200);

        given()
            .when()
                .pathParam("id", endId)
                .delete("/node/{id}")
            .then()
                .statusCode(200);
    }

    @Test
    void testCreateRelationshipWithoutStartId() {
        given()
            .when()
                .contentType("application/json")
                .queryParam("end", 2)
                .post("/node/relationship")
            .then()
                .statusCode(400);
    }

}