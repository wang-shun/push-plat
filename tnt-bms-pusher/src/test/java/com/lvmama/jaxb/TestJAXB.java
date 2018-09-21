package com.lvmama.jaxb;

import com.lvmama.bms.core.commons.utils.JAXBUtils;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.StringReader;

public class TestJAXB {

    @Test
    public void testUnmarshall() {

        try {
            String xml = "<response status=\"200\"><msg>500</msg></response>";
            Response response = JAXBUtils.deserialize(new StringReader(xml), Response.class);
            System.out.println(response);
        } catch (JAXBException e) {
            e.printStackTrace();
        }


    }



}
