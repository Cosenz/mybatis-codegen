package it.cosenzproject.mybatiscodegen.model;

public class Method {

    private String name;
    private Property parameterType;
    private ResultMethod result;

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
     * @return the parameterType
     */
    public Property getParameterType() {
        return this.parameterType;
    }

    /**
     * @param parameterType
     *            the parameterType to set
     */
    public void setParameterType(Property parameterType) {
        this.parameterType = parameterType;
    }

    /**
     * @return the result
     */
    public ResultMethod getResult() {
        return this.result;
    }

    /**
     * @param result
     *            the result to set
     */
    public void setResult(ResultMethod result) {
        this.result = result;
    }

}
