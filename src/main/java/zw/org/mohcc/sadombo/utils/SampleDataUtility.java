package zw.org.mohcc.sadombo.utils;

/**
 *
 * @author Charles Chigoriwa
 */
public class SampleDataUtility {

    public static String getAXDSampleData() {
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

}
