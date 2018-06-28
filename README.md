## Sample full stack ToDo application in Kotlin

Can be easily packaged into Helm chart and ready to be deployed into Kubernetes cluster.

Backend: ktor, exposed

Frontend: kotlin-react, kotlin-js-wrappers

Deployment: docker, helm, kubernetes

## Build and deploy using `minikube`

All the commands below use FISH shell syntax on macOS

### Build project and docker image 

`$ ./gradlew build`

`$ docker build -t todoapp:v0.0.3 .`

### (Optionally) Run inside `minikube`'s VM

`$ eval (minikube docker-env)`

`$ docker run -m512M --cpus 2 -it -p 9090:9090 --rm todoapp:v0.0.1`

`$ open http://(minikube ip):9090/`

### Install `helm`

`$ brew install helm`

`$ helm init`

### Deploy application into Kubernetes cluster (`minikube`)

Use `helm` to install.

* Update dependencies (copy PostgreSQL chart into charts)

`$ helm update dependencies helm-chart/todoapp`

* Package chart into archive

`$ helm package helm-chart/todoapp`

* Install package into Kubernetes

`$ helm install todoapp-0.0.3.tgz`

* Setup port forwarding using command provided in `helm install` output. Something similar to:

```
  export SERVICE_NAME=(kubectl get service --namespace default -l "app=todoapp,release=geared-marmot" -o jsonpath="{.items[0].metadata.name}")
  echo "Visit http://127.0.0.1:8080 to use your application"
  kubectl port-forward svc/$SERVICE_NAME 8080:9090
```

### Check if replication is working as expected

* Kill one or both pods

`$ kubectl delete pod <app-pod-id>`

* Check if new instance is automatically deployed

`$ kubectl get pods`

* Check if DB and data is still there from UI

### Useful debugging commands

Get status of helm installation

`$ helm status <installation-name>`

List helm installations

`$ helm list`

Delete helm installation

`$ helm delete <installation-name>`

List kubernetes pods

`$ kubectl get pods`

Get logs from the pod

`$ kubectl logs <pod>`

### TODOs

* Tests
* LoadBalancer in front of app pods
* Improve styling of Web UI
* Check if kotlin multiplatform project will allow for better code reuse between backend and frontend
* Minify JS in production build
* Do not used development server in production build
* Gradle build scripts to build docker image and helm package