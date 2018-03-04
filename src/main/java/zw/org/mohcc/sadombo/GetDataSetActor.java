package zw.org.mohcc.sadombo;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPResponse;
import org.openhim.mediator.engine.messages.MediatorRequestMessage;
import org.openhim.mediator.engine.messages.SimpleMediatorRequest;
import org.openhim.mediator.engine.messages.SimpleMediatorResponse;
import zw.org.mohcc.sadombo.data.DataSet;
import zw.org.mohcc.sadombo.data.DataSetInput;
import zw.org.mohcc.sadombo.data.DataSetOutput;
import zw.org.mohcc.sadombo.data.DataSetsWrapper;
import zw.org.mohcc.sadombo.utils.ConfigUtility;

/**
 *
 * @author Charles Chigoriwa
 */
public class GetDataSetActor extends UntypedActor {

    private final MediatorConfig config;
    private ResolveDataSetRequest originalRequest;
    private final Channels channels;

    public static class ResolveDataSetRequest extends SimpleMediatorRequest<DataSetInput> {

        public ResolveDataSetRequest(ActorRef requestHandler, ActorRef respondTo, DataSetInput dataSetRequestInput) {
            super(requestHandler, respondTo, dataSetRequestInput);
        }
    }

    public static class ResolveDataSetResponse extends SimpleMediatorResponse<DataSetOutput> {

        public ResolveDataSetResponse(MediatorRequestMessage originalRequest, DataSetOutput dataSetOutput) {
            super(originalRequest, dataSetOutput);
        }
    }

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public GetDataSetActor(MediatorConfig config) {
        this.config = config;
        this.channels = ConfigUtility.getChannels(config);
    }

    private void sendDataSetRequest(ResolveDataSetRequest request) {

        String requestPath = "/api/dataSets";
        originalRequest = request;

        DataSetInput dataSetRequestInput = request.getRequestObject();
        String dataSetCode = dataSetRequestInput.getDataSetCode();
        String dhisAuthorization = dataSetRequestInput.getDhisAuthorization();
        String parentOpenHIMTranId = dataSetRequestInput.getParentOpenHIMTranId();

        List<Pair<String, String>> params = new ArrayList<>();
        params.add(new ImmutablePair<>("filter", "code:eq:" + dataSetCode));
        params.add(new ImmutablePair<>("fields", "id,code,name"));
        params.add(new ImmutablePair<>("paging", "false"));

        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("Authorization", dhisAuthorization);
        headers.put("x-parent-openhim-transaction-id", parentOpenHIMTranId);

        MediatorHTTPRequest serviceRequest = new MediatorHTTPRequest(
                request.getRequestHandler(),
                getSelf(),
                "Get DataSet(DHIS)",
                "GET",
                channels.getDhisChannelScheme(),
                channels.getDhisChannelHost(),
                channels.getDhisChannelPort(),
                channels.getDhisChannelContextPath() + requestPath,
                null,
                headers,
                params
        );

        ActorSelection httpConnector = getContext().actorSelection(config.userPathFor("http-connector"));
        httpConnector.tell(serviceRequest, getSelf());

    }

    private void processDataSetServiceResponse(MediatorHTTPResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DataSetsWrapper dataSetsWrapper = mapper.readValue(response.getBody(), DataSetsWrapper.class);
        DataSet dataSet = null;
        List<DataSet> dataSets = dataSetsWrapper.getDataSets();
        if (dataSets != null && !dataSets.isEmpty()) {
            dataSet = dataSets.get(0);
        }
        ResolveDataSetResponse resolveDataSetResponse = new ResolveDataSetResponse(originalRequest, new DataSetOutput(dataSet));
        originalRequest.getRespondTo().tell(resolveDataSetResponse, getSelf());

    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof ResolveDataSetRequest) {
            log.info("ResolveDataSetRequest");
            sendDataSetRequest((ResolveDataSetRequest) msg);
        } else if (msg instanceof MediatorHTTPResponse) {
            processDataSetServiceResponse((MediatorHTTPResponse) msg);
        } else {
            unhandled(msg);
        }
    }
}
