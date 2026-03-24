#!/usr/bin/env bash
set -euo pipefail

NAMESPACE="${1:-}"
SERVICE="${2:-}"
PORT="${3:-8080}"
PATH_SSE="${4:-/sse}"

if [[ -z "${NAMESPACE}" || -z "${SERVICE}" ]]; then
  echo "Usage: $0 <namespace> <service> [port] [path]" >&2
  echo "Example: $0 customer-service-mcp-dev customer-service-mcp 8080 /sse" >&2
  exit 1
fi

CONCURRENCY="${CONCURRENCY:-10}"
DURATION_SECONDS="${DURATION_SECONDS:-60}"
POD_NAME="${POD_NAME:-sse-loadgen}"

URL="http://${SERVICE}.${NAMESPACE}.svc.cluster.local:${PORT}${PATH_SSE}"

echo "Running SSE load test"
echo "  namespace:   ${NAMESPACE}"
echo "  url:         ${URL}"
echo "  concurrency: ${CONCURRENCY}"
echo "  duration:    ${DURATION_SECONDS}s"

cat <<EOF | oc -n "${NAMESPACE}" apply -f -
apiVersion: v1
kind: Pod
metadata:
  name: ${POD_NAME}
  labels:
    app: sse-loadgen
spec:
  restartPolicy: Never
  containers:
    - name: loadgen
      image: registry.access.redhat.com/ubi9/ubi-minimal:9.5
      command:
        - /bin/bash
        - -lc
        - |
          set -euo pipefail
          microdnf -y install curl coreutils >/dev/null
          echo "Starting ${CONCURRENCY} SSE connections to ${URL}"
          end=$((SECONDS + ${DURATION_SECONDS}))
          while [ $SECONDS -lt $end ]; do
            for i in $(seq 1 ${CONCURRENCY}); do
              (curl -sS -N --max-time 15 --connect-timeout 3 "${URL}" >/dev/null 2>&1 || true) &
            done
            sleep 1
          done
          wait || true
          echo "Done"
EOF

echo "Pod created: ${POD_NAME} (check logs with: oc -n ${NAMESPACE} logs -f pod/${POD_NAME})"

