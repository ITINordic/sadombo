package zw.org.mohcc.sadombo.mapper;

import java.util.HashMap;
import java.util.Map;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;

/**
 *
 * @author Charles Chigoriwa
 */
public class DefaultRequestHeaderMapper extends RequestHeaderMapper {

    @Override
    public Map<String, String> mapHeaders(MediatorHTTPRequest request) {
        Map<String, String> headers = request.getHeaders();
        Map<String, String> newHeaders = new HashMap<>();
        newHeaders.put("content-type", headers.get("content-type"));
        newHeaders.put("x-openhim-transactionid", headers.get("x-openhim-transactionid"));
        newHeaders.put("x-forwarded-for", headers.get("x-forwarded-for"));
        newHeaders.put("x-forwarded-host", headers.get("x-forwarded-host"));
        return newHeaders;
    }

}
