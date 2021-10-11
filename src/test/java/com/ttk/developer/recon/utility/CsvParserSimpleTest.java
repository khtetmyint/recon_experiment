package com.ttk.developer.recon.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CsvParserSimpleTest {

    private CsvParserSimple csvParserSimple;


    @BeforeEach
    void setUp() {
        csvParserSimple = new CsvParserSimple();
    }


    //MockMultipartFile csvFile = new MockMultipartFile("foo,bar,baz", "foo.csv", MediaType.TEXT_PLAIN_VALUE, "some_val".getBytes());


    @Test
    void testThrowException() {
        Throwable exception =
        assertThrows(Exception.class, ()-> csvParserSimple.readAsMultipartFile(null));
        //System.out.println(exception.getMessage());
        assertEquals(null, exception.getMessage());
    }



    /**
     * Take Reference from
     * https://mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
     * @param line
     * @throws Exception
     */

    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = {
            "\"aa\",\"bb\",\"cc\",\"dd\",\"ee\"",
            "aa,bb,cc,dd,ee",
            "aa,bb,\"cc\",dd,ee"})
    void test_csv_line_default(String line) throws Exception {
        String[] result = csvParserSimple.parseLine(line);
        assertEquals(5, result.length);
        assertEquals("aa", result[0]);
        assertEquals("bb", result[1]);
        assertEquals("cc", result[2]);
        assertEquals("dd", result[3]);
        assertEquals("ee", result[4]);
    }

    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = {
            "\"aa\",\"\",\"\",\"\",\"\"",
            "aa,,,,",
            "aa,,\"\",,"})
    void test_csv_line_empty(String line) throws Exception {
        String[] result = csvParserSimple.parseLine(line);
        assertEquals(5, result.length);
        assertEquals("aa", result[0]);
        assertEquals("", result[1]);
        assertEquals("", result[2]);
        assertEquals("", result[3]);
        assertEquals("", result[4]);
    }



    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = {
            "\"aa\";\"bb\";\"cc\";\"dd\";\"ee\"",
            "aa;bb;cc;dd;ee",
            "aa;bb;\"cc\";dd;ee"
    }
    )
    void test_csv_line_custom_separator(String line) throws Exception {
        String[] result = csvParserSimple.parseLine(line, ';');
        assertEquals(5, result.length);
        assertEquals("aa", result[0]);
        assertEquals("bb", result[1]);
        assertEquals("cc", result[2]);
        assertEquals("dd", result[3]);
        assertEquals("ee", result[4]);
    }

    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = {
            "\"Australia\",\"11 Lakkari Cl, Taree, NSW 2430\"",
            "Australia,\"11 Lakkari Cl, Taree, NSW 2430\""
    }
    )
    void test_csv_line_contain_comma_in_field(String line) throws Exception {

        String[] result = csvParserSimple.parseLine(line);

        assertEquals(2, result.length);
        assertEquals("Australia", result[0]);
        assertEquals("11 Lakkari Cl, Taree, NSW 2430", result[1]);

    }

    @Test
    void test_csv_line_contain_double_quotes_in_field() throws Exception {

        String line = "\"Australia\",\"51 Maritime Avenue, \"\"Western Australia\"\", 6286\"";
        String[] result = csvParserSimple.parseLine(line);

        assertEquals(2, result.length);
        assertEquals("Australia", result[0]);
        assertEquals("51 Maritime Avenue, \"Western Australia\", 6286", result[1]);

    }

    @Test
    void test_csv_line_contain_double_quotes_in_field_2() throws Exception {

        String line = "Yangon,Welcome to \"\"South Myanmar\"\"";
        String[] result = csvParserSimple.parseLine(line);

        assertEquals(2, result.length);
        assertEquals("Yangon", result[0]);
        assertEquals("Welcome to \"South Myanmar\"", result[1]);

    }
}