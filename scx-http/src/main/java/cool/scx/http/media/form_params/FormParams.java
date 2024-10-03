package cool.scx.http.media.form_params;

import cool.scx.http.ParametersImpl;

public class FormParams extends ParametersImpl<String, String> {

    @Override
    public FormParams set(String name, String... value) {
        return (FormParams) super.set(name, value);
    }

    @Override
    public FormParams add(String name, String... value) {
        return (FormParams) super.add(name, value);
    }

    @Override
    public FormParams remove(String name) {
        return (FormParams) super.remove(name);
    }

    @Override
    public FormParams clear() {
        return (FormParams) super.clear();
    }

}
