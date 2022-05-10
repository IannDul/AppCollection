package io.request;

import io.Properties;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class PropertiesTest {

    private List<String> in;

    @Test
    void parseProperties() {
        in = new ArrayList<String>(
                List.of(new String[]{
                        "name",
                        "213.4",
                        "97",
                        "213",
                        "null",
                        "fire",
                        "good",
                        "213",
                        "231"
                })
        );
        try {
            Properties p = Properties.parseProperties(in, 0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
    }
}