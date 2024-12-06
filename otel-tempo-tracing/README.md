# OTEL Tempo Tracing Integration

This example shows steps of deploying a tracing stack with Tempo and OpenTelemetry collector on an OpenShift cluster.

### Create namespaces

```
oc new-project istio-system
oc new-project istio-cni
oc new-project tracing-system
oc new-project bookinfo
```

### Install operators from console OperatorHub

Install the following operators with their default values:

- OSSM 3
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

### Create a Service Mesh Istio and IstioCNI resource

Create an OSSM 3 Istio resource in `istio-system` namespace.
Create an OSSM 3 IstioCNI resource in `istio-cni` namespace.

Update the Istio resource spec with the following spec values:

```
spec:
  namespace: istio-system
  values:
    meshConfig:
      discoverySelectors:
        - matchLabels:
            istio-discovery: enabled
      extensionProviders:
      - name: otel
        opentelemetry:
          port: 4317
          service: otel-collector.istio-system.svc.cluster.local
```

Then you can label namespace.

```
oc label namespace istio-system istio-discovery=enabled
oc label namespace bookinfo istio-discovery=enabled istio-injection=enabled
```

### Deploy a bookinfo sample application

```
oc apply -n bookinfo -f https://raw.githubusercontent.com/istio/istio/master/samples/bookinfo/platform/kube/bookinfo.yaml
```

Create a Gateway using Kubernetes Gateway API

```
oc get crd gateways.gateway.networking.k8s.io &> /dev/null ||  { oc kustomize "github.com/kubernetes-sigs/gateway-api/config/crd?ref=v1.0.0" | oc apply -f -; }
oc apply -n bookinfo -f https://raw.githubusercontent.com/istio/istio/master/samples/bookinfo/gateway-api/bookinfo-gateway.yaml

export INGRESS_HOST=$(oc get gtw bookinfo-gateway -o jsonpath='{.status.addresses[0].value}')
export INGRESS_PORT=$(oc get gtw bookinfo-gateway -o jsonpath='{.spec.listeners[?(@.name=="http")].port}')
export GATEWAY_URL=$INGRESS_HOST:$INGRESS_PORT
echo "http://${GATEWAY_URL}/productpage"

```

### Create an OTEL collector in the istio-system namespace

```
oc apply -n istio-system -f manifests/otel.yaml
```

### Start sending traces

Apply a Telemetry resource and then send requests to bookinfo productpage.

```
oc apply -n istio-system -f manifests/telemetry.yaml
```
