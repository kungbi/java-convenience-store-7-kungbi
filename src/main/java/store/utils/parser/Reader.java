package store.utils.parser;

import java.io.IOException;
import java.util.List;

public interface Reader {
    List<String> readLine() throws IOException;

    void close() throws IOException;
}
