package it.cosenzproject.mybatiscodegen.model;

public class DAOBase {

    private String packageName;
    private String name;

    /**
     * @return the packageName
     */
    public String getPackageName() {
        return this.packageName;
    }

    /**
     * @param packageName
     *            the packageName to set
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
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

}
