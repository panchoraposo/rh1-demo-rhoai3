# 🚀 RH1 Demo

This repository contains a comprehensive automation suite to deploy a fully integrated Internal Developer Platform (IDP) on Red Hat OpenShift. Using Ansible and GitOps principles, it provisions the entire lifecycle of the platform, from storage and identity to developer portals, service mesh, and advanced API management.

## 🏗 The Stack

This demo orchestrates the following enterprise components:

| Category | Component | Description |
|---|---|---|
| **Orchestration** | OpenShift GitOps (Argo CD) | Manages the lifecycle of all applications. |
| **Networking** | OpenShift Service Mesh (Istio) | Provides mTLS, traffic management, and a centralized Ingress Gateway. |
| **API Management** | Red Hat Connectivity Link | Delivers Multi-cluster connectivity, Rate Limiting (Limitador), and AuthZ (Authorino). |
| **Identity & Access** | Keycloak | Centralized SSO for the Platform and OIDC provider for Mesh Gateways. |
| **Secret Management** | HashiCorp Vault + ESO | Secure secret storage injected via External Secrets Operator. |
| **DevOps & CI/CD** | GitLab + OpenShift Pipelines | Source code management and CI pipelines. |
| **Developer Portal** | Red Hat Developer Hub | Backstage-based portal for developer self-service. |
| **Registry & Storage** | Quay & ODF | Container registry and OpenShift Data Foundation storage. |
| **Development** | OpenShift Dev Spaces | Cloud-native IDE workspaces. |
| **Operations** | Observability & Logging | Full stack monitoring and log aggregation. |

## 🔐 Traffic & Security Architecture

This demo implements a Zero Trust architecture using the **Gateway API** pattern:

1.  **Ingress Gateway:** All external traffic enters through the **OpenShift Service Mesh (Istio)** Gateway.
    
2.  **Connectivity Link:** **Red Hat Connectivity Link** (powered by Kuadrant) attaches policies to the Gateway:
    
    -   **Authentication:** Validates JWTs via **Keycloak** (OIDC).
        
    -   **Rate Limiting:** Protects endpoints from abuse using global and local limits.
        
3.  **Identity:** **Keycloak** acts as the central Identity Provider (IdP), issuing tokens for users (US/EU/LATAM groups) which are validated at the edge by Authorino (part of Connectivity Link) before reaching the microservices.
    

## 🛠 Prerequisites

Before running the installation, ensure you have:

1.  **OpenShift Cluster:** Access to an OCP 4.20 cluster with `cluster-admin` privileges.
    
2.  **System Tools:**
    
    -   Python 3.9+
        
    -   `oc` CLI
        
    -   `ansible` (Core)
        
3.  **Configuration:** Ensure your `vars/demo.yaml` is populated with your specific domain and token details.
    

## ⚙️ Installation Guide

The installation process is encapsulated in a Python virtual environment to ensure dependency consistency.

### 1. Clone the Repository

Bash

Bash

```
git clone https://github.com/panchoraposo/rh1-demo-rhoai3.git
cd rh1-demo-rhoai3

```

### 2. Setup Virtual Environment

Create and activate a clean Python environment to avoid conflicts.

**For Bash/Zsh:**

Bash

Bash

```
python3 -m venv venv
source venv/bin/activate

```

**For Fish Shell:**

Bash

Bash

```
python3 -m venv venv
source venv/bin/activate.fish

```

### 3. Install Dependencies & Run

Once the environment is active, execute the installation wrapper. This script will install required Ansible collections and python libraries before launching the playbooks.

Bash

Bash

```
./install.sh

```

> **☕ Note:** This is a full-stack deployment. The complete provisioning process may take **~45-60 minutes** depending on your cluster's resources and internet connection.