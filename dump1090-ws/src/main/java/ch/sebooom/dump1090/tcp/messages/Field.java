package ch.sebooom.dump1090.tcp.messages;

/**
 * Created by seb on 22.11.16.
 */
public class Field {

    private int fieldPosition;
    private String fieldName;
    private String value;

    public Field(String fieldName, int fieldPosition){
        this.fieldName = fieldName;
        this.fieldPosition = fieldPosition;
    }

    public void withValue(String value){
        this.value = value;
    }

    public String getFieldName() {
        return fieldName;
    }

    public int getFieldPosition(){
        return fieldPosition;
    }

    public String getValue(){
        return value;
    }


}
