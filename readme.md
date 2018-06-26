## To Do list application in Kotlin ##

### Build and run docker image locally

`./gradlew build`
`$ docker build -t todoapp .`
`$ docker run -m512M --cpus 2 -it -p 9090:9090 --rm todoapp`

### Running inside minikube's VM

In FISH shell:

`$ eval (minikube docker-env)`
`$ docker run -m512M --cpus 2 -it -p 9090:9090 --rm todoapp`
`$ open http://(minikube ip):9090/`
