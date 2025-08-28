package com.example.eduguide;

public class Edge {
    private final String toVertex;
    private final String relation;


    public Edge(String toVertex, String relation) {
        this.toVertex = toVertex;
        this.relation = relation;
    }

    public String getToVertex() {
        return toVertex;
    }

    public String getRelation() {
        return relation;
    }

    @Override
    public String toString() {
        return "Edge{toVertex='" + toVertex + "', relation='" + relation + "'}";
    }
}
