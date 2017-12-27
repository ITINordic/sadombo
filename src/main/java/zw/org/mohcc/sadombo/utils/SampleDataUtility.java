package zw.org.mohcc.sadombo.utils;

/**
 *
 * @author Charles Chigoriwa
 */
public class SampleDataUtility {

    public static String getAdxSampleData() {
        String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<adx xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:ihe:qrph:adx:2015 ATB_005.xsd\" xmlns=\"urn:ihe:qrph:adx:2015\">\n"
                + "    \n"
                + "    <group dataSet=\"ATB_005\" orgUnit=\"ZW000A59\" period=\"2017-01-01/P1M\" comment=\"EPMS_timestamp\">\n"
                + "        <dataValue dataElement=\"ATB_005_D23\" value=\"32\" ATB_SUMMARY_AGEGRP=\"P0M/P3M\" SEX=\"M\"/>\n"
                + "        <dataValue dataElement=\"ATB_005_D24\" value=\"56\" SEX=\"F\" ATB_SUMMARY_AGEGRP=\"P20Y/P5Y\"/>\n"
                + "    </group>    \n"
                + "    \n"
                + "</adx>";
        return s;
    }

    public static String getAdxSampleResponse() {
        String s = "<?xml version='1.0' encoding='UTF-8'?>\n"
                + "<importSummary \n"
                + "    xmlns=\"http://dhis2.org/schema/dxf/2.0\" responseType=\"ImportSummary\">\n"
                + "    <status>SUCCESS</status>\n"
                + "    <importOptions>\n"
                + "        <idSchemes/>\n"
                + "        <dryRun>false</dryRun>\n"
                + "        <async>false</async>\n"
                + "        <importStrategy>CREATE_AND_UPDATE</importStrategy>\n"
                + "        <mergeMode>REPLACE</mergeMode>\n"
                + "        <reportMode>FULL</reportMode>\n"
                + "        <skipExistingCheck>false</skipExistingCheck>\n"
                + "        <sharing>false</sharing>\n"
                + "        <skipNotifications>false</skipNotifications>\n"
                + "        <datasetAllowsPeriods>false</datasetAllowsPeriods>\n"
                + "        <strictPeriods>false</strictPeriods>\n"
                + "        <strictCategoryOptionCombos>false</strictCategoryOptionCombos>\n"
                + "        <strictAttributeOptionCombos>false</strictAttributeOptionCombos>\n"
                + "        <strictOrganisationUnits>false</strictOrganisationUnits>\n"
                + "        <requireCategoryOptionCombo>false</requireCategoryOptionCombo>\n"
                + "        <requireAttributeOptionCombo>false</requireAttributeOptionCombo>\n"
                + "    </importOptions>\n"
                + "    <description>Import process completed successfully</description>\n"
                + "    <importCount imported=\"0\" updated=\"31\" ignored=\"0\" deleted=\"0\"/>\n"
                + "    <dataSetComplete>false</dataSetComplete>\n"
                + "</importSummary>";
        return s;
    }

    public static String getAdxSampleDataNonXsdCompliant() {
        String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<adx xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:ihe:qrph:adx:2015 ATB_005.xsd\" xmlns=\"urn:ihe:qrph:adx:2015\">\n"
                + "    \n"
                + "    <group dataSet=\"ATB_005\" orgUnit=\"ZW000A59\" period=\"2017-01-01/P1M\" comment=\"EPMS_timestamp\">\n"
                + "        <dataValue dataElement=\"ATB_005_D23\" value=\"32\" ATB_SUMMARY_AGEGRP=\"P0M/P3M\" SEX=\"M\"/>\n"
                + "        <dataValue dataElement=\"ATB_005_D24\" value=\"56\" SEX=\"F\" ATB_SUMMARY_AGEGRP=\"P20Y/P5Y\"/>\n"
                + "         <dataValue1 dataElement=\"ATB_005_D24\" value=\"56\" SEX=\"F\" ATB_SUMMARY_AGEGRP=\"P20Y/P5Y\"/>\n"
                + "    </group>    \n"
                + "    \n"
                + "</adx>";
        return s;
    }

}
