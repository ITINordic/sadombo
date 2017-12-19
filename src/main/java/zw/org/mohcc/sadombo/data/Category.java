package zw.org.mohcc.sadombo.data;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author Charles Chigoriwa
 */
public class Category {

    private String id;
    private String name;
    private String code;
    private List<CategoryOption> categoryOptions;

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

    public List<CategoryOption> getCategoryOptions() {
        return categoryOptions;
    }

    public void setCategoryOptions(List<CategoryOption> categoryOptions) {
        this.categoryOptions = categoryOptions;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
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
        final Category other = (Category) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Category{" + "id=" + id + ", name=" + name + ", code=" + code + ", categoryOptions=" + categoryOptions + '}';
    }

}
