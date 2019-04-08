package it.cosenzproject.mybatiscodegen.model;

public class Property {

    private String name;
    private String type;

    public Property(String name, String type) {
        super();
        this.name = name;
        this.type = type;
    }

    public Property() {
        super();
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    public String getType() {
        return this.type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
}
