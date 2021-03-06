package org.eclipse.yasson.customization.model;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;

/**
 * @author Roman Grigoriadi
 */
public class CreatorWithoutJsonbProperty {

    private final String par1;
    private final String par2;
    private double par3;

    @JsonbCreator
    public CreatorWithoutJsonbProperty(@JsonbProperty("s1") String par1, String par2, double d1) {
        this.par1 = par1;
        this.par2 = par2;
        this.par3 = d1;
    }

    public String getPar1() {
        return par1;
    }

    public String getPar2() {
        return par2;
    }

    public double getPar3() {
        return par3;
    }
}
