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
        Node node = new Node(null, "4ahitm", NodeType.FLOOR, false, 125f, 25f, 890f);
        createNode(node);
        deleteNode(node.getName());
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
                .body("failedFields[0].message", is("Name may not be blank!"));
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
                .body("failedFields[0].message", is("Name may not be blank!"));
    }

    @Test
    void testCreateNodeWithoutNodeType() {
        Node node = new Node(null, "4ahitm", null, false, 125f, 25f, 890f);
        given()
            .when()
                .contentType("application/json")
                .body(jsonb.toJson(node))
                .post("/node")
            .then()
                .statusCode(400)
                .body("failedFields[0].message", is("Type may not be null!"));
    }

    @Test
    void testCreateNodeWithTypeFloorAsText() {
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("name", "4ahitm")
                .add("type", "FLOOR")
                .add("isHidden", false)
                .add("x", 5)
                .add("y", 5)
                .add("z", 5)
                .build();

        String name = given()
            .when()
                .contentType("application/json")
                .body(jsonObject.toString())
                .post("/node")
            .then()
                .statusCode(200)
                .body("name", is("4ahitm"))
                .extract()
                .jsonPath()
                .getString("name");

        deleteNode(name);
    }

    @Test
    void testCreateNodeWithTypeStairsAsText() {
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("name", "4ahitm")
                .add("type", "STAIRS")
                .add("isHidden", false)
                .add("x", 5)
                .add("y", 5)
                .add("z", 5)
                .build();

        given()
            .when()
                .contentType("application/json")
                .body(jsonObject.toString())
                .post("/node")
            .then()
                .statusCode(200)
                .body("name", is("4ahitm"));

        deleteNode("4ahitm");
    }

    @Test
    void testCreateNodeWithTypeElevatorAsText() {
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("name", "4ahitm")
                .add("type", "ELEVATOR")
                .add("isHidden", false)
                .add("x", 5)
                .add("y", 5)
                .add("z", 5)
                .build();

        given()
            .when()
                .contentType("application/json")
                .body(jsonObject.toString())
                .post("/node")
            .then()
                .statusCode(200);

        deleteNode("4ahitm");
    }

    @Test
    void testCreateNodeWithoutHidden() {
        Node node = new Node(null, "4ahitm", NodeType.FLOOR, null, 125f, 25f, 890f);
        given()
            .when()
                .contentType("application/json")
                .body(jsonb.toJson(node))
                .post("/node")
            .then()
                .statusCode(400)
                .body("failedFields[0].message", is("Hidden may not be null!"));
    }

    @Test
    void testCreateNodeWithoutX() {
        Node node = new Node(null, "4ahitm", NodeType.FLOOR, false, null, 25f, 890f);
        given()
            .when()
                .contentType("application/json")
                .body(jsonb.toJson(node))
                .post("/node")
            .then()
                .statusCode(400)
                .body("failedFields[0].message", is("X may not be null!"));
    }

    @Test
    void testCreateNodeWithoutY() {
        Node node = new Node(null, "4ahitm", NodeType.FLOOR, false, 125f, null, 890f);
        given()
            .when()
                .contentType("application/json")
                .body(jsonb.toJson(node))
                .post("/node")
            .then()
                .statusCode(400)
                .body("failedFields[0].message", is("Y may not be null!"));
    }

    @Test
    void testCreateNodeWithoutZ() {
        Node node = new Node(null, "4ahitm", NodeType.FLOOR, false, 125f, 25f, null);
        given()
            .when()
                .contentType("application/json")
                .body(jsonb.toJson(node))
                .post("/node")
            .then()
                .statusCode(400)
                .body("failedFields[0].message", is("Z may not be null!"));
    }

    @Test
    void testGetNode() {
        Node node = new Node(null, "4ahitm", NodeType.FLOOR, false, 125f, 25f, 890f);
        given()
            .when()
                .contentType("application/json")
                .body(jsonb.toJson(node))
                .post("/node")
            .then()
                .statusCode(200)
                .body("name", is(node.getName()));

        given()
            .when()
                .pathParam("name", node.getName())
                .get("/node/{name}")
            .then()
                .statusCode(200);

        deleteNode(node.getName());
    }

    @Test
    void testGetNotExistingNode() {
        given()
            .when()
                .pathParam("name", "69ahitm")
                .get("/node/{name}")
            .then()
                .statusCode(404);
    }

    @Test
    void testUpdateNodeName() {
        Node node = new Node(null, "4ahitm", NodeType.FLOOR, false, 125f, 25f, 890f);
        int nodeId = createNode(node);

        Node nodeUpdated = new Node(null, "5ahitm", NodeType.FLOOR, false, 125f, 25f, 890f);

        given()
            .when()
                .contentType("application/json")
                .body(jsonb.toJson(nodeUpdated))
                .pathParam("name", node.getName())
                .put("/node/{name}")
            .then()
                .statusCode(200)
                .body("name", is("5ahitm"))
                .body("id", is(nodeId));

        deleteNode(nodeUpdated.getName());
    }

    @Test
    void testUpdateNotExistingName() {
        given()
            .when()
                .contentType("application/json")
                .pathParam("name", "69ahitm")
                .put("/node/{name}")
            .then()
                .statusCode(404);
    }

    int createNode(Node node) {
        return given()
            .when()
                .contentType("application/json")
                .body(jsonb.toJson(node))
                .post("/node")
            .then()
                .statusCode(200)
                .body("name", is(node.getName()))
                .extract()
                    .jsonPath()
                    .getInt("id");
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
