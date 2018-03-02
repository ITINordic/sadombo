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
    private String dhisAuthorization;
    private String parentOpenHIMTranId;

    public CompletionInput() {
    }

    public CompletionInput(String facilityId, String dataSetId, String period, String dhisAuthorization, String parentOpenHIMTranId) {
        this.facilityId = facilityId;
        this.dataSetId = dataSetId;
        this.period = period;
        this.dhisAuthorization = dhisAuthorization;
        this.parentOpenHIMTranId = parentOpenHIMTranId;
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

    public String getDhisAuthorization() {
        return dhisAuthorization;
    }

    public void setDhisAuthorization(String dhisAuthorization) {
        this.dhisAuthorization = dhisAuthorization;
    }

    public String getParentOpenHIMTranId() {
        return parentOpenHIMTranId;
    }

    public void setParentOpenHIMTranId(String parentOpenHIMTranId) {
        this.parentOpenHIMTranId = parentOpenHIMTranId;
    }

}
