package store.utils.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

class CsvReaderTest {

    @Test
    void 한_줄_읽기_테스트() throws IOException {
        // given
        String line = "1,2,3,4,5\n5,4,3,2,1";
        CsvReader csvReader = new CsvReader(new BufferedReader(new CharArrayReader(line.toCharArray())), false);

        // when
        List<String> firstLine = csvReader.readLine();
        List<String> secondLine = csvReader.readLine();

        // then
        assertEquals(5, firstLine.size());
        assertEquals("1", firstLine.get(0));
        assertEquals("2", firstLine.get(1));
        assertEquals("3", firstLine.get(2));
        assertEquals("4", firstLine.get(3));
        assertEquals("5", firstLine.get(4));

        assertEquals(5, secondLine.size());
        assertEquals("5", secondLine.get(0));
        assertEquals("4", secondLine.get(1));
        assertEquals("3", secondLine.get(2));
        assertEquals("2", secondLine.get(3));
        assertEquals("1", secondLine.get(4));

    }

}