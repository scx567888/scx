package cool.scx.exception;

import cool.scx.vo.VoHelper;

/**
 * a
 */
public record ScxHttpExceptionViewWrapper(int httpCode, String title, String info) {

    public String toHtml() {
        var htmlStr = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>%s</title>
                </head>
                <body>
                    <h1>%s - %s</h1>
                    <pre>%s</pre>
                </body>
                </html>
                """;
        return String.format(htmlStr, title, httpCode, title, info);
    }

    public String toJson() {
        return VoHelper.toJson(this, "");
    }

}
