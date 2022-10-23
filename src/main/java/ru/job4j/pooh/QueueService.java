package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {

    private final Map<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp resp = new Resp("", "204");
        final String sourceName = req.getSourceName();
        if ("GET".equals(req.httpRequestType())) {
            var sourceQueue = queue.getOrDefault(sourceName, new ConcurrentLinkedQueue<>());
            if (!sourceQueue.isEmpty()) {
                resp = new Resp(sourceQueue.poll(), "200");
            }
        } else if ("POST".equals(req.httpRequestType())) {
            queue.putIfAbsent(sourceName, new ConcurrentLinkedQueue<>());
            if (queue.get(sourceName).add(req.getParam())) {
                resp = new Resp(req.getParam(), "200");
            }
        }
        return resp;
    }
}