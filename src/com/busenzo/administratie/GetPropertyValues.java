/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.busenzo.administratie;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 *
 * @author stasiuz
 */
public class GetPropertyValues {
    String[] results = new String[2];
    InputStream inputStream;

    public String[] getPropValues() throws IOException {
        try {
            Properties prop = new Properties();
            String propFileName = "/resources/config.properties";

            inputStream = getClass().getResourceAsStream(propFileName);
                        
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("properties file " + propFileName + " not found in the classpath");
            }

            results[0] = prop.getProperty("restServer");
            results[1] = prop.getProperty("restKey");
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            inputStream.close();
        }
        
        return results;
    }
}
