package zw.org.mohcc.sadombo.mapper;

import java.util.Map;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;

/**
 *
 * @author Charles Chigoriwa
 */
public abstract class RequestHeaderMapper {

    public abstract Map<String, String> mapHeaders(MediatorHTTPRequest request);

}
