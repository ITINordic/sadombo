package zw.org.mohcc.sadombo.lab;

/**
 *
 * @author Charles Chigoriwa
 */
public class DataSetElement {

    private DataElement dataElement;
    private DataSet dataSet;
    private CategoryCombo categoryCombo;

    public DataElement getDataElement() {
        return dataElement;
    }

    public void setDataElement(DataElement dataElement) {
        this.dataElement = dataElement;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public CategoryCombo getCategoryCombo() {
        return categoryCombo;
    }

    public void setCategoryCombo(CategoryCombo categoryCombo) {
        this.categoryCombo = categoryCombo;
    }

    public boolean hasCategoryCombo() {
        return this.categoryCombo != null;
    }

    public CategoryCombo resolvedCategoryCombo() {
        return hasCategoryCombo() ? this.categoryCombo : dataElement.getCategoryCombo();
    }

    @Override
    public String toString() {
        return "DataSetElement{" + "dataElement=" + dataElement + ", dataSet=" + dataSet + ", categoryCombo=" + categoryCombo + '}';
    }

}
