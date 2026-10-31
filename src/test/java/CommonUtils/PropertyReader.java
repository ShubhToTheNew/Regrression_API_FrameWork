package CommonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertyReader {

    private static final Properties CONFIG = new Properties();
    private static final Properties DATA_SET = new Properties();

    static {
        loadProperties(CONFIG, "config.properties");

        String environment = CONFIG.getProperty("Environment");
        if (environment == null || environment.isBlank()) {
            throw new IllegalStateException("Environment is missing in config.properties");
        }

        String dataSetFile = switch (environment.trim().toLowerCase()) {
            case "prod" -> "DataSet_Prod.properties";
            case "stage" -> "DataSet_Stage.properties";
            case "uat" -> "DataSet_UAT.properties";
            default -> throw new IllegalArgumentException(
                    "Unsupported environment: " + environment
            );
        };

        loadProperties(DATA_SET, dataSetFile);
    }

    private PropertyReader() {
    }

    public static String getProperty(String key) {
        String value = DATA_SET.getProperty(key);

        if (value == null || value.isBlank()) {
            value = CONFIG.getProperty(key);
        }

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Property is missing or empty: " + key
            );
        }

        return value.trim();
    }

    private static void loadProperties(Properties properties, String fileName) {
        try (InputStream inputStream =
                     PropertyReader.class.getClassLoader()
                             .getResourceAsStream(fileName)) {

            if (inputStream == null) {
                throw new IllegalStateException(fileName + " was not found in src/test/resources");
            }

            properties.load(inputStream);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load " + fileName, exception);
        }
    }
}