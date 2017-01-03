package ch.sebooom.dump1090.messages.sbs1;

import java.util.Objects;

/**
 * Created by seb on .
 * <p>
 * ${VERSION}
 */
public class ICAOIdent {

    private String hexIdent;

    public ICAOIdent(String hexIdent){
        this.hexIdent = hexIdent;
    }

    public String icaoIdent(){
        return this.hexIdent;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null)
        {
            return false;
        }
        if (getClass() != o.getClass())
        {
            return false;
        }

        final ICAOIdent other = (ICAOIdent) o;

        return Objects.equals(this.hexIdent,((ICAOIdent) o).hexIdent);

    }

    @Override
    public int hashCode() {

        return Objects.hash(hexIdent);
    }
}
