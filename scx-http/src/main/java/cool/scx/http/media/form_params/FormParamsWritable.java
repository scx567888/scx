package cool.scx.http.media.form_params;

import cool.scx.http.parameters.ParametersWritable;

/// FormParams
///
/// @author scx567888
/// @version 0.0.1
public interface FormParamsWritable extends FormParams, ParametersWritable<String, String> {
    
    @Override
    FormParamsWritable clear();

    @Override
    FormParamsWritable remove(String name);

    @Override
    FormParamsWritable add(String name, String... value);

    @Override
    FormParamsWritable set(String name, String... value);
    
}
