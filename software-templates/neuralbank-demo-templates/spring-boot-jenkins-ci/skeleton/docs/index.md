# ${{values.component_id}} Documentation

${{values.description}}

## Endpoints

- Health: `/actuator/health`
- Prometheus metrics: `/actuator/prometheus`
- AsyncAPI spec (repo): `asyncapi.yaml`

## Async API demo (SSE)

Submit a payment (async, returns **202**):

```bash
curl -sS -X POST "http://<route-host>/api/payments" \
  -H "content-type: application/json" \
  -d '{
    "customerId": "cust-123",
    "currency": "CLP",
    "amount": 12990.50,
    "reference": "invoice-2026-00042"
  }' | jq .
```

Stream lifecycle events via SSE:

```bash
curl -N "http://<route-host>/api/payments/<payment-id>/events"
```

Check status:

```bash
curl -sS "http://<route-host>/api/payments/<payment-id>" | jq .
```

## Observability

This service is pre-configured to export traces and metrics via OTLP/HTTP to the in-cluster collector:

- Traces: `http://dev-collector.observability.svc.cluster.local:4318/v1/traces`
- Metrics: `http://dev-collector.observability.svc.cluster.local:4318/v1/metrics`

You can override these with environment variables:

- `OTEL_EXPORTER_OTLP_TRACES_ENDPOINT`
- `OTEL_EXPORTER_OTLP_METRICS_ENDPOINT`

