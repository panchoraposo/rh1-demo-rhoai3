{{- define "app.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "app.labels" -}}
app.kubernetes.io/name: {{ include "app.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.openshift.io/runtime: quarkus
backstage.io/kubernetes-id: {{ include "app.name" . }}
{{- end -}}

{{- define "app.image" -}}
{{- if eq .Values.image.registry "Quay" -}}
  {{- if .Values.image.digest -}}
    {{- printf "%s/%s/%s@%s" .Values.image.host .Values.image.organization .Values.image.name .Values.image.digest -}}
  {{- else -}}
    {{- printf "%s/%s/%s:%s" .Values.image.host .Values.image.organization .Values.image.name .Values.image.tag -}}
  {{- end -}}
{{- else -}}
  {{- if .Values.image.digest -}}
    {{- printf "%s/%s/%s@%s" .Values.image.host .Values.image.namespace .Values.image.name .Values.image.digest -}}
  {{- else -}}
    {{- printf "%s/%s/%s:%s" .Values.image.host .Values.image.namespace .Values.image.name .Values.image.tag -}}
  {{- end -}}
{{- end -}}
{{- end -}}

