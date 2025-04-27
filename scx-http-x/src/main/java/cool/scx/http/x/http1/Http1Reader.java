package cool.scx.http.x.http1;

import cool.scx.http.exception.BadRequestException;
import cool.scx.http.exception.ContentTooLargeException;
import cool.scx.http.exception.URITooLongException;
import cool.scx.http.x.http1.chunked.FixedLengthDataSupplier;
import cool.scx.http.x.http1.chunked.HttpChunkedDataSupplier;
import cool.scx.http.x.http1.exception.HttpVersionNotSupportedException;
import cool.scx.http.x.http1.exception.RequestHeaderFieldsTooLargeException;
import cool.scx.http.x.http1.headers.Http1Headers;
import cool.scx.http.x.http1.request_line.Http1RequestLine;
import cool.scx.http.x.http1.request_line.InvalidHttpRequestLineException;
import cool.scx.http.x.http1.request_line.InvalidHttpVersion;
import cool.scx.http.x.http1.status_line.Http1StatusLine;
import cool.scx.http.x.http1.status_line.InvalidHttpStatusException;
import cool.scx.http.x.http1.status_line.InvalidHttpStatusLineException;
import cool.scx.io.data_reader.DataReader;
import cool.scx.io.exception.NoMatchFoundException;
import cool.scx.io.exception.NoMoreDataException;
import cool.scx.io.io_stream.DataReaderInputStream;

import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Arrays;

import static cool.scx.http.headers.ScxHttpHeadersHelper.parseHeaders;
import static cool.scx.http.x.http1.Http1Helper.CRLF_BYTES;
import static cool.scx.http.x.http1.Http1Helper.CRLF_CRLF_BYTES;
import static cool.scx.http.x.http1.headers.transfer_encoding.TransferEncoding.CHUNKED;
import static java.nio.charset.StandardCharsets.UTF_8;

/// 读取 HTTP/1.1 请求或响应内容工具类
final class Http1Reader {

    public static InputStream readBodyInputStream(Http1Headers headers, DataReader dataReader, long maxPayloadSize) {
        // HTTP/1.1 本质上只有两种请求体格式 1, 分块传输 2, 指定长度 (当然也可以没有长度 那就表示没有请求体)

        //1, 因为 分块传输的优先级高于 contentLength 所以先判断是否为分块传输
        var transferEncoding = headers.transferEncoding();
        if (transferEncoding == CHUNKED) {
            return new DataReaderInputStream(new HttpChunkedDataSupplier(dataReader, maxPayloadSize));
        }

        //2, 判断请求体是不是有 指定长度
        var contentLength = headers.contentLength();
        if (contentLength != null) {
            // 请求体长度过大 这里抛出异常
            if (contentLength > maxPayloadSize) {
                throw new ContentTooLargeException();
            }
            return new DataReaderInputStream(new FixedLengthDataSupplier(dataReader,contentLength));
        }

        //3, 没有长度的空请求体
        return InputStream.nullInputStream();
    }

    public static Http1Headers readHeaders(DataReader dataReader, int maxHeaderSize) {
        //尝试读取 headers
        try {
            // 1, 尝试检查空头的情况 , 即请求行后紧跟 \r\n
            var b = dataReader.peek(2);
            if (Arrays.equals(b, CRLF_BYTES)) {
                dataReader.skip(2);
                return new Http1Headers();
            }

            // 2, 尝试正常读取 , 读取到 第一个 \r\n\r\n 为止
            var headerBytes = dataReader.readUntil(CRLF_CRLF_BYTES, maxHeaderSize);
            var headerStr = new String(headerBytes, UTF_8);
            return parseHeaders(new Http1Headers(), headerStr, true); //使用严格模式解析
        } catch (NoMoreDataException | UncheckedIOException e) {
            // Socket 关闭了 或者底层 Socket 发生异常
            throw new CloseConnectionException();
        } catch (NoMatchFoundException e) {
            // 在指定长度内未匹配到 这里抛出请求头过大异常
            throw new RequestHeaderFieldsTooLargeException(e.getMessage());
        }
    }

    public static Http1RequestLine readRequestLine(DataReader dataReader, int maxRequestLineSize) {
        //尝试读取 请求行
        try {
            // 1, 尝试读取到 第一个 \r\n 为止
            var requestLineBytes = dataReader.readUntil(CRLF_BYTES, maxRequestLineSize);
            var requestLineStr = new String(requestLineBytes, UTF_8);
            return Http1RequestLine.of(requestLineStr);
        } catch (NoMoreDataException | UncheckedIOException e) {
            // Socket 关闭了 或者底层 Socket 发生异常
            throw new CloseConnectionException();
        } catch (NoMatchFoundException e) {
            // 在指定长度内未匹配到 这里抛出 URI 过长异常
            throw new URITooLongException(e.getMessage());
        } catch (InvalidHttpRequestLineException e) {
            // 解析 RequestLine 异常
            throw new BadRequestException("Invalid HTTP request line : " + e.requestLineStr);
        } catch (InvalidHttpVersion e) {
            // 错误的 Http 版本异常
            throw new HttpVersionNotSupportedException("Invalid HTTP version : " + e.versionStr);
        }
    }

    public static Http1StatusLine readStatusLine(DataReader dataReader, int maxStatusLineSize) {
        try {
            var statusLineBytes = dataReader.readUntil(CRLF_BYTES, maxStatusLineSize);
            var statusLineStr = new String(statusLineBytes);
            return Http1StatusLine.of(statusLineStr);
        } catch (NoMoreDataException e) {
            throw new CloseConnectionException();
        } catch (NoMatchFoundException e) {
            // 在指定长度内未匹配到 这里抛出响应行过大异常, 包装到 RuntimeException 中 因为这其中的异常一般都会由用户来处理 
            throw new RuntimeException("响应行过大 !!!");
        } catch (InvalidHttpStatusLineException | InvalidHttpStatusException | InvalidHttpVersion e) {
            // 解析异常我们全部包装到 RuntimeException 中
            throw new RuntimeException(e);
        }
    }

}
