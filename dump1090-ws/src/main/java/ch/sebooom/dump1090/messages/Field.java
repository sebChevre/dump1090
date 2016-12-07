package ch.sebooom.dump1090.messages;

/**
 * Model a SBS1 Messsage Fields
 */
class Field {

    private int fieldPosition;
    private String fieldName;
    private String value;

    Field(String fieldName, int fieldPosition){
        this.fieldName = fieldName;
        this.fieldPosition = fieldPosition;
    }

    void withValue(String value){
        this.value = value;
    }

    public String getFieldName() {
        return fieldName;
    }

    public int getFieldPosition(){
        return fieldPosition;
    }

    String getValue(){
        return value;
    }


}
