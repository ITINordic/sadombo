package zw.org.mohcc.sadombo.data;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author Charles Chigoriwa
 */
public class CategoryOptionCombo {

    private String id;
    private CategoryCombo categoryCombo;
    private List<CategoryOption> categoryOptions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CategoryCombo getCategoryCombo() {
        return categoryCombo;
    }

    public void setCategoryCombo(CategoryCombo categoryCombo) {
        this.categoryCombo = categoryCombo;
    }

    public List<CategoryOption> getCategoryOptions() {
        return categoryOptions;
    }

    public void setCategoryOptions(List<CategoryOption> categoryOptions) {
        this.categoryOptions = categoryOptions;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
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
        final CategoryOptionCombo other = (CategoryOptionCombo) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "CategoryOptionCombo{" + "id=" + id + ", categoryCombo=" + categoryCombo + ", categoryOptions=" + categoryOptions + '}';
    }

}
