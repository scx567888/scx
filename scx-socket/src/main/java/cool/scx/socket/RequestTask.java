package cool.scx.socket;

import cool.scx.common.util.$.Timeout;

import java.util.function.Consumer;

import static cool.scx.common.util.$.setTimeout;

final class RequestTask {

    private final Consumer<ScxSocketResponse> responseCallback;
    private final RequestManager requestManager;
    private final RequestOptions options;
    private final long seq_id;
    private Timeout failTimeout;

    public RequestTask(Consumer<ScxSocketResponse> responseCallback, RequestManager requestManager, RequestOptions options, long seqId) {
        this.responseCallback = responseCallback;
        this.requestManager = requestManager;
        this.options = options;
        this.seq_id = seqId;
    }

    public void start() {
        cancelFail();
        this.failTimeout = setTimeout(this::fail, options.getRequestTimeout());
    }

    public void success(String payload) {
        this.clear();
        this.responseCallback.accept(new ScxSocketResponse(payload));
    }

    public void fail() {
        this.clear();
        this.responseCallback.accept(new ScxSocketResponse(new RuntimeException("超时")));
    }

    public void cancelFail() {
        if (this.failTimeout != null) {
            this.failTimeout.cancel();
            this.failTimeout = null;
        }
    }

    public void clear() {
        this.cancelFail();
        this.requestManager.responseTaskMap.remove(seq_id);
    }

}
