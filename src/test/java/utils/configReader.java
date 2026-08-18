package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class configReader {
	
	 Properties properties;
	private static final String configFilePath = ".\\src\\test\\resources\\config.Properties";
	
	//This constructor will just load the properties file
	public configReader() {
        properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(configFilePath)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config.properties file");
        }
    }

  
  public String getProperty(String key)
  {
	  return properties.getProperty(key);
  }

  
  public int getIntProperty(String key)
  {
	  return Integer.parseInt(properties.getProperty(key));
  }

}
