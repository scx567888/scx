package cool.scx.logging.appender;

import cool.scx.logging.ScxLogEvent;
import cool.scx.logging.ScxLogRecorder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.nio.file.StandardOpenOption.*;

/**
 * 文件记录器
 */
public class FileRecorder implements ScxLogRecorder {

    /**
     * Constant <code>LOG_FILE_NAME</code>
     */
    private static final DateTimeFormatter LOG_FILE_NAME = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Path storedDirectory;

    public FileRecorder(Path storedDirectory) {
        this.storedDirectory = storedDirectory;
    }

    @Override
    public void record(ScxLogEvent event) {
        var directory = storedDirectory;
        if (directory == null) {
            return;
        }
        var data = getLayout().format(event);
        var logFileName = getLogFileName(event.timeStamp());
        var path = directory.resolve(logFileName);
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, data, APPEND, CREATE, SYNC, WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "FILE";
    }

    public String getLogFileName(LocalDateTime time) {
        return LOG_FILE_NAME.format(time) + ".log";
    }

}
