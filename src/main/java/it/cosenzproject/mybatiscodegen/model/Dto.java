package it.cosenzproject.mybatiscodegen.model;

import java.util.ArrayList;
import java.util.List;

public class Dto extends DAOBase {

    private List<Property> property;

    /**
     * @return the property
     */
    public List<Property> getProperty() {
        if (this.property == null) {
            return new ArrayList<>();
        }
        return this.property;
    }

    /**
     * @param property
     *            the property to set
     */
    public void setProperty(List<Property> property) {
        this.property = property;
    }
}
