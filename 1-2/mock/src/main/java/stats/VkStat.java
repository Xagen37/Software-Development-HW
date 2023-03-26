package stats;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.wall.Wallpost;

import java.util.List;
import java.util.stream.Collectors;

public class VkStat implements Stat {
    private final VkApiClient vkApiClient;
    private final static int APP_ID = MY_ID;
    private final static String SERVICE_KEY = MY_SERVICE_KEY;
    private final ServiceActor serviceActor;


    public VkStat() {
        final TransportClient transportClient = HttpTransportClient.getInstance();
        vkApiClient = new VkApiClient(transportClient);

        serviceActor = new ServiceActor(APP_ID, SERVICE_KEY);
    }

    public List<String> get(String tag, Long startTime, Long endTime) throws ClientException, ApiException {
        return vkApiClient.newsfeed()
        .search(serviceActor)
        .count(200)
        .startTime(startTime.intValue()).endTime(endTime.intValue())
        .q(tag)
        .execute()
        .getItems()
        .stream()
        .map(Wallpost::getText)
        .collect(Collectors.toList());
    }
}
