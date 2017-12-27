package zw.org.mohcc.sadombo.mapper;

import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author Charles Chigoriwa
 */
public class RequestTarget {

    private String relativePath;
    private List<Pair<String, String>> params;
    private String method;

    public RequestTarget() {
    }

    public RequestTarget(String relativePath, List<Pair<String, String>> params, String method) {
        this.relativePath = relativePath;
        this.params = params;
        this.method = method;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public List<Pair<String, String>> getParams() {
        return params;
    }

    public void setParams(List<Pair<String, String>> params) {
        this.params = params;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.relativePath);
        if (this.params != null) {
            hash = 37 * hash + Objects.hashCode(this.params);
        }
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
        final RequestTarget other = (RequestTarget) obj;
        if (!Objects.equals(this.relativePath, other.relativePath)) {
            return false;
        }
        return Objects.equals(this.params, other.params);
    }

}
