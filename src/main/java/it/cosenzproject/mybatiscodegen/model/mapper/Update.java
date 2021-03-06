package it.cosenzproject.mybatiscodegen.model.mapper;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "update")
@XmlAccessorType(XmlAccessType.FIELD)
public class Update {

    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "parameterType")
    private String parameterType;
    @XmlAttribute(name = "statementType")
    private String statementType;
    @XmlValue
    private String body;

    /**
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the parameterType
     */
    public String getParameterType() {
        return this.parameterType;
    }

    /**
     * @param parameterType
     *            the parameterType to set
     */
    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    /**
     * @return the statementType
     */
    public String getStatementType() {
        return this.statementType;
    }

    /**
     * @param statementType
     *            the statementType to set
     */
    public void setStatementType(String statementType) {
        this.statementType = statementType;
    }

    /**
     * @return the body
     */
    public String getBody() {
        return this.body;
    }

    /**
     * @param body
     *            the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

}
