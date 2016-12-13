package ch.sebooom.dump1090.messages.sbs1;

/**
 * Model a SBS1 Messsage Fields
 */
class Field {

    private FieldType fieldType;
    private String value;

    Field(FieldType fieldType, String value){
        this.fieldType = fieldType;
        this.value = value;
    }

    void withValue(String value){
        this.value = value;
    }

    public FieldType fieldType() {
        return fieldType;
    }


    String getValue(){
        return value;
    }


}
