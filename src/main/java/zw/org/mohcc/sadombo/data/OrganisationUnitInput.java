package zw.org.mohcc.sadombo.data;

/**
 *
 * @author Charles Chigoriwa
 */
public class OrganisationUnitInput {

    private String facilityCode;
    private String dhisAuthorization;
    private String parentOpenHIMTranId;

    public OrganisationUnitInput() {
    }

    public OrganisationUnitInput(String facilityCode, String dhisAuthorization, String parentOpenHIMTranId) {
        this.facilityCode = facilityCode;
        this.dhisAuthorization = dhisAuthorization;
        this.parentOpenHIMTranId = parentOpenHIMTranId;
    }

    public String getFacilityCode() {
        return facilityCode;
    }

    public void setFacilityCode(String facilityCode) {
        this.facilityCode = facilityCode;
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
