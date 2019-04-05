package com.z;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

public class Company {

    @Property(propertyName = "com.mycompany.name")
    private String myCompanyName;

    @Property(propertyName = "com.mycompany.owner", defaultValue = "I am owner.")
    private String myCompanyOwner;

    @Property(propertyName = "com.mycompany.address")
    private Address address;

    private static Company company;

    public static Company getInstance() {

        if (company == null) {
            synchronized (Company.class) {
                if (company == null) {
                    company = new Company();
                }
            }
        }
        return company;
    }

    private Company() {
        doRefresh();
    }

    synchronized public void doRefresh() {

        HashMap<String, String> properties = readProperties();

        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Property.class)) {
                Property fAnno = field.getAnnotation(Property.class);

                if (properties.containsKey(fAnno.propertyName())) {
                    setFieldSafely(field, properties.get(fAnno.propertyName()));
                } else {
                    setFieldSafely(field, fAnno.defaultValue());
                }

            }
        }

    }

    private void setFieldSafely(Field field, String value) {

        try {
            if (JsonUtils.isValidJSON(value)) {
                ObjectMapper mapper = new ObjectMapper();
                field.set(this, mapper.readValue(value, field.getType()));
            } else {
                field.set(this, value);
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, String> readProperties() {
        HashMap<String, String> properties = new HashMap<>();

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(Constants.PROPERTIES_PATH));
            String line = reader.readLine();
            while (line != null) {

                String[] values = line.split("=", 2);
                properties.put(values[0].trim(), values[1].trim());
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
