package cool.scx.http.media.form_params;

import cool.scx.http.parameters.ParametersImpl;

/// FormParams
///
/// @author scx567888
/// @version 0.0.1
public class FormParamsImpl extends ParametersImpl<String, String> implements FormParamsWritable {

    public FormParamsImpl() {

    }

    public FormParamsImpl(FormParams oldFormParams) {
        super(oldFormParams);
    }

    @Override
    public FormParamsImpl set(String name, String... value) {
        return (FormParamsImpl) super.set(name, value);
    }

    @Override
    public FormParamsImpl add(String name, String... value) {
        return (FormParamsImpl) super.add(name, value);
    }

    @Override
    public FormParamsImpl remove(String name) {
        return (FormParamsImpl) super.remove(name);
    }

    @Override
    public FormParamsImpl clear() {
        return (FormParamsImpl) super.clear();
    }

}
