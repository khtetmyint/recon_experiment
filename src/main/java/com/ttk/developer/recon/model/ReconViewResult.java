package com.ttk.developer.recon.model;

public class ReconViewResult {

    private ReconResult reconResultFileOne;
    private ReconResult reconResultFileTwo;

    private boolean computeStatus;
    private String message;

    public ReconViewResult() {
    }

    public ReconViewResult(boolean computeStatus, String message) {
        this.computeStatus = computeStatus;
        this.message = message;
    }


    /////////////

    public ReconResult getReconResultFileOne() {
        return reconResultFileOne;
    }

    public ReconResult getReconResultFileTwo() {
        return reconResultFileTwo;
    }

    public boolean isComputeStatus() {
        return computeStatus;
    }

    public String getMessage() {
        return message;
    }

    ////////////////

    public void setReconResultFileOne(ReconResult reconResultFileOne) {
        this.reconResultFileOne = reconResultFileOne;
    }

    public void setReconResultFileTwo(ReconResult reconResultFileTwo) {
        this.reconResultFileTwo = reconResultFileTwo;
    }

    public void setComputeStatus(boolean computeStatus) {
        this.computeStatus = computeStatus;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ReconViewResult{" +
                "computeStatus=" + computeStatus +
                ", message='" + message + '\'' +
                '}';
    }
}
