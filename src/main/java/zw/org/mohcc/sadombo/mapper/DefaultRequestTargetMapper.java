package zw.org.mohcc.sadombo.mapper;

import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;

/**
 *
 * @author Charles
 */
public class DefaultRequestTargetMapper extends RequestTargetMapper {

    @Override
    public RequestTarget getRequestTarget(MediatorHTTPRequest request) {
        String relativePath = getRelativePath(request);
        List<Pair<String, String>> params = request.getParams();
        return new RequestTarget(relativePath, params, request.getMethod());
    }

    protected String getRelativePath(MediatorHTTPRequest request) {
        String requestPath = request.getPath();
        String relativePath = "/";
        int firstIndexOf = requestPath.indexOf("/");
        if (firstIndexOf >= 0 && requestPath.length() >= firstIndexOf + 2) {
            int secondIndexOf = requestPath.indexOf("/", firstIndexOf + 1);
            if (secondIndexOf > firstIndexOf) {
                relativePath = requestPath.substring(secondIndexOf);
            }
        }
        return relativePath;
    }

}
