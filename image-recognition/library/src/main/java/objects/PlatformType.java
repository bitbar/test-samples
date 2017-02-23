package objects;

public enum PlatformType {
    ANDROID("Android"), 
    IOS("iOS");

    PlatformType(String platformName){
        this.platformName=platformName;
    }

    public String getPlatformName(){
        return this.platformName;
    }

    private String platformName;

}
