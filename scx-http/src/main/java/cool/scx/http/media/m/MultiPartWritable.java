package cool.scx.http.media.m;

public interface MultiPartWritable extends MultiPart {
    
    MultiPartWritable boundary(String boundary);
    
    MultiPartWritable add(MultiPartPart part);
    
}
