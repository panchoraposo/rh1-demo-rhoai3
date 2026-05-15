package com.example.payments.core;

import com.example.payments.model.PaymentAccepted;
import com.example.payments.model.PaymentEvent;
import com.example.payments.model.PaymentRequest;
import com.example.payments.model.PaymentStatus;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class PaymentService {
  private final Map<String, PaymentStatus> statuses = new ConcurrentHashMap<>();
  private final Map<String, Sinks.Many<PaymentEvent>> sinks = new ConcurrentHashMap<>();
  private final Random rnd = new Random();

  public Mono<PaymentAccepted> submit(PaymentRequest req, String baseUrl) {
    Objects.requireNonNull(req, "request");

    String id = UUID.randomUUID().toString();
    Instant now = Instant.now();

    statuses.put(id, new PaymentStatus(id, "ACCEPTED", now, null, null));
    Sinks.Many<PaymentEvent> sink = Sinks.many().multicast().directBestEffort();
    sinks.put(id, sink);
    sink.tryEmitNext(new PaymentEvent("PAYMENT_ACCEPTED", id, now, Map.of(
        "customerId", req.customerId(),
        "amount", req.amount(),
        "currency", req.currency(),
        "reference", req.reference()
    )));

    // Simulate async processing with a small event timeline.
    Flux.just(
            Duration.ofSeconds(1),
            Duration.ofSeconds(2),
            Duration.ofSeconds(3)
        )
        .concatMap(delay -> Mono.delay(delay))
        .doOnNext(i -> progress(id))
        .doFinally(sig -> complete(id))
        .subscribe();

    return Mono.just(new PaymentAccepted(
        id,
        "ACCEPTED",
        baseUrl + "/api/payments/" + id,
        baseUrl + "/api/payments/" + id + "/events"
    ));
  }

  public Mono<PaymentStatus> getStatus(String id) {
    PaymentStatus st = statuses.get(id);
    if (st == null) {
      return Mono.empty();
    }
    return Mono.just(st);
  }

  public Flux<PaymentEvent> events(String id) {
    Sinks.Many<PaymentEvent> sink = sinks.get(id);
    if (sink == null) {
      return Flux.empty();
    }
    return sink.asFlux();
  }

  private void progress(String id) {
    PaymentStatus current = statuses.get(id);
    if (current == null) return;
    if (current.state().equals("COMPLETED")) return;

    Instant now = Instant.now();
    statuses.put(id, new PaymentStatus(id, "PROCESSING", now, null, current.riskLevel()));
    emit(id, "PAYMENT_PROCESSING", now, Map.of("step", "risk-scoring"));

    String risk = pickRisk();
    statuses.put(id, new PaymentStatus(id, "PROCESSING", now, null, risk));
    emit(id, "RISK_SCORED", now, Map.of("riskLevel", risk));
  }

  private void complete(String id) {
    PaymentStatus current = statuses.get(id);
    if (current == null) return;

    Instant now = Instant.now();
    String risk = Optional.ofNullable(current.riskLevel()).orElse("UNKNOWN");
    String decision = decide(risk);

    statuses.put(id, new PaymentStatus(id, "COMPLETED", now, decision, risk));
    emit(id, "PAYMENT_DECIDED", now, Map.of("decision", decision, "riskLevel", risk));
    emit(id, "PAYMENT_COMPLETED", now, Map.of("status", decision));
  }

  private void emit(String id, String type, Instant at, Map<String, Object> data) {
    Sinks.Many<PaymentEvent> sink = sinks.get(id);
    if (sink == null) return;
    sink.tryEmitNext(new PaymentEvent(type, id, at, new HashMap<>(data)));
  }

  private String pickRisk() {
    int x = rnd.nextInt(100);
    if (x < 60) return "LOW";
    if (x < 90) return "MEDIUM";
    return "HIGH";
  }

  private String decide(String risk) {
    return switch (risk) {
      case "LOW" -> "APPROVED";
      case "MEDIUM" -> rnd.nextBoolean() ? "APPROVED" : "REVIEW";
      case "HIGH" -> "DECLINED";
      default -> "REVIEW";
    };
  }
}

