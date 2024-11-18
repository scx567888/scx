package cool.scx.socket;

final class ScxSocketStatus {

    final FrameCreator frameCreator;
    final DuplicateFrameChecker duplicateFrameChecker;
    final FrameSender frameSender;
    final RequestManager requestManager;

    public ScxSocketStatus(ScxSocketOptions options) {
        this.frameCreator = new FrameCreator();
        this.frameSender = new FrameSender(options);
        this.duplicateFrameChecker = new DuplicateFrameChecker(options);
        this.requestManager = new RequestManager(options);
    }

}
