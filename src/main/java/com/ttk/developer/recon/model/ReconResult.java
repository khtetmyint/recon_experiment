package com.ttk.developer.recon.model;

import java.util.List;

/**
 * Represent summary of reconciled result (counts) + list of unmatched result (to display in unmatched report section at view)
 * per CSV file
 *
 */
public class ReconResult {

    /**
     * 1. Total Record Count (excluding Header; 1st row from CSV)
     */
    int totalRecords;

    /**
     * 2. Matching Records of main file with another file
     */
    int matchedRecords;

    /**
     * 3.
     * Total Record - Matched Records = You got unmatched, simple as that.. or is it?
     */
    int unmatchedRecords;

    /**
     * 4. number of count which are not found in another file
     */
    int notFoundInAnotherFileCount;

    /**
     * 5. number of count which are found in another file but not matched one/more values
     */
    int foundButNotMatchInAnotherFileCount;

    List<String> keyListNotFoundInFileTwo;
    List<String> keyListFoundInFileTwoButNotMatch;

    List<CompareResult> compareResultList;


    ////


    public int getTotalRecords() {
        return totalRecords;
    }

    public int getMatchedRecords() {
        return matchedRecords;
    }

    public int getUnmatchedRecords() {
        return unmatchedRecords;
    }

    public int getNotFoundInAnotherFileCount() {
        return notFoundInAnotherFileCount;
    }

    public int getFoundButNotMatchInAnotherFileCount() {
        return foundButNotMatchInAnotherFileCount;
    }

    public List<String> getKeyListNotFoundInFileTwo() {
        return keyListNotFoundInFileTwo;
    }

    public List<String> getKeyListFoundInFileTwoButNotMatch() {
        return keyListFoundInFileTwoButNotMatch;
    }

    public List<CompareResult> getCompareResultList() {
        return compareResultList;
    }

    ///////////


    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public void setMatchedRecords(int matchedRecords) {
        this.matchedRecords = matchedRecords;
    }

    public void setUnmatchedRecords(int unmatchedRecords) {
        this.unmatchedRecords = unmatchedRecords;
    }

    public void setNotFoundInAnotherFileCount(int notFoundInAnotherFileCount) {
        this.notFoundInAnotherFileCount = notFoundInAnotherFileCount;
    }

    public void setFoundButNotMatchInAnotherFileCount(int foundButNotMatchInAnotherFileCount) {
        this.foundButNotMatchInAnotherFileCount = foundButNotMatchInAnotherFileCount;
    }

    public void setKeyListNotFoundInFileTwo(List<String> keyListNotFoundInFileTwo) {
        this.keyListNotFoundInFileTwo = keyListNotFoundInFileTwo;
    }

    public void setKeyListFoundInFileTwoButNotMatch(List<String> keyListFoundInFileTwoButNotMatch) {
        this.keyListFoundInFileTwoButNotMatch = keyListFoundInFileTwoButNotMatch;
    }

    public void setCompareResultList(List<CompareResult> compareResultList) {
        this.compareResultList = compareResultList;
    }
}
