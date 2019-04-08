package it.cosenzproject.mybatiscodegen.model;

import java.util.ArrayList;
import java.util.List;

public class DAOMapper extends DAOBase {

    private List<Method> methods;

    /**
     * @return the methods
     */
    public List<Method> getMethods() {
        if (this.methods == null) {
            return new ArrayList<>();
        }
        return this.methods;
    }

    /**
     * @param methods
     *            the methods to set
     */
    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }
}
