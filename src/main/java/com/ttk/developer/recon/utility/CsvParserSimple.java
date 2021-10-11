package com.ttk.developer.recon.utility;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

/**
 * https://tools.ietf.org/html/rfc4180
 * <p>
 * Fields containing line breaks (CRLF), double quotes, and commas
 * should be enclosed in double-quotes.  For example:
 * <p>
 * "aaa","b CRLF
 * bb","ccc" CRLF
 * zzz,yyy,xxx
 * <p>
 * If double-quotes are used to enclose fields, then a double-quote
 * appearing inside a field must be escaped by preceding it with
 * another double quote.  For example:
 * <p>
 * "aaa","b""bb","ccc"
 *
 * @kaung : copy from https://mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
 */

public class CsvParserSimple {

    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DOUBLE_QUOTES = '"';
    private static final char DEFAULT_QUOTE_CHAR = DOUBLE_QUOTES;
    private static final String NEW_LINE = "\n";

    private boolean isMultiLine = false;
    private String pendingField = "";
    private String[] pendingFieldLine = new String[]{};

    /**
     * Read Headers Row only (must be first row in csv)
     * LATER to compare , validate against pre-defined HEADERS
     * Must match ...
     * @param csvFile
     * @return
     * @throws Exception
     */

    public List<String[]> readHeadersOnlyMultipartFile(MultipartFile csvFile) throws Exception {
        InputStream inputStream = csvFile.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        List<String[]> headersResult = new ArrayList<>();
        int indexLine = 1;
        try (BufferedReader br = new BufferedReader(inputStreamReader)) {
            String line;
            while ((line = br.readLine()) != null) {

                if (indexLine>1) break;

                String[] csvLineInArray = parseLine(line);

                headersResult.add(csvLineInArray);

                indexLine++;
            }
        }

        return headersResult;
    }

    /**
     * Read CSV file from Start Row (including HEADERS)
     *
     * @param csvFile from Request ;;;
     * @return
     * @throws Exception
     */
    public List<String[]> readAsMultipartFile(MultipartFile csvFile) throws Exception {
        return readAsMultipartFile(csvFile, 0);
    }

    public List<String[]> readAsMultipartFile(MultipartFile csvFile, int skipLine) throws Exception {
        InputStream inputStream = csvFile.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        List<String[]> result = new ArrayList<>();
        int indexLine = 1;

        try (BufferedReader br = new BufferedReader(inputStreamReader)) {

            String line;
            while ((line = br.readLine()) != null) {

                if (indexLine++ <= skipLine) {
                    continue;
                }

                String[] csvLineInArray = parseLine(line);

                if (isMultiLine) {
                    pendingFieldLine = joinArrays(pendingFieldLine, csvLineInArray);
                } else {

                    if (pendingFieldLine != null && pendingFieldLine.length > 0) {
                        // joins all fields and add to list
                        result.add(joinArrays(pendingFieldLine, csvLineInArray));
                        pendingFieldLine = new String[]{};
                    } else {
                        // if dun want to support multiline, only this line is required.
                        result.add(csvLineInArray);
                    }

                }

            }
        }

        return result;
    }
    //////////////////////////////////////// end of Multipart file reading ///////////////////////////

//    public List<String[]> readFile(File csvFile) throws Exception {
//        return readFile(csvFile, 0);
//    }
//
//    public List<String[]> readFile(File csvFile, int skipLine) throws Exception {
//
//        List<String[]> result = new ArrayList<>();
//        int indexLine = 1;
//
//        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
//
//            String line;
//            while ((line = br.readLine()) != null) {
//
//                if (indexLine++ <= skipLine) {
//                    continue;
//                }
//
//                String[] csvLineInArray = parseLine(line);
//
//                if (isMultiLine) {
//                    pendingFieldLine = joinArrays(pendingFieldLine, csvLineInArray);
//                } else {
//
//                    if (pendingFieldLine != null && pendingFieldLine.length > 0) {
//                        // joins all fields and add to list
//                        result.add(joinArrays(pendingFieldLine, csvLineInArray));
//                        pendingFieldLine = new String[]{};
//                    } else {
//                        // if dun want to support multiline, only this line is required.
//                        result.add(csvLineInArray);
//                    }
//
//                }
//
//            }
//        }
//
//        return result;
//    }

    public String[] parseLine(String line) throws Exception {
        return parseLine(line, DEFAULT_SEPARATOR);
    }

    public String[] parseLine(String line, char separator) throws Exception {
        return parse(line, separator, DEFAULT_QUOTE_CHAR).toArray(String[]::new);
    }

    private List<String> parse(String line, char separator, char quoteChar) throws Exception {

        List<String> result = new ArrayList<>();

        boolean inQuotes = false;
        boolean isFieldWithEmbeddedDoubleQuotes = false;

        StringBuilder field = new StringBuilder();

        for (char c : line.toCharArray()) {

            if (c == DOUBLE_QUOTES) {               // handle embedded double quotes ""
                if (isFieldWithEmbeddedDoubleQuotes) {

                    if (field.length() > 0) {       // handle for empty field like "",""
                        field.append(DOUBLE_QUOTES);
                        isFieldWithEmbeddedDoubleQuotes = false;
                    }

                } else {
                    isFieldWithEmbeddedDoubleQuotes = true;
                }
            } else {
                isFieldWithEmbeddedDoubleQuotes = false;
            }

            if (isMultiLine) {                      // multiline, add pending from the previous field
                field.append(pendingField).append(NEW_LINE);
                pendingField = "";
                inQuotes = true;
                isMultiLine = false;
            }

            if (c == quoteChar) {
                inQuotes = !inQuotes;
            } else {
                if (c == separator && !inQuotes) {  // if find separator and not in quotes, add field to the list
                    result.add(field.toString());
                    field.setLength(0);             // empty the field and ready for the next
                } else {
                    field.append(c);                // else append the char into a field
                }
            }

        }

        //line done, what to do next?
        if (inQuotes) {
            pendingField = field.toString();        // multiline
            isMultiLine = true;
        } else {
            result.add(field.toString());           // this is the last field
        }

        return result;

    }

    private String[] joinArrays(String[] array1, String[] array2) {
        return Stream.concat(Arrays.stream(array1), Arrays.stream(array2))
                .toArray(String[]::new);
    }
}
