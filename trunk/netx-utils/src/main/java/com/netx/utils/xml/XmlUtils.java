package com.netx.utils.xml;


import com.sun.xml.bind.marshaller.CharacterEscapeHandler;
import com.sun.xml.bind.marshaller.DataWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @author Closure.Yang
 * @since 2014/9/25
 */
public class XmlUtils {
    public static <T> String objToXml(T t, Class<T> clazz) {
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            /*marshaller.setProperty("com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler",
                    new CharacterEscapeHandler() {
                        @Override
                        public void escape(char[] ch, int start, int length, boolean isAttVal,
                                           Writer writer) throws IOException {
                            writer.write(ch, start, length);
                        }
                    });*/

            StringWriter sw = new StringWriter();
            marshaller.marshal(t, sw);
            return sw.toString();

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String objToXmlWithCData(Object t, String encoding) {

        String result = null;
        try {
            JAXBContext context = JAXBContext.newInstance(t.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            //marshaller.setProperty(CustomCharacterEscapeHandler.class.getName(),  new CustomCharacterEscapeHandler());
            StringWriter writer = new StringWriter();
            DataWriter dataWriter = new DataWriter(writer, "UTF-8", new CharacterEscapeHandler() {
                @Override
                public void escape(char[] ch, int start, int length, boolean isAttVal,
                                   Writer writer) throws IOException {
                    writer.write(ch, start, length);
                }
            });
            marshaller.marshal(t, dataWriter);
            result = writer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static <T> T xmlToObj(String xml, Class<T> clz) {
        try {
            JAXBContext context = JAXBContext.newInstance(clz);
            Unmarshaller um = context.createUnmarshaller();
            StringReader sr = new StringReader(xml);
            return (T) um.unmarshal(sr);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }


}
