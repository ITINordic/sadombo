package zw.org.mohcc.sadombo.data;

import java.util.Objects;

/**
 *
 * @author Charles Chigoriwa
 */
public class DataElement {

    private String id;
    private String code;
    private String name;
    private CategoryCombo categoryCombo;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryCombo getCategoryCombo() {
        return categoryCombo;
    }

    public void setCategoryCombo(CategoryCombo categoryCombo) {
        this.categoryCombo = categoryCombo;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final DataElement other = (DataElement) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "DataElement{" + "id=" + id + ", code=" + code + ", name=" + name + ", categoryCombo=" + categoryCombo + '}';
    }

}
