package info.mvc.experience.utility;

import info.mvc.experience.model.CsvRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ComputationTest {
    private static final Logger logger = LoggerFactory.getLogger(ComputationTest.class);

    static final String LOAD_CSV_FILENAME = "loadtrans.csv";
    static final String COMPARE_KEY = "TransactionID";

    public static void main(String[] args) throws Exception {
        //TEST getResourceAsStream
//        String fileName = "csv/monitor.csv";
//        InputStream inputStreamResource = ComputationTest.class.getClassLoader().
//                getResourceAsStream(fileName);
//
//        if (inputStreamResource == null)
//            throw new IllegalArgumentException("file not found! " + fileName);
//
//        try (InputStreamReader streamReader =
//                     new InputStreamReader(inputStreamResource, StandardCharsets.UTF_8);
//             BufferedReader reader = new BufferedReader(streamReader)) {
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                logger.info(line);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        //Testing with local file
        File file = Paths.get(LOAD_CSV_FILENAME).toFile();
        CsvParserSimple obj = new CsvParserSimple();
        //Read file
        List<String[]> result = obj.readFile(file);

        Map<String, Integer> headerMap = new LinkedHashMap<>();
        List<CsvRecord> csvRecordList = new ArrayList<>();
        int rowIndex = 0;
        for (String[] arrays : result) {

            if (rowIndex == 0) { //READING for header only
                for (int i = 0; i < arrays.length; i++) {
                    headerMap.put(arrays[i], i);
                }
            }

            CsvRecord csvRecord = new CsvRecord(rowIndex, headerMap, Arrays.asList(arrays));
            csvRecordList.add(csvRecord);
//            System.out.println("\nString[" + rowIndex + "] : " + Arrays.toString(arrays));
//
//            int index = 0;
//            for (String array : arrays) {
//                System.out.println(index++ + " : " + array);
//            }
            //INCREMENT to next row record
            rowIndex++;

        }
        System.out.println("===============================");
        System.out.println("===============================");
//        for (CsvRecord csvRecord : csvRecordList) {
//            csvRecord.getDataByHeader();
//            logger.info("Row:{}", csvRecord.getRowNumber());
//            csvRecord.convertToTransaction().getValuesHolder().forEach((k,v)->logger.info("{}={}", k,v));
//            csvRecord.getTransaction().forEach((k,v)->logger.info("{}={}", k,v));
//        }
        System.out.println("==============================================================");

        Map<String,CsvRecord> csvRecordMap = csvRecordList
                .stream()
                .filter(csvRecord -> csvRecord.getRowNumber() != 0)
//                .collect(Collectors.toList())
                .collect(Collectors.toMap((map) ->
                        map.getTransaction().get(COMPARE_KEY), map -> map));
//        collect.forEach(c->logger.info("Filtered Row={}",c.getRowNumber()));

        csvRecordMap.forEach((k,v)->{
            logger.info("{}={}, CsvRecord={}",COMPARE_KEY, k, v.getTransaction());
        });
    }
}
