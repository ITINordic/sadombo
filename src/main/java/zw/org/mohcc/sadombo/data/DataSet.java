package zw.org.mohcc.sadombo.data;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Charles Chigoriwa
 */
public class DataSet {

    private String id;
    private String code;
    private String periodType;
    private CategoryCombo categoryCombo;
    private List<DataSetElement> dataSetElements;
    private List<OrganisationUnit> organisationUnits;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CategoryCombo getCategoryCombo() {
        return categoryCombo;
    }

    public void setCategoryCombo(CategoryCombo categoryCombo) {
        this.categoryCombo = categoryCombo;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public List<DataSetElement> getDataSetElements() {
        return dataSetElements;
    }

    public void setDataSetElements(List<DataSetElement> dataSetElements) {
        this.dataSetElements = dataSetElements;
    }

    public List<OrganisationUnit> getOrganisationUnits() {
        return organisationUnits;
    }

    public void setOrganisationUnits(List<OrganisationUnit> organisationUnits) {
        this.organisationUnits = organisationUnits;
    }

    public Set<CategoryCombo> dataSetElementCategoryCombos() {
        Set<CategoryCombo> categoryCombos = new HashSet<>();
        this.getDataSetElements().forEach((dataSetElement) -> {
            categoryCombos.add(dataSetElement.resolvedCategoryCombo());
        });
        return categoryCombos;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DataSet other = (DataSet) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "DataSet{" + "id=" + id + ", code=" + code + ", periodType=" + periodType + ", categoryCombo=" + categoryCombo + ", dataSetElements=" + dataSetElements + '}';
    }

}
