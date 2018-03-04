package zw.org.mohcc.sadombo.data;

/**
 *
 * @author Charles
 */
public class OrganisationUnitOutput {

    private OrganisationUnit organisationUnit;

    public OrganisationUnitOutput() {
    }

    public OrganisationUnitOutput(OrganisationUnit organisationUnit) {
        this.organisationUnit = organisationUnit;
    }

    public OrganisationUnit getOrganisationUnit() {
        return organisationUnit;
    }

    public void setOrganisationUnit(OrganisationUnit organisationUnit) {
        this.organisationUnit = organisationUnit;
    }

    public boolean hasOrganisationUnit() {
        return this.organisationUnit != null;
    }

}
