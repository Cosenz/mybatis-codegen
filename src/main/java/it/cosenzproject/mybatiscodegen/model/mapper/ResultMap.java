package it.cosenzproject.mybatiscodegen.model.mapper;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "resultMap")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResultMap {

    @XmlElement(name = "id")
    private List<Id> ids;
    @XmlElement(name = "result")
    private List<Result> result;
    @XmlAttribute(name = "type")
    private String type;

    public List<Result> getResult() {
        if (this.result == null) {
            return new ArrayList<>();
        }
        return this.result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the ids
     */
    public List<Id> getIds() {
        if (this.ids == null) {
            return new ArrayList<>();
        }
        return this.ids;
    }

    /**
     * @param ids
     *            the ids to set
     */
    public void setIds(List<Id> ids) {
        this.ids = ids;
    }

}
