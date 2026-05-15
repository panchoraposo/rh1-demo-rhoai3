#!/usr/bin/env bash
set -euo pipefail

export KUBECONFIG="${KUBECONFIG:-$HOME/.kube/config}"
export K8S_AUTH_CONTEXT="${K8S_AUTH_CONTEXT:-previred}"

ansible-playbook -i localhost, -c local ./ansible/playbooks/install.yaml