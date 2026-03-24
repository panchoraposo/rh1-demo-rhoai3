# MCP con Cursor (Red Hat Developer Hub)

Esta guía explica cómo configurar **Cursor** para usar el servidor **MCP** expuesto por esta instancia de **Red Hat Developer Hub** y así poder “chatear con el catálogo” y consultar TechDocs desde el IDE.

Está basada en lo publicado por Red Hat en [MCP in Red Hat Developer Hub: Chat with your catalog](https://developers.redhat.com/articles/2025/11/10/mcp-red-hat-developer-hub-chat-your-catalog#configure_your_mcp_client_application).

## Requisitos

- Esta instancia de Developer Hub ya tiene habilitado el endpoint MCP:
  - `GET ${RHDH_HOST}/api/mcp-actions/v1`
- Ya existe un **token estático** configurado en el backend (variable `MCP_TOKEN`).

## 1) Identifica el `RHDH_HOST`

Usa la **misma URL** con la que entras a Developer Hub en el navegador (sin la ruta del catálogo).

Ejemplos:

- Si tu portal es `https://backstage-developer-hub-backstage.apps.<cluster-domain>`, entonces:
  - `RHDH_HOST=https://backstage-developer-hub-backstage.apps.<cluster-domain>`

## 2) Obtén el token estático (`MCP_TOKEN`)

Pídeselo al equipo de plataforma o, si tienes acceso al cluster:

```bash
oc -n backstage get secret backstage-env -o jsonpath='{.data.MCP_TOKEN}' | base64 -d
```

Guárdalo como `${STATIC_TOKEN}` para los pasos siguientes.

## 3) Configura Cursor

Crea/edita el archivo `~/.cursor/mcp.json` con esta configuración (reemplaza variables):

```json
{
  "mcpServers": {
    "backstage-actions": {
      "url": "${RHDH_HOST}/api/mcp-actions/v1",
      "headers": {
        "Authorization": "Bearer ${STATIC_TOKEN}"
      }
    }
  }
}
```

Alternativa: en Cursor, ve a **Settings → Tools and MCP → New MCP Server** y pega el JSON.

## 4) Pruebas rápidas

En Cursor (chat), prueba consultas como:

- “Lista los componentes del catálogo relacionados con *mcp*.”
- “Dame el `catalog-info.yaml` de `test-service-mcp`.”
- “Resume la documentación (TechDocs) de `test-service-mcp`.”

## Referencia

- Artículo de Red Hat: [MCP in Red Hat Developer Hub: Chat with your catalog](https://developers.redhat.com/articles/2025/11/10/mcp-red-hat-developer-hub-chat-your-catalog#configure_your_mcp_client_application)
