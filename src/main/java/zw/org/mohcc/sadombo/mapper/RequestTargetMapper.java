package zw.org.mohcc.sadombo.mapper;

import org.openhim.mediator.engine.messages.MediatorHTTPRequest;

/**
 *
 * @author Charles
 */
public abstract class RequestTargetMapper {

    public abstract RequestTarget getRequestTarget(MediatorHTTPRequest request);

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
