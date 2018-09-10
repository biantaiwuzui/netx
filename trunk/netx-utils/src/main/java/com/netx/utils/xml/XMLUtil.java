package com.netx.utils.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import java.io.Writer;

/**
 *
 * @author Runshine
 * @since 2015-9-24
 * @version 1.0.0
 *
 */
public class XMLUtil {
    private static class CDataXStream extends XStream {
        public CDataXStream() {
            super(new XppDriver() {
                public HierarchicalStreamWriter createWriter(Writer out) {
                    return new PrettyPrintWriter(out, new NoNameCoder()) {
                        // 对所有xml节点的转换都增加CDATA标记
                        boolean cdata = true;

                        public void startNode(String name, Class clazz) {
                            super.startNode(name, clazz);
                        }

                        protected void writeText(QuickWriter writer, String text) {
                            if (cdata) {
                                writer.write("<![CDATA[");
                                writer.write(text);
                                writer.write("]]>");
                            } else {
                                writer.write(text);
                            }
                        }
                    };
                }
            });
        }
    }

    public static String toXML(Object obj) {
        CDataXStream xs = new CDataXStream();
        xs.alias("xml", obj.getClass());
        return xs.toXML(obj);
    }

    public static <T> T fromXML(String xml, Class<T> clazz) {
        CDataXStream xs = new CDataXStream();
        xs.alias("xml", clazz);
        return (T)xs.fromXML(xml);
    }
}
