package at.htl.indoornav.entity;

import at.htl.indoornav.validator.UniqueName;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Node {

    private Long id;
    @NotBlank(message = "Name may not be blank!")
    @UniqueName(message = "Name is already in use!")
    private String name;
    @NotNull(message = "Type may not be null!")
    private NodeType type;
    @NotNull(message = "Hidden may not be null!")
    private Boolean isHidden;
    @NotNull(message = "X may not be null!")
    private Float x;
    @NotNull(message = "Y may not be null!")
    private Float y;
    @NotNull(message = "Z may not be null!")
    private Float z;

    public Node(Long id, String name, NodeType type, Boolean isHidden, Float x, Float y, Float z) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.isHidden = isHidden;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Node() {
    }

    public static Node from(org.neo4j.driver.types.Node node) {
        return new Node(
                node.id(), node.get("name").asString(), NodeType.valueOf(node.get("type").asString()),
                node.get("isHidden").asBoolean(), node.get("x").asFloat(), node.get("y").asFloat(), node.get("z").asFloat()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(String type) {
        NodeType nodeType = null;
        NodeType[] allTypes = NodeType.values();
        for (NodeType singleType : allTypes) {
            if (singleType.name().equals(type)) {
                nodeType = singleType;
                break;
            }
        }

        this.type = nodeType;
    }

    public Boolean isHidden() {
        return isHidden;
    }

    public void setHidden(Boolean isHidden) {
        this.isHidden = isHidden;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getZ() {
        return z;
    }

    public void setZ(Float z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", isHidden=" + isHidden +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
