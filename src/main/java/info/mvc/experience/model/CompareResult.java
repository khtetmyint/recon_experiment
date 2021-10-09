package info.mvc.experience.model;

import java.util.List;

public class CompareResult {

    private String compareKey;
    private boolean recordMatch;
    private String reason;
    private List<String> comparedHeader;
    private List<String> valueInFileOne;
    private List<String> valueInFileTwo;
    private String rowNumberInFileOne;
    private String rowNumberInFileTwo;





    public boolean isRecordMatch() {
        return recordMatch;
    }

    public String getCompareKey() {
        return compareKey;
    }

    public void setRecordMatch(boolean recordMatch) {
        this.recordMatch = recordMatch;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setComparedHeader(List<String> comparedHeader) {
        this.comparedHeader = comparedHeader;
    }

    public void setValueInFileOne(List<String> valueInFileOne) {
        this.valueInFileOne = valueInFileOne;
    }

    public void setValueInFileTwo(List<String> valueInFileTwo) {
        this.valueInFileTwo = valueInFileTwo;
    }

    public void setRowNumberInFileOne(String rowNumberInFileOne) {
        this.rowNumberInFileOne = rowNumberInFileOne;
    }

    public void setRowNumberInFileTwo(String rowNumberInFileTwo) {
        this.rowNumberInFileTwo = rowNumberInFileTwo;
    }

    public void setCompareKey(String compareKey) {
        this.compareKey = compareKey;
    }

    @Override
    public String toString() {
        return "CompareResult{" +
                "compareKey='" + compareKey + '\'' +
                ", recordMatch=" + recordMatch +
                ", reason='" + reason + '\'' +
                ", comparedHeader=" + comparedHeader +
                ", valueInFileOne=" + valueInFileOne +
                ", valueInFileTwo=" + valueInFileTwo +
                ", rowNumberInFileOne='" + rowNumberInFileOne + '\'' +
                ", rowNumberInFileTwo='" + rowNumberInFileTwo + '\'' +
                '}';
    }
}
