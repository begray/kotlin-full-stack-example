## Sample full stack ToDo application in Kotlin

Backend: ktor, exposed
Frontend: kotlin-react, kotlin-js-wrappers
Deployment: docker, helm, kubernetes

## Build and deploy

All the commands below use FISH shell syntax on macOS

### Build project and docker image 

`$ ./gradlew build`

`$ docker build -t todoapp:v0.0.3 .`

### (Optionally) Run inside minikube's VM

`$ eval (minikube docker-env)`

`$ docker run -m512M --cpus 2 -it -p 9090:9090 --rm todoapp:v0.0.1`

`$ open http://(minikube ip):9090/`

### Install Helm

`$ brew install helm`

`$ helm init`

### Deploy application into Kubernetes cluster

Use `helm` to install.

* Update dependecies (copy PostgreSQL chart into charts)

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

### Useful debugging commands

Get status of helm installation

`$ helm status <installation-name>`

List helm installations

`$ helm list`

List kubernetes pods

`$ kubectl get pods`

Get logs from the pod

`$ kubectl logs <pod>`