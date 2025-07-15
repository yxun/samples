# OTEL Tempo Tracing Integration

This example shows steps of deploying a tracing stack with Tempo and OpenTelemetry collector on an OpenShift cluster.

### Create namespaces

```
oc new-project istio-system
oc new-project tracing-system
oc new-project servlet
```

### Install operators from console OperatorHub

Install the following operators with their default values:

- OSSM 2.6
- Kiali
- Tempo Operator
- Red Hat build of OpenTelemetry

### Create a Tempo Stack

1. The Tempo Stack instance needs storage setup first. You can apply the following manifests for setting a minio storage and then create a Tempo Stack instance in the `tracing-system` namespace.

```
oc apply -n tracing-system -f manifests/minio-storage.yaml
oc apply -n tracing-system -f manifests/sample-secret.yaml 
```

2. Create a TempoStack instance when the minio pod is running.
You can create a TempoStack from the TempoOperator with its default settings in the `tracing-system` namespace.

You should wait for all pods running in Ready state before accessing the Tempo dashboard from Networking Routes `tempo-sample-query-frontend`. Otherwise, you may hit a HTTP 401 race condition in the Tempo dashboard.

### Create a Service Mesh Control Plane resource

Create an OSSM 2.6 Control Plane resource in `istio-system` namespace.

```
kind: ServiceMeshControlPlane
apiVersion: maistra.io/v2
metadata:
  name: basic
  namespace: istio-system
spec:
  version: v2.6
  policy:
    type: Istiod
  telemetry:
    type: Istiod
  addons:
    prometheus:
      enabled: true
    kiali:
      enabled: true
    grafana:
      enabled: true
  meshConfig:
    extensionProviders:
    - name: otel
      opentelemetry:
        port: 4317
        service: otel-collector.istio-system.svc.cluster.local
```

Then you can create a Service Mesh Member Roll

```
kind: ServiceMeshMemberRoll
apiVersion: maistra.io/v1
metadata:
  name: default
  namespace: istio-system
spec:
  members:
    - servlet
```

### Deploy a sample application

```
oc apply -n servlet -f https://raw.githubusercontent.com/yxun/samples/refs/heads/main/docker-java-servlets/manifests/my-servlet.yaml
oc apply -n servlet -f https://raw.githubusercontent.com/yxun/samples/refs/heads/main/docker-java-servlets/manifests/my-servlet-gateway.yaml
```

To visit your app page go to:

```
GATEWAY_URL=$(oc get route -n istio-system istio-ingressgateway -o jsonpath='{.spec.host}')
echo "http://${GATEWAY_URL}/helloservlet/sayhello"
```

### Create an OTEL collector in the istio-system namespace

```
oc apply -n istio-system -f manifests/otel.yaml
```

### Start sending traces

Apply a Telemetry resource and then send requests to the app page.

```
oc apply -n istio-system -f manifests/telemetry.yaml
```

You can access the tracing UI dashboard from:

```
TEMPO_URL=$(oc get route -n tracing-system tempo-sample-query-frontend -o jsonpath='{.spec.host}')
echo "https://${TEMPO_URL}"
```
