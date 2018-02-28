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
public class Group {

    private String orgUnit;
    private String dataSet;
    private String period;

    public Group() {
    }

    public Group(String orgUnit, String dataSet) {
        this.orgUnit = orgUnit;
        this.dataSet = dataSet;
    }

    public String getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(String orgUnit) {
        this.orgUnit = orgUnit;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

}
