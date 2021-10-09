package info.mvc.experience.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CsvRecord {

    private int rowNumber;
    private Map<String, Integer> headerMap;
    //private List<String> headerValues;
    private List<String> dataValues;

    //Map as Transaction ;;;
    private Map<String, String> transaction;

    public CsvRecord(int rowNumber, Map<String, Integer> headerMap, List<String> dataValues) {
        this.rowNumber = rowNumber;
        this.headerMap = headerMap;
        this.dataValues = dataValues;
        this.transaction = new LinkedHashMap();
        //if(this.rowNumber !=0 ){
        for (String headerKey : this.headerMap.keySet()) {
            final Integer integer = this.headerMap.get(headerKey);
//            System.out.println("filling transaction map Row:"+this.rowNumber+":"+headerKey +"="+this.dataValues.get(integer));
            transaction.put(headerKey, this.dataValues.get(integer));
        }
        //}


    }


    ///////////

    public int getRowNumber() {
        return rowNumber;
    }

    private Map<String, Integer> getHeaderMap() {
        return headerMap;
    }



    private List<String> getDataValues() {
        return dataValues;
    }

    public Map<String, String> getTransaction() {
        return transaction;
    }

    ///////////////////////////

    private void getDataByHeader(){
        for (String headerKey : this.headerMap.keySet()) {
            final Integer integer = this.headerMap.get(headerKey);
            System.out.println("Row:"+this.rowNumber+":"+headerKey +"="+this.dataValues.get(integer));
        }
    }

    private Transaction convertToTransaction(){
        Map valuesHolder = new LinkedHashMap();
        for (String headerKey : this.headerMap.keySet()) {
            final Integer integer = this.headerMap.get(headerKey);
//            System.out.println("Row:"+this.rowNumber+":"+headerKey +"="+this.dataValues.get(integer));
            valuesHolder.put(headerKey, this.dataValues.get(integer));
        }
        return new Transaction(valuesHolder);
    }
}
