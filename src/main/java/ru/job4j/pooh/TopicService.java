package ru.job4j.pooh;

import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    private final Map<String, Map<String, ConcurrentLinkedQueue<String>>> queue = new ConcurrentHashMap<>();
    @Override
    public Resp process(Req req) {
        Resp resp = new Resp("", "204");
        final String sourceName = req.getSourceName();
        if ("GET".equals(req.httpRequestType())) {
            queue.putIfAbsent(sourceName, new ConcurrentHashMap<>());
            Queue<String> topic = queue.get(sourceName).putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
            String responseText = "";
            if (topic != null) {
                responseText = topic.poll();
            }
            resp = new Resp(Optional.ofNullable(responseText).orElse(""), "200");
        } else if ("POST".equals(req.httpRequestType())) {
            var topic = queue.getOrDefault(sourceName, new ConcurrentHashMap<>());
            if (!topic.isEmpty()) {
                queue.get(sourceName).values().forEach((topicQueue) -> topicQueue.add(req.getParam()));
            }
            resp = new Resp(req.getParam(), "200");
        }
        return resp;
    }
}