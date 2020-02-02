package at.htl.indoornav.service;

public class Path {
    private String start;
    private String end;

    public Path(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public Path() {
    }

    public boolean isValid() {
        return start != null && end != null;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
