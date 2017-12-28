package zw.org.mohcc.sadombo.mapper;

import java.util.HashMap;
import java.util.Map;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import static zw.org.mohcc.sadombo.utils.GeneralUtility.copyHeader;

/**
 *
 * @author Charles Chigoriwa
 */
public class DefaultRequestHeaderMapper extends RequestHeaderMapper {

    @Override
    public Map<String, String> mapHeaders(MediatorHTTPRequest request) {
        Map<String, String> headers = request.getHeaders();
        Map<String, String> newHeaders = new HashMap<>();
        String[] headerNames = {"accept", "content-type", "x-openhim-transactionid", "x-forwarded-for", "x-forwarded-host"};
        for (String headerName : headerNames) {
            copyHeader(headerName, headers, newHeaders);
        }
        return newHeaders;
    }

}
