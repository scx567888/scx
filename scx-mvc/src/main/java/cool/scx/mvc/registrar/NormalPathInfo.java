package cool.scx.mvc.registrar;

import cool.scx.enumeration.HttpMethod;

public record NormalPathInfo(HttpMethod httpMethod, String key) {

}