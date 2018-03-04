package zw.org.mohcc.sadombo.data;

/**
 *
 * @author Charles Chigoriwa
 */
public class DataSetOutput {

    private DataSet dataSet;

    public DataSetOutput() {
    }

    public DataSetOutput(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public boolean hasDataSet() {
        return this.dataSet != null;
    }

}
