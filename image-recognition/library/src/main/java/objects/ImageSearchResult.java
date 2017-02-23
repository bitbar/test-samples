package objects;

public class ImageSearchResult {

    public ImageSearchResult(){
        this.screenshotFile = null;
        this.imageLocation = null;
    }

    public ImageSearchResult(String screenshotFile, ImageLocation imageLocation){
        this.screenshotFile = screenshotFile;
        this.imageLocation = imageLocation;
    }

    public boolean isFound(){
        return screenshotFile!=null && imageLocation!=null;
    }

    public String getScreenshotFile() {
        return screenshotFile;
    }
    public void setScreenshotFile(String screenshotFile) {
        this.screenshotFile = screenshotFile;
    }

    public ImageLocation getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(ImageLocation imageLocation) {
        this.imageLocation = imageLocation;
    }

    private String screenshotFile;
    private ImageLocation imageLocation;
}
