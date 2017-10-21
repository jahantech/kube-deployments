# Kubernetes Cheat Sheet

|Description| Command  |
|---|---|
|List pods all namespaces  | kubectl get pods --all-namespaces  |
|Force delete pod   | kubectl -n NAMESPACE delete pod --grace-period=0 --force   |
|Delete a node   | kubectl delete NODENAME  |
|Drain a node | kubectl drain NODENAME|

