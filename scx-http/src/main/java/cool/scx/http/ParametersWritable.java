package cool.scx.http;

public interface ParametersWritable extends Parameters{
    
    ParametersWritable set(String name, String... value);

    ParametersWritable add(String name, String... value);
    
    ParametersWritable remove(String name);
    
}
