package com.lvmama.bms.core.commons.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.Reader;

public class JAXBUtils {

    public static <T> T deserialize(Reader reader, Class<T> target) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(target);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        T result = (T) unmarshaller.unmarshal(reader);
        return result;
    }


}
