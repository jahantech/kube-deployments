Main Features:
- Runs a pod per node
- Pod gets added to the new node automically
- Typically used for Monitoring, glcusterd, ceph, logstash and fluentd
- Prometheous node-exporter is one of the typical examples for daemonsets

Scheduling:
Pods of daemonsets are not scheduled by kubernetes scheduler because .spec.nodeName field is specified when the Pod is created so it is ignored by the scheduler
Communication:

Updating Process:
