package zw.org.mohcc.sadombo.data;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author Charles Chigoriwa
 */
public class CategoryCombo {

    private String id;
    private String name;
    private String code;
    private List<Category> categories;
    private List<CategoryOptionCombo> categoryOptionCombos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<CategoryOptionCombo> getCategoryOptionCombos() {
        return categoryOptionCombos;
    }

    public void setCategoryOptionCombos(List<CategoryOptionCombo> categoryOptionCombos) {
        this.categoryOptionCombos = categoryOptionCombos;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final CategoryCombo other = (CategoryCombo) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "CategoryCombo{" + "id=" + id + ", name=" + name + ", code=" + code + ", categories=" + categories + '}';
    }

}
