package at.htl.indoornav.entity;

public class Node {

    private Long id;
    private String name;
    private NodeType type;
    private boolean isHidden;
    private float x;
    private float y;
    private float z;

    public Node(Long id, String name, NodeType type, boolean isHidden, float x, float y, float z) {
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

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
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
