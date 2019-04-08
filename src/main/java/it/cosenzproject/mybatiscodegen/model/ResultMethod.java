package it.cosenzproject.mybatiscodegen.model;

public class ResultMethod {

    private String name;
    private String type;

    public ResultMethod() {
        super();
    }

    public ResultMethod(String name, String type) {
        super();
        this.name = name;
        this.type = type;
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
