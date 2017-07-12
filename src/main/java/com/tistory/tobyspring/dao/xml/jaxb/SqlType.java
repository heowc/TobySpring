//
// ÀÌ ÆÄÀÏÀº JAXB(JavaTM Architecture for XML Binding) ÂüÁ¶ ±¸Çö 2.2.8-b130911.1802 ¹öÀüÀ» ÅëÇØ »ý¼ºµÇ¾ú½À´Ï´Ù. 
// <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>¸¦ ÂüÁ¶ÇÏ½Ê½Ã¿À. 
// ÀÌ ÆÄÀÏÀ» ¼öÁ¤ÇÏ¸é ¼Ò½º ½ºÅ°¸¶¸¦ ÀçÄÄÆÄÀÏÇÒ ¶§ ¼öÁ¤ »çÇ×ÀÌ ¼Õ½ÇµË´Ï´Ù. 
// »ý¼º ³¯Â¥: 2017.06.30 ½Ã°£ 02:00:49 PM KST 
//


package com.tistory.tobyspring.dao.xml.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sqlType", propOrder = {
    "value"
})
public class SqlType {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "key", required = true)
    protected String key;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String value) {
        this.key = value;
    }

}
