### Sample full stack ToDo application in Kotlin

Use `helm` to install.

* Copy PostgreSQL chart into charts

`$ helm update dependencies todoapp`

* Package chart into archive

`$ helm package todoapp`

* Install package into Kubernetes

`$ helm install todoapp-0.0.2.tgz`