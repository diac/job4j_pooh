package ru.job4j.pooh;

import java.util.HashMap;
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
            queue.putIfAbsent(sourceName, new HashMap<>());
            var responseText = "";
            Queue<String> topicQueue = queue.get(sourceName)
                    .getOrDefault(req.getParam(), new ConcurrentLinkedQueue<>());
            if (queue.get(sourceName).putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>()) == null) {
                responseText = topicQueue.peek();
            } else {
                responseText = topicQueue.poll();
            }
            resp = new Resp(Optional.ofNullable(responseText).orElse(""), "200");
        } else if ("POST".equals(req.httpRequestType())) {
            if (queue.containsKey(sourceName)) {
                queue.get(sourceName).values().forEach((topicQueue) -> topicQueue.add(req.getParam()));
            }
            resp = new Resp(req.getParam(), "200");
        }
        return resp;
    }
}