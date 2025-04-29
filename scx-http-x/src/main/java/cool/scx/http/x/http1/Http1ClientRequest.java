package cool.scx.http.x.http1;

import cool.scx.http.ScxHttpClientRequest;
import cool.scx.http.x.http1.request_line.RequestTargetForm;

public interface Http1ClientRequest extends ScxHttpClientRequest {

    RequestTargetForm requestTargetForm();

    Http1ClientRequest requestTargetForm(RequestTargetForm requestTargetForm);

}
