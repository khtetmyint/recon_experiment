package com.ttk.developer.recon.service.impl;


import com.ttk.developer.recon.model.CompareResult;
import com.ttk.developer.recon.model.CsvRecord;
import com.ttk.developer.recon.model.ReconResult;
import com.ttk.developer.recon.model.ReconViewResult;
import com.ttk.developer.recon.service.CsvTransactionReconService;
import com.ttk.developer.recon.utility.CsvParserSimple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class CsvTransactionReconServiceImpl implements CsvTransactionReconService {
    private static final Logger logger = LoggerFactory.getLogger(CsvTransactionReconServiceImpl.class);


    @Value("${compare.key}")
    String COMPARE_KEY ;//= "TransactionID";

    @Value("#{${compare.headersMap}}")
    Map<String, String> headersMap; // set pre-defined HEADERS

    /**
     * * Read Headers row Only to compare , validate against pre-defined HEADERS
     *      * Must match ...
     * @param file
     * @return
     */
    private boolean isContainsAllHeader(MultipartFile file)  {

        CsvParserSimple csvParserSimple = new CsvParserSimple();
        List<String> firstRow = new ArrayList<>();
        boolean isContainAllHeaders;

        List<String[]> headersRow = new ArrayList<>();
        try {
            headersRow = csvParserSimple.readHeadersOnlyMultipartFile(file);
        } catch (Exception e) {
            logger.warn("Exception During Reading Headers Only.", e);
            return false;
        }
        headersMap.forEach((k,v)->logger.info("Properties Header Key={}", k));
        for (int i = 0; i < headersRow.size(); i++) {
            if(i>0) break;
            String[] headersArray = headersRow.get(i);
            logger.info("Headers: {}",Arrays.asList(headersArray));
            firstRow = Arrays.asList(headersArray);
        }

        //int headerMatchCount=0;
        if (firstRow.size() != headersMap.size()){
            logger.info("Pre-defined header count not matched with loaded header count={}", firstRow.size() );
            return false;
        }
        for (String header : firstRow) {
            if(!headersMap.containsKey(header)){
                isContainAllHeaders = false;
                //break;
                logger.info(" isContainAllHeaders={}",  isContainAllHeaders);
                return false;
            }

        }
        logger.info("I guessed File ContainAllHeaders");
        return true;
    }

    /**
     * Main Reconciliation process method here.
     * @param file1
     * @param file2
     * @return
     */
    @Override
    public ReconViewResult processAndReconcileFiles(MultipartFile file1, MultipartFile file2) {
        logger.info("Inside Reconciliation Service Implementation");
        if(file1 == null || !file1.getOriginalFilename().endsWith(".csv")){
            String message = "Invalid File. Only .csv format supported";
            throw new IllegalArgumentException(message);
        }
        if(file2 == null || !file2.getOriginalFilename().endsWith(".csv")){
            String message = "Invalid File. Only .csv format supported";
            throw new IllegalArgumentException(message);
        }

        if ( !isContainsAllHeader(file1) ) {
            String message = "%s headers do not match with pre-defined headers";
            throw new IllegalArgumentException(String.format(message, file1.getOriginalFilename()));
        }
        if ( !isContainsAllHeader(file2) ) {
            String message = "%s headers do not match with pre-defined headers";
            throw new IllegalArgumentException(String.format(message, file2.getOriginalFilename()));
        }

        try {
            ReconResult reconResultFileOne = compareFileOneAgainstFileTwo(file1, file2, false);

            ReconResult reconResultFileTwo = compareFileOneAgainstFileTwo(file2, file1, false);

            ReconViewResult reconViewResult =  new ReconViewResult();
            reconViewResult.setComputeStatus(true);
            reconViewResult.setMessage("processed");
            reconViewResult.setReconResultFileOne(reconResultFileOne);
            reconViewResult.setReconResultFileTwo(reconResultFileTwo);
            return reconViewResult;

        } catch (Exception e) {
            logger.error("Error while processing recon", e);

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage(), e);
        }

    }

    /**
     *
     * @param file1 ; Main file (left side) to compare with Another file (right side)
     * @param file2 ; Another file to be compared against Main file
     * @param isLocalTest ; unused param
     * @return
     * @throws Exception
     */

    private ReconResult compareFileOneAgainstFileTwo(final MultipartFile file1, final MultipartFile file2, boolean isLocalTest) throws Exception {
        final List<CsvRecord> fileOneCsvRecordList = convertToCsvRecordList(file1);
        final List<CsvRecord> fileTwoCsvRecordList = convertToCsvRecordList(file2);

        final Map<String, List<CsvRecord>> fileOneRecordMap = getIntoRecordMap(fileOneCsvRecordList);
        final Map<String, List<CsvRecord>> fileTwoRecordMap = getIntoRecordMap(fileTwoCsvRecordList);
        logger.info("==================================Left Main File - Checking Record Map=============");
        int FILE_ONE_TOTAL_RECORD_COUNT = 0;

        int FILE_ONE_MATCHED_COUNT = 0;
        int FILE_ONE_NOT_FOUND_IN_FILE_TWO_COUNT = 0;
        int FILE_ONE_FOUND_IN_FILE_TWO_BUT_UNMATCH_COUNT = 0;
        List<String> KEY_LIST_NOT_FOUND_IN_FILE_TWO = new ArrayList<>();

        List<String> nonUniqueKeyListMatch = new ArrayList<>();

        List<String> nonUniqueKeyListfoundNotMatch = new ArrayList<>();

        FILE_ONE_TOTAL_RECORD_COUNT = fileOneCsvRecordList.size()-1;

        //this list to be used for "unmatched report" at UI
        List<CompareResult> COMPARE_RESULT_LIST = new ArrayList<>();
        for (String key : fileOneRecordMap.keySet()) {

            if(fileTwoRecordMap.containsKey(key)){
                //Found ;;; Record with Same Key Exists in File Two ;;; further Matching
                List<CsvRecord> csvRecordsFileOne = fileOneRecordMap.get(key);

                for (CsvRecord csvRecordOne : csvRecordsFileOne) {

                    List<CsvRecord> csvRecordsFileTwo = fileTwoRecordMap.get(key);
                    List<CompareResult> internalCompareResultList = new ArrayList<>();



                    for (CsvRecord csvRecordTwo : csvRecordsFileTwo) {
                        CompareResult compareResult =  compareTwoCsvRecords(csvRecordOne, csvRecordTwo, key);
                        internalCompareResultList.add(compareResult);
                        if( !compareResult.isRecordMatch() ) {
                            //logger.info("Key={}, Unmatched Result ={}", key, compareResult);

                            nonUniqueKeyListfoundNotMatch.add(key);

                        } else {
                            nonUniqueKeyListMatch.add(key);
                            //FILE_ONE_MATCHED_COUNT++;
                        }
                    }
                    /////
                    internalCompareResultList.forEach(internalCompare-> {
                        if( !internalCompare.isRecordMatch() )
                            COMPARE_RESULT_LIST.add(internalCompare);
                    });

                    /////
                }


            } else{
                KEY_LIST_NOT_FOUND_IN_FILE_TWO.add(key);
                FILE_ONE_NOT_FOUND_IN_FILE_TWO_COUNT++;
                //////// Setting Result for Not Found CsvRecord
                Optional<CsvRecord> anyCsvRec = fileOneCsvRecordList
                        .stream()
                        .filter(csvRecord -> key.equalsIgnoreCase(csvRecord.getTransaction().get(COMPARE_KEY)))
                        .findAny();
                //logger.info("anyCsvRec={}",anyCsvRec.get().getTransaction());
                ////////
                anyCsvRec.ifPresent(csvRecord -> {
                    CompareResult result = new CompareResult();
                    result.setCompareKey(key);
                    result.setRecordMatch(false);
                    result.setMainRecord(csvRecord);
                    result.setReason("Record Not Found in Another File");
                    result.setRowNumberInFileOne(String.valueOf(csvRecord.getRowNumber()));
                    result.setValueInFileTwo(Collections.emptyList());

                    result.setComparedHeader(Collections.emptyList());
                    COMPARE_RESULT_LIST.add(result);
                });


            }
        }
        

        FILE_ONE_MATCHED_COUNT =
                (int) nonUniqueKeyListMatch
                        .stream()
                        .distinct()
                        .count();

        List<String> KEY_LIST_FOUND_IN_FILE_TWO_BUT_NOT_MATCH = nonUniqueKeyListfoundNotMatch
                .stream()
                .distinct()
                .collect(Collectors.toList());


        FILE_ONE_FOUND_IN_FILE_TWO_BUT_UNMATCH_COUNT = KEY_LIST_FOUND_IN_FILE_TWO_BUT_NOT_MATCH.size();

        logger.info("=====Compare Main File Against Another======" +
                        "\nFILE_ONE TOTAL COUNT={} " +
                        "\nFILE_ONE MATCHED COUNT={} " +
                        "\nFILE_ONE NOT_FOUND_IN_FILE_TWO COUNT={} " +
                        "\nFILE_ONE FOUND_IN_FILE_TWO BUT UNMATCH_COUNT={}",
                FILE_ONE_TOTAL_RECORD_COUNT,
                FILE_ONE_MATCHED_COUNT,
                FILE_ONE_NOT_FOUND_IN_FILE_TWO_COUNT,
                FILE_ONE_FOUND_IN_FILE_TWO_BUT_UNMATCH_COUNT);
        logger.info("===================================================================");

        logger.info("\nKEY LIST FOUND BUT NOT MATCHED IN ANOTHER FILE TWO={} " +
                        "\nKEY LIST NOT FOUND IN ANOTHER FILE TWO={}",
                KEY_LIST_FOUND_IN_FILE_TWO_BUT_NOT_MATCH.toString(),
                KEY_LIST_NOT_FOUND_IN_FILE_TWO.toString());
        logger.info("===================================================================");

        COMPARE_RESULT_LIST.forEach(compare->logger.info("Unmatched Result={}",compare));
        //logger.warn("\nNon Unique, Found But Not Match Key List From File Two={}",nonUniqueKeyListfoundNotMatch.toString());
        //logger.warn("\nUnique Found But Not Match Key List={}",keyListFoundInFileTwoButNotMatch.toString());
        ReconResult reconResult = new ReconResult();
        reconResult.setTotalRecords(FILE_ONE_TOTAL_RECORD_COUNT) ;

        reconResult.setMatchedRecords(FILE_ONE_MATCHED_COUNT) ;

        //reconResult.setUnmatchedRecords(FILE_ONE_NOT_FOUND_IN_FILE_TWO_COUNT + FILE_ONE_FOUND_IN_FILE_TWO_BUT_UNMATCH_COUNT) ;
//        logger.info("{} - {} UNMATCHED COUNT!!!==={}",FILE_ONE_TOTAL_RECORD_COUNT, FILE_ONE_MATCHED_COUNT,
//                FILE_ONE_TOTAL_RECORD_COUNT - FILE_ONE_MATCHED_COUNT);
        reconResult.setUnmatchedRecords(FILE_ONE_TOTAL_RECORD_COUNT - FILE_ONE_MATCHED_COUNT) ;

        reconResult.setNotFoundInAnotherFileCount(FILE_ONE_NOT_FOUND_IN_FILE_TWO_COUNT) ;

        reconResult.setFoundButNotMatchInAnotherFileCount(FILE_ONE_FOUND_IN_FILE_TWO_BUT_UNMATCH_COUNT) ;

        reconResult.setKeyListNotFoundInFileTwo(KEY_LIST_NOT_FOUND_IN_FILE_TWO) ;

        reconResult.setKeyListFoundInFileTwoButNotMatch(KEY_LIST_FOUND_IN_FILE_TWO_BUT_NOT_MATCH) ;

        reconResult.setCompareResultList(COMPARE_RESULT_LIST) ;

        return reconResult;
    }

    /**
     * Comparing one record from Main file with one from Another file
     * output: result, which contained necessary info
     * @param one
     * @param two
     * @param key
     * @return
     * @see CompareResult
     */
    private CompareResult compareTwoCsvRecords(CsvRecord one, CsvRecord two, String key){


        CompareResult result = new CompareResult();
        result.setCompareKey(key);
        result.setRecordMatch(true);

        result.setMainRecord(one);

        for (String headerKey : one.getTransaction().keySet()) {

            String twoValue = two.getTransaction().get(headerKey);
            String oneValue = one.getTransaction().get(headerKey);
            List<String> headers = new ArrayList<>();
            List<String> recordOneValues = new ArrayList<>();
            List<String> recordTwoValues = new ArrayList<>();

            if(!twoValue.equalsIgnoreCase(oneValue) ){

                result.setRecordMatch(false);

                result.setReason("Record Values Not Matched");

                headers.add(headerKey);
                result.setComparedHeader(headers);

                recordOneValues.add(oneValue);
                result.setValueInFileOne(recordOneValues);

                recordTwoValues.add(twoValue);
                result.setValueInFileTwo(recordTwoValues);


                result.setRowNumberInFileOne(String.valueOf(one.getRowNumber()));
                result.setRowNumberInFileTwo(String.valueOf(two.getRowNumber()));

            }

        }
//        if (resultsMap.containsValue(false)) return resultsMap;
//        resultsMap.put("AllMatch", String.valueOf(true));
        return result;
    }

    /**
     * This is intermediate converter method only
     * From
     * input: CSV Raw File ->
     * output: CsvRecords List
     * @param file
     * @return
     * @see List<CsvRecord>
     * @throws Exception
     */
    private List<CsvRecord> convertToCsvRecordList(MultipartFile file) throws Exception {
        //


        CsvParserSimple csvParserSimple = new CsvParserSimple();
        //
        List<String[]> result = csvParserSimple.readAsMultipartFile(file);

        Map<String, Integer> headerMap = new LinkedHashMap<>();
        List<CsvRecord> csvRecordList = new ArrayList<>();
        int rowIndex = 1;
        for (String[] arrays : result) {

            if (rowIndex == 1) { //READING for header only ;;; HEADER row starts at Row 1
                for (int i = 0; i < arrays.length; i++) {
                    headerMap.put(arrays[i], i);
                }
            }

            CsvRecord csvRecord = new CsvRecord(rowIndex, headerMap, Arrays.asList(arrays));
            csvRecordList.add(csvRecord);

            rowIndex++;

        }
        logger.info("===============================");
        logger.info("Total Records (-Headers)={}", (csvRecordList.size()-1));
        logger.info("===============================");

        return csvRecordList;
    }

    /**
     * This method covert from List to Map
     *
     * CSV Raw File ->
     * input: csvRecordList ->
     * output:
     * Map with key = Main ID of the record; which is configurable via properties file
     * Map value = list of Csv Record "group by" ID
     * @param csvRecordList
     * @return
     */

    private Map<String, List<CsvRecord>> getIntoRecordMap(List<CsvRecord> csvRecordList) {


        Map<String, List<CsvRecord>> csvRecordMap = csvRecordList
                .stream()
                .filter(csvRecord -> csvRecord.getRowNumber() != 1)

                .collect(Collectors.groupingBy((map) ->
                        map.getTransaction().get(COMPARE_KEY), LinkedHashMap::new, Collectors.toList()));

        return csvRecordMap;
    }
}
