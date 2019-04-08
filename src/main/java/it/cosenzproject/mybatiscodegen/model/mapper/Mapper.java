package it.cosenzproject.mybatiscodegen.model.mapper;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "mapper")
@XmlAccessorType(XmlAccessType.FIELD)
public class Mapper {

    @XmlElement(name = "select")
    private List<Select> select;
    @XmlElement(name = "insert")
    private List<Insert> insert;
    @XmlElement(name = "update")
    private List<Update> update;
    @XmlElement(name = "resultMap")
    private List<ResultMap> resultMap;
    @XmlAttribute(name = "namespace")
    private String namespace;

    public List<Select> getSelect() {
        if (this.select == null) {
            return new ArrayList<>();
        }
        return this.select;
    }

    public void setSelect(List<Select> select) {
        this.select = select;
    }

    /**
     * @return the update
     */
    public List<Update> getUpdate() {
        if (this.update == null) {
            return new ArrayList<>();
        }
        return this.update;
    }

    /**
     * @param update
     *            the update to set
     */
    public void setUpdate(List<Update> update) {
        this.update = update;
    }

    /**
     * @return the insert
     */
    public List<Insert> getInsert() {
        if (this.insert == null) {
            return new ArrayList<>();
        }
        return this.insert;
    }

    /**
     * @param insert
     *            the insert to set
     */
    public void setInsert(List<Insert> insert) {
        this.insert = insert;
    }

    public List<ResultMap> getResultMap() {
        if (this.resultMap == null) {
            return new ArrayList<>();
        }
        return this.resultMap;
    }

    public void setResultMap(List<ResultMap> resultMap) {
        this.resultMap = resultMap;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

}
