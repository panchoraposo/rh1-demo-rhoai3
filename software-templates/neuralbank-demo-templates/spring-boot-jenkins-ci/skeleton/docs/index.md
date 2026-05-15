# ${{values.component_id}} Documentation

${{values.description}}

## Endpoints

- Health: `/actuator/health`
- Prometheus metrics: `/actuator/prometheus`

## Observability

This service is pre-configured to export traces and metrics via OTLP/HTTP to the in-cluster collector:

- Traces: `http://dev-collector.observability.svc.cluster.local:4318/v1/traces`
- Metrics: `http://dev-collector.observability.svc.cluster.local:4318/v1/metrics`

You can override these with environment variables:

- `OTEL_EXPORTER_OTLP_TRACES_ENDPOINT`
- `OTEL_EXPORTER_OTLP_METRICS_ENDPOINT`

