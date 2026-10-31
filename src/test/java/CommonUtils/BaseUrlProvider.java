package CommonUtils;


public class BaseUrlProvider {
    private  BaseUrlProvider()
    {

    }

    public static String getBaseUrl()
    {
        String Environment = PropertyReader.getProperty("Environment");

        return switch (Environment.toLowerCase())
        {
            case "prod" -> PropertyReader.getProperty("OTT_Prod_Base_Url");
            case "uat" -> PropertyReader.getProperty("OTT_UAT_Base_Url");
            case "stage" -> PropertyReader.getProperty("OTT_Stage_Base_Url");

            default -> throw new IllegalArgumentException("Unsupported enviroment: " + Environment);

        };
    }


}
