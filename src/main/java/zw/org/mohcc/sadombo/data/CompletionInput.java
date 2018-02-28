/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zw.org.mohcc.sadombo.data;

/**
 *
 * @author daniel
 */
public class CompletionInput {

    private String facilityId;
    private String dataSetId;
    private String period;

    public CompletionInput() {
    }

    public CompletionInput(String facilityId, String dataSetId, String period) {
        this.facilityId = facilityId;
        this.dataSetId = dataSetId;
        this.period = period;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getDataSetId() {
        return dataSetId;
    }

    public void setDataSetId(String dataSetId) {
        this.dataSetId = dataSetId;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

}
