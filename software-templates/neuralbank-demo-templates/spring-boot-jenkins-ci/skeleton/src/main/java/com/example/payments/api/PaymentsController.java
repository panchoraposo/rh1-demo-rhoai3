package com.example.payments.api;

import com.example.payments.core.PaymentService;
import com.example.payments.model.PaymentAccepted;
import com.example.payments.model.PaymentEvent;
import com.example.payments.model.PaymentRequest;
import com.example.payments.model.PaymentStatus;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/payments")
public class PaymentsController {
  private final PaymentService svc;

  public PaymentsController(PaymentService svc) {
    this.svc = svc;
  }

  @PostMapping
  public Mono<ResponseEntity<PaymentAccepted>> submit(
      @RequestBody PaymentRequest req,
      ServerWebExchange exchange
  ) {
    String baseUrl = exchange.getRequest().getURI().getScheme() + "://" + exchange.getRequest().getURI().getAuthority();
    return svc.submit(req, baseUrl).map(acc ->
        ResponseEntity.accepted()
            .location(URI.create(acc.statusUrl()))
            .body(acc)
    );
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<PaymentStatus>> status(@PathVariable String id) {
    return svc.getStatus(id)
        .map(ResponseEntity::ok)
        .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
  }

  @GetMapping(value = "/{id}/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<ServerSentEvent<PaymentEvent>> events(@PathVariable String id) {
    return svc.events(id)
        .map(evt -> ServerSentEvent.<PaymentEvent>builder()
            .event(evt.type())
            .id(evt.paymentId())
            .data(evt)
            .build()
        );
  }
}

