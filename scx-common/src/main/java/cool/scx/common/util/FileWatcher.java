package cool.scx.common.util;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * 文件监听器 (只监听单个文件)
 */
public final class FileWatcher {

    private final WatchService watchService;
    private final Path fileName;
    private Thread watchThread;
    private Runnable deleteHandler;
    private Runnable createHandler;
    private Runnable modifyHandler;

    public FileWatcher(Path path) throws IOException {
        this.watchService = FileSystems.getDefault().newWatchService();
        this.fileName = path.getFileName();
        var fileParent = path.getParent();
        fileParent.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
    }

    public void _do() {
        while (true) {
            WatchKey watchKey;
            try {
                watchKey = watchService.take();
            } catch (InterruptedException e) {
                //中断则退出
                break;
            }
            var watchEvents = watchKey.pollEvents();
            for (var event : watchEvents) {
                Path context = (Path) event.context();
                //只处理指定文件
                if (this.fileName.equals(context)) {
                    var kind = event.kind();
                    if (kind == ENTRY_CREATE) {
                        _callOnCreate();
                    } else if (kind == ENTRY_MODIFY) {
                        _callOnModify();
                    } else if (kind == ENTRY_DELETE) {
                        _callOnDelete();
                    }
                }
            }
            // reset the key
            var valid = watchKey.reset();
            if (!valid) {
                break;
            }
        }
    }

    public FileWatcher onCreate(Runnable createHandler) {
        this.createHandler = createHandler;
        return this;
    }

    public FileWatcher onModify(Runnable modifyHandler) {
        this.modifyHandler = modifyHandler;
        return this;
    }

    public FileWatcher onDelete(Runnable deleteHandler) {
        this.deleteHandler = deleteHandler;
        return this;
    }

    private void _callOnCreate() {
        if (this.createHandler != null) {
            this.createHandler.run();
        }
    }

    private void _callOnModify() {
        if (this.modifyHandler != null) {
            this.modifyHandler.run();
        }
    }

    private void _callOnDelete() {
        if (this.deleteHandler != null) {
            this.deleteHandler.run();
        }
    }

    public synchronized void start() {
        this.watchThread = Thread.ofVirtual().start(this::_do);
    }

    public synchronized void stop() {
        if (this.watchThread != null) {
            this.watchThread.interrupt();
            this.watchThread = null;
        }
    }

}
