#!/usr/bin/env bash
set -euo pipefail

NAMESPACE="${1:-}"
if [[ -z "${NAMESPACE}" ]]; then
  echo "Usage: $0 <namespace>" >&2
  exit 1
fi

WAYPOINT_NAME="${WAYPOINT_NAME:-waypoint}"

echo "Onboarding namespace to Istio ambient: ${NAMESPACE}"

oc get ns "${NAMESPACE}" >/dev/null

# Enroll namespace in ambient dataplane mode
oc label ns "${NAMESPACE}" istio.io/dataplane-mode=ambient --overwrite

# Create a namespace waypoint (Gateway API) and enroll namespace to use it
cat <<EOF | oc apply -f -
apiVersion: gateway.networking.k8s.io/v1
kind: Gateway
metadata:
  name: ${WAYPOINT_NAME}
  namespace: ${NAMESPACE}
  labels:
    istio.io/waypoint-for: service
spec:
  gatewayClassName: istio-waypoint
  listeners:
    - name: mesh
      port: 15008
      protocol: HBONE
EOF

oc label ns "${NAMESPACE}" "istio.io/use-waypoint=${WAYPOINT_NAME}" --overwrite

echo "Done."

