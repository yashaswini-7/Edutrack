package edutrack.core;

public class TextMatch {
    public final int lineNumber;     
    public final int columnNumber;   
    public final int absoluteOffset; 
    public final String pattern;

    public TextMatch(int lineNumber, int columnNumber, int absoluteOffset, String pattern) {
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
        this.absoluteOffset = absoluteOffset;
        this.pattern = pattern;
    }

    @Override
    public String toString() {
        return "[Line: " + lineNumber + ", Col: " + columnNumber + ", Absolute Offset: " + absoluteOffset + "] -> \"" + pattern + "\"";
    }
}
