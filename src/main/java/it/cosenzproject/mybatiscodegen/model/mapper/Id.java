package it.cosenzproject.mybatiscodegen.model.mapper;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Id {

    @XmlAttribute(name = "column")
    private String column;
    @XmlAttribute(name = "property")
    private String property;

    /**
     * @return the column
     */
    public String getColumn() {
        return this.column;
    }

    /**
     * @param column
     *            the column to set
     */
    public void setColumn(String column) {
        this.column = column;
    }

    /**
     * @return the property
     */
    public String getProperty() {
        return this.property;
    }

    /**
     * @param property
     *            the property to set
     */
    public void setProperty(String property) {
        this.property = property;
    }
}
