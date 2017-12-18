package zw.org.mohcc.sadombo.lab;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Charles Chigoriwa
 */
public class AdxFactory {

    public static void main(String[] args) throws IOException {
        System.out.println(getAdxXsd("ATB_005"));
    }

    public static String getAdxXsd(String dataSetCode) throws IOException {
        DataSet dataSet = DhisClient.getDataSetByCode(dataSetCode);
        if (dataSet == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<xs:schema xmlns=\"urn:ihe:qrph:adx:2015\" xmlns:common=\"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:str=\"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/structure\" targetNamespace=\"urn:ihe:qrph:adx:2015\" elementFormDefault=\"qualified\">").append("\n");
        sb.append("<xs:import namespace=\"http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common\" schemaLocation=\"sdmx/SDMXCommon.xsd\"/>").append("\n");

        sb.append("<xs:simpleType name=\"CL_OrgUnits_ZW_MOHCC_1.0_Type\">").append("\n");
        sb.append(" <xs:restriction base=\"xs:token\">").append("\n");
        dataSet.getOrganisationUnits().forEach((organizationUnit) -> {
            if (organizationUnit.getCode() != null && !organizationUnit.getCode().trim().isEmpty()) {
                sb.append("<xs:enumeration value=\"").append(organizationUnit.getCode()).append("\"/>").append("\n");
            }
        });
        sb.append("</xs:restriction>").append("\n");
        sb.append("</xs:simpleType>").append("\n");

        sb.append("<xs:simpleType name=\"CL_DataElements_ZW_MOHCC_1.0_Type\">").append("\n");
        sb.append(" <xs:restriction base=\"xs:token\">").append("\n");
        for (DataSetElement dataSetElement : dataSet.getDataSetElements()) {
            DataElement dataElement = dataSetElement.getDataElement();
            if (dataElement.getCode() != null && !dataElement.getCode().trim().isEmpty()) {
                sb.append("<xs:enumeration value=\"").append(dataElement.getCode()).append("\"/>").append("\n");
            }
        }
        sb.append("</xs:restriction>").append("\n");
        sb.append("</xs:simpleType>").append("\n");

        Set<CategoryCombo> inflatedDataElementCategoryCombos = new HashSet<>();

        for (CategoryCombo categoryCombo : dataSet.dataSetElementCategoryCombos()) {
            inflatedDataElementCategoryCombos.add(DhisClient.getCategoryComboById(categoryCombo.getId()));
        }

        Set<Category> categories = getCategories(inflatedDataElementCategoryCombos);

        for (Category category : categories) {
            if (category.getCode() != null && !category.getCode().isEmpty()) {
                sb.append("<xs:simpleType name=\"").append(getClassTypeName(category.getCode())).append("\">").append("\n");
                sb.append("<xs:restriction base=\"xs:token\">").append("\n");
                for (CategoryOption categoryOption : category.getCategoryOptions()) {
                    if (categoryOption.getCode() != null && !categoryOption.getCode().isEmpty()) {
                        sb.append("<xs:enumeration value=\"").append(categoryOption.getCode()).append("\"/>").append("\n");
                    }
                }
                sb.append("</xs:restriction>").append("\n");

                sb.append("</xs:simpleType>").append("\n");
            }
        }

        sb.append("<xs:simpleType name=\"periodType\">").append("\n");
        sb.append("<xs:restriction base=\"common:TimeRangeType\"/>").append("\n");
        sb.append("</xs:simpleType>").append("\n");

        sb.append("<xs:complexType name=\"adxType\">").append("\n");
        sb.append("<xs:sequence maxOccurs=\"unbounded\">").append("\n");
        sb.append("<xs:element name=\"group\" type=\"groupType\"/>").append("\n");
        sb.append("</xs:sequence>").append("\n");
        sb.append("<xs:anyAttribute processContents=\"skip\"/>").append("\n");
        sb.append("</xs:complexType>").append("\n").append("\n");

        sb.append("<xs:complexType name=\"groupType\">").append("\n");
        sb.append("<xs:sequence maxOccurs=\"unbounded\">").append("\n");
        sb.append("<xs:element name=\"dataValue\" type=\"DataValueType\"/>").append("\n");
        sb.append("</xs:sequence>").append("\n");
        sb.append("<xs:attribute name=\"dataSet\" use=\"required\" type=\"xs:string\" fixed=\"ATB_005\"/>").append("\n");
        sb.append("<xs:attribute name=\"orgUnit\" use=\"required\" type=\"CL_OrgUnits_ZW_MOHCC_1.0_Type\"/>").append("\n");
        sb.append("<xs:attribute name=\"period\" use=\"required\" type=\"periodType\"/>").append("\n");
        sb.append("<xs:anyAttribute processContents=\"skip\"/>").append("\n");
        sb.append("</xs:complexType>").append("\n");

        sb.append("<xs:complexType name=\"DataValueType\">").append("\n");
        sb.append("<xs:sequence maxOccurs=\"1\" minOccurs=\"0\">").append("\n");
        sb.append("<xs:element name=\"annotation\"/>").append("\n");
        sb.append("</xs:sequence>").append("\n");
        sb.append("<xs:attribute name=\"dataElement\" use=\"required\" type=\"CL_DataElements_ZW_MOHCC_1.0_Type\"/>").append("\n");
        sb.append("<xs:attribute name=\"value\" use=\"required\" type=\"xs:decimal\"/>").append("\n");

        for (Category category : categories) {
            if (category.getCode() != null && !category.getCode().isEmpty()) {
                sb.append("<xs:attribute name=\"").append(category.getCode()).append("\" type=\"").append(getClassTypeName(category.getCode())).append("\" use=\"optional\"/>").append("\n");
            }
        }

        sb.append("<xs:anyAttribute processContents=\"skip\"/>").append("\n");
        sb.append("</xs:complexType>").append("\n");

        sb.append(" <xs:element name=\"adx\" type=\"adxType\"/>").append("\n");

        sb.append("</xs:schema>").append("\n");
        return sb.toString();
    }

    private static Set<Category> getCategories(Set<CategoryCombo> inflatedDataElementCategoryCombos) {
        Set<Category> categories = new HashSet<>();
        inflatedDataElementCategoryCombos.forEach((categoryCombo) -> {
            categories.addAll(categoryCombo.getCategories());
        });
        return categories;
    }

    private static String getClassTypeName(String code) {
        return "CL_" + code + "_ZW_MOHCC_1.0_Type";
    }

}
