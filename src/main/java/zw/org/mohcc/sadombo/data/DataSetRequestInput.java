package zw.org.mohcc.sadombo.data;

/**
 *
 * @author Charles Chigoriwa
 */
public class DataSetRequestInput {

    private String dataSetCode;
    private String dhisAuthorization;
    private String parentOpenHIMTranId;

    public DataSetRequestInput() {
    }

    public DataSetRequestInput(String dataSetCode, String dhisAuthorization, String parentOpenHIMTranId) {
        this.dataSetCode = dataSetCode;
        this.dhisAuthorization = dhisAuthorization;
        this.parentOpenHIMTranId = parentOpenHIMTranId;
    }

    public String getDataSetCode() {
        return dataSetCode;
    }

    public void setDataSetCode(String dataSetCode) {
        this.dataSetCode = dataSetCode;
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
