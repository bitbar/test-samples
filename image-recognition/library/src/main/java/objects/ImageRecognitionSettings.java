package objects;

public class ImageRecognitionSettings {

    private final int DEFAULT_RETRIES = 5;
    private final int DEFAULT_RETRY_WAIT = 0;
    private final double DEFAULT_TOLERANCE = 0.6;
    private final boolean DEFAULT_CROP = false;

    public ImageRecognitionSettings(){
        this.retries = DEFAULT_RETRIES;
        this.retryWaitTime = DEFAULT_RETRY_WAIT;
        this.tolerance = DEFAULT_TOLERANCE;
        this.crop = DEFAULT_CROP;
    }


    public int getRetries() {
        return retries;
    }
    public void setRetries(int retries) {
        this.retries = retries;
    }


    public int getRetryWaitTime() {
        return retryWaitTime;
    }


    public void setRetryWaitTime(int retryWaitTime) {
        this.retryWaitTime = retryWaitTime;
    }


    public double getTolerance() {
        return tolerance;
    }


    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }


    public boolean isCrop() {
        return crop;
    }


    public void setCrop(boolean crop) {
        this.crop = crop;
    }


    private int retries;
    private int retryWaitTime;
    private double tolerance;
    private boolean crop;
}
