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
        String method = request.getMethod();
        return new RequestTarget(relativePath, params, method);
    }

}
