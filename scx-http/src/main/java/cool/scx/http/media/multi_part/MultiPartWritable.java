package cool.scx.http.media.multi_part;

public interface MultiPartWritable extends MultiPart {
    
    MultiPartWritable boundary(String boundary);
    
    MultiPartWritable add(MultiPartPart part);
    
}
