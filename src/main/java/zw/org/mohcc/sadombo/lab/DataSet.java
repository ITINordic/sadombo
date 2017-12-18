package zw.org.mohcc.sadombo.lab;

import java.util.List;

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

    @Override
    public String toString() {
        return "DataSet{" + "id=" + id + ", code=" + code + ", periodType=" + periodType + ", categoryCombo=" + categoryCombo + ", dataSetElements=" + dataSetElements + '}';
    }

}
