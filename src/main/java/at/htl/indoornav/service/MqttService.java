package at.htl.indoornav.service;

import at.htl.indoornav.entity.Node;
import at.htl.indoornav.repository.NodeRepository;
import at.htl.indoornav.repository.PathRepository;
import io.smallrye.reactive.messaging.annotations.Merge;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class MqttService {

    @Inject
    NodeRepository nodeRepository;
    @Inject
    PathRepository pathRepository;

    Jsonb jsonb;

    public MqttService() {
        jsonb = JsonbBuilder.create();
    }

    @Outgoing("path")
    public PublisherBuilder<byte[]> wakeUpCall() {
        byte[] test = "WAKE UP!".getBytes();
        return ReactiveStreams.of(test);
    }

    @Incoming("path")
    @Outgoing("send-path")
    @Merge
    public PublisherBuilder<Path> processValid(byte[] raw) {
        String data = new String(raw);
        Path path = null;
        try {
            path = jsonb.fromJson(data, Path.class);
            if (!path.isValid()) {
                path = null;
            }
        } catch (JsonbException ignored) {
        }

        return ReactiveStreams.ofNullable(path);
    }

    @Incoming("send-path")
    @Outgoing("topic-path")
    public CompletionStage<String> sendPath(Path path) {
        return CompletableFuture.supplyAsync(() -> {
            Node start = nodeRepository.getNode(path.getStart());
            Node end = nodeRepository.getNode(path.getEnd());

            if (start == null || end == null) {
                return JsonArray.EMPTY_JSON_ARRAY.toString();
            }
            return pathRepository.getShortestPath(start, end).toString();
        });
    }
}
