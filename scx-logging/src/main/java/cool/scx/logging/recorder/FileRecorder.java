package cool.scx.logging.recorder;

import cool.scx.logging.ScxLogRecord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import static java.nio.file.StandardOpenOption.*;

/// 文件记录器
///
/// @author scx567888
/// @version 0.0.1
public class FileRecorder extends AbstractRecorder {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Path storedDirectory;

    public FileRecorder(Path storedDirectory) {
        this.storedDirectory = storedDirectory;
    }

    public static void writeToFile(Path path, String data) {
        try {
            Files.writeString(path, data, APPEND, CREATE, SYNC, WRITE);
        } catch (NoSuchFileException e) {
            try {
                Files.createDirectories(path.getParent());
                Files.writeString(path, data, APPEND, CREATE, SYNC, WRITE);
            } catch (IOException ignored1) {

            }
        } catch (Exception ignored) {

        }
    }

    public String getLogFileName(TemporalAccessor temporal) {
        return DATE_TIME_FORMATTER.format(temporal) + ".log";
    }

    @Override
    public void record0(ScxLogRecord logRecord) {
        var directory = storedDirectory;
        if (directory == null) {
            return;
        }
        var data = format(logRecord);
        var logFileName = getLogFileName(logRecord.timeStamp());
        var path = directory.resolve(logFileName);
        writeToFile(path, data);
    }

}
