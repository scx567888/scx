package cool.scx.logging.recorder;

import cool.scx.logging.ScxLogRecord;
import cool.scx.logging.ScxLogRecorder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import static java.nio.file.StandardOpenOption.*;

/**
 * 文件记录器
 */
public class FileRecorder extends ScxLogRecorder {

    /**
     * Constant <code>LOG_FILE_NAME</code>
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Path storedDirectory;

    public FileRecorder(Path storedDirectory) {
        this.storedDirectory = storedDirectory;
    }

    @Override
    public void record(ScxLogRecord logRecord) {
        var directory = storedDirectory;
        if (directory == null) {
            return;
        }
        var data = formatter().format(logRecord);
        var logFileName = getLogFileName(logRecord.timeStamp());
        var path = directory.resolve(logFileName);
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, data, APPEND, CREATE, SYNC, WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String name() {
        return "FILE";
    }

    public String getLogFileName(TemporalAccessor temporal) {
        return DATE_TIME_FORMATTER.format(temporal) + ".log";
    }

}
