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

### Create a Service Mesh Istio and IstioCNI resource

Create an OSSM 3 Istio resource in `istio-system` namespace.
Create an OSSM 3 IstioCNI resource in `istio-cni` namespace.


### Create a Tempo Stack

1. The Tempo Stack instance needs storage setup first. You can apply the following manifests for setting a minio storage and then create a Tempo Stack instance in the `tracing-system` namespace.

```
oc apply -n tracing-system -f manifests/storage.yaml 
oc apply -n tracing-system -f manifests/sample-secret.yaml 
```

2. Create a Tempo Stack instance after minio pod is running.
You can create a TempoStack from the TempoOperator with its default settings in the `tracing-system` namespace.

You can access the Tempo dashboard from Networking Routes `tempo-sample-query-frontend`

CNI Race condition: 
If you see the `HTTP Error: missing tenant header` 401 error, try to delete only the Tempo Stack and recreate the instance again. 

### Configure Istio resource

Update the Istio resource spec with the following parts

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
          service: otel-collector.bookinfo.svc.cluster.local
```

Then you can label namespace.

```
oc label namespace istio-system istio-discovery=enabled
oc label namespace bookinfo istio-discovery=enabled istio-injection=enabled
```

### Deploy an OTEL collector with bookinfo sample application


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

Create an OTEL collector in the bookinfo namespace.

```
oc apply -n bookinfo -f manifests/otel.yaml
```

### Start sending traces

Apply a Telemetry resource

```
oc apply -n istio-system -f manifests/telemetry.yaml
```
