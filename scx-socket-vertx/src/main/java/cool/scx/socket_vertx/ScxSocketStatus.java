package cool.scx.socket_vertx;

import cool.scx.socket.FrameCreator;

final class ScxSocketStatus {

    final FrameCreator frameCreator;
    final DuplicateFrameChecker duplicateFrameChecker;
    final FrameSender frameSender;
    final RequestManager requestManager;

    public ScxSocketStatus(ScxSocketOptions options) {
        this.frameCreator = new FrameCreator();
        this.frameSender = new FrameSender();
        this.duplicateFrameChecker = new DuplicateFrameChecker(options.getDuplicateFrameCheckerClearTimeout());
        this.requestManager = new RequestManager();
    }

}
