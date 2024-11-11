package store.utils.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import store.exception.ReaderException;
import store.exception.message.ReaderExceptionMessage;

public class CsvReader implements Reader {
    private final BufferedReader reader;

    public CsvReader(BufferedReader reader, boolean header) {
        if (reader == null) {
            throw new ReaderException(ReaderExceptionMessage.BUFFERED_READER_NULL);
        }
        this.reader = reader;
        if (header) {
            try {
                this.reader.readLine();
            } catch (IOException error) {
                throw new ReaderException(ReaderExceptionMessage.READ_LINE_FAILED, error);
            }
        }
    }

    @Override
    public List<String> readLine() throws IOException {
        String line = this.reader.readLine();
        if (line == null) {
            return null;
        }

        return List.of(line.split(","));
    }

    @Override
    public void close() throws IOException {
        this.reader.close();
    }
}
