/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sources.siren;

/**
 *
 * @author marieroca
 */
public class SirenPOJO {
    private String nhits;

    private Parameters parameters;

    private Records[] records;

    public String getNhits() {
        return nhits;
    }

    public void setNhits(String nhits) {
        this.nhits = nhits;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public Records[] getRecords() {
        return records;
    }

    public void setRecords(Records[] records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return "ClassPojo [nhits = " + nhits + ", parameters = " + parameters + ", records = " + records + "]";
    }
}
