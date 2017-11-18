# kube-deployments
My k8s deployment configs for various setups

# k8s cluster with kubeadm
## Master Node
OS: CentOS 7 

Install kuebadm on the master node
``` 
cat <<EOF > /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=https://packages.cloud.google.com/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=1
repo_gpgcheck=1
gpgkey=https://packages.cloud.google.com/yum/doc/yum-key.gpg https://packages.cloud.google.com/yum/doc/rpm-package-key.gpg
EOF
setenforce 0
yum install -y kubelet kubeadm kubectl
systemctl enable kubelet && systemctl start kubelet 
```

Lets try to initialize the k8s cluster

```
[root@k8s-master-1 ~]# kubeadm init 
[kubeadm] WARNING: kubeadm is in beta, please do not use it for production clusters.
[init] Using Kubernetes version: v1.8.3
[init] Using Authorization modes: [Node RBAC]
[preflight] Running pre-flight checks
[preflight] WARNING: firewalld is active, please ensure ports [6443 10250] are open or your cluster may not function correctly
[preflight] WARNING: Running with swap on is not supported. Please disable swap or set kubelet's --fail-swap-on flag to false.
[preflight] Some fatal errors occurred:
        /proc/sys/net/bridge/bridge-nf-call-iptables contents are not set to 1
[preflight] If you know what you are doing, you can skip pre-flight checks with `--skip-preflight-checks`
```
So preflight checks failed, we need to open ports in firewalld:
```
[root@k8s-master-1 ~]# firewall-cmd --zone=public --add-port=6443/tcp --permanent
success
[root@k8s-master-1 ~]# firewall-cmd --zone=public --add-port=10250/tcp --permanent
success
[root@k8s-master-1 ~]# firewall-cmd --reload
success
```
Lets turn the swap off and make sure fstab has no entries for swap: 
```
[root@k8s-master-1 ~]# swapoff -a 
[root@k8s-master-1 etc]# cat fstab 
/dev/mapper/centos-root /                       xfs     defaults        0 0
UUID=6a16b235-1bd6-4cfc-8a25-69ea8adf4708 /boot                   xfs     defaults        0 0
````
Update sysctl conf file for bridge-nf-call-iptables:
```
[root@k8s-master-1 ~]# cat /etc/sysctl.conf 
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
```

Lets get kubeadm to work!

```
[root@k8s-master-1 ~]# kubeadm init                                                                                       
[kubeadm] WARNING: kubeadm is in beta, please do not use it for production clusters.
[init] Using Kubernetes version: v1.8.3
[init] Using Authorization modes: [Node RBAC]
[preflight] Running pre-flight checks
[preflight] WARNING: firewalld is active, please ensure ports [6443 10250] are open or your cluster may not function correctly
[kubeadm] WARNING: starting in 1.8, tokens expire after 24 hours by default (if you require a non-expiring token use --token-ttl 0)
[certificates] Generated ca certificate and key.
[certificates] Generated apiserver certificate and key.
[certificates] apiserver serving cert is signed for DNS names [k8s-master-1 kubernetes kubernetes.default kubernetes.default.svc kubernetes.default.svc.cluster.local] and IPs [10.96.0.1 10.0.3.15]
[certificates] Generated apiserver-kubelet-client certificate and key.
[certificates] Generated sa key and public key.
[certificates] Generated front-proxy-ca certificate and key.
[certificates] Generated front-proxy-client certificate and key.
[certificates] Valid certificates and keys now exist in "/etc/kubernetes/pki"
[kubeconfig] Wrote KubeConfig file to disk: "admin.conf"
[kubeconfig] Wrote KubeConfig file to disk: "kubelet.conf"
[kubeconfig] Wrote KubeConfig file to disk: "controller-manager.conf"
[kubeconfig] Wrote KubeConfig file to disk: "scheduler.conf"
[controlplane] Wrote Static Pod manifest for component kube-apiserver to "/etc/kubernetes/manifests/kube-apiserver.yaml"
[controlplane] Wrote Static Pod manifest for component kube-controller-manager to "/etc/kubernetes/manifests/kube-controller-manager.yaml"
[controlplane] Wrote Static Pod manifest for component kube-scheduler to "/etc/kubernetes/manifests/kube-scheduler.yaml"
[etcd] Wrote Static Pod manifest for a local etcd instance to "/etc/kubernetes/manifests/etcd.yaml"
[init] Waiting for the kubelet to boot up the control plane as Static Pods from directory "/etc/kubernetes/manifests"
[init] This often takes around a minute; or longer if the control plane images have to be pulled.
[uploadconfig] Storing the configuration used in ConfigMap "kubeadm-config" in the "kube-system" Namespace        [0/1777]
[markmaster] Will mark node k8s-master-1 as master by adding a label and a taint
[markmaster] Master k8s-master-1 tainted and labelled with key/value: node-role.kubernetes.io/master=""
[bootstraptoken] Using token: d6f610.18971c6b916a9ffb
[bootstraptoken] Configured RBAC rules to allow Node Bootstrap tokens to post CSRs in order for nodes to get long term ce$tificate credentials
[bootstraptoken] Configured RBAC rules to allow the csrapprover controller automatically approve CSRs from a Node Bootstr$p Token
[bootstraptoken] Configured RBAC rules to allow certificate rotation for all node client certificates in the cluster
[bootstraptoken] Creating the "cluster-info" ConfigMap in the "kube-public" namespace
[addons] Applied essential addon: kube-dns
[addons] Applied essential addon: kube-proxy

Your Kubernetes master has initialized successfully!

To start using your cluster, you need to run (as a regular user):

  mkdir -p $HOME/.kube
  sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
  sudo chown $(id -u):$(id -g) $HOME/.kube/config

You should now deploy a pod network to the cluster.
Run "kubectl apply -f [podnetwork].yaml" with one of the options listed at:
  http://kubernetes.io/docs/admin/addons/

You can now join any number of machines by running the following on each node
as root:

  kubeadm join --token d6f610.18971c6b916a9ffb 10.0.3.15:6443 --discovery-token-ca-cert-hash sha256:5e8191be284392c695e4d$
62a26d5203554db550281cc77a3fdbca9de90e56a9

```

Looks like we have a k8s master!

Wo Wo Wo ... kubectl doesnot work!
```
[root@k8s-master-1 ~]# kubectl get pods
The connection to the server localhost:8080 was refused - did you specify the right host or port?
```
Lets copy k8s admin config to the home folder for your user:
```
[root@k8s-master-1 ~]# mkdir -p $HOME/.kube
[root@k8s-master-1 ~]# sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
[root@k8s-master-1 ~]# sudo chown $(id -u):$(id -g) $HOME/.kube/config

```
Its working!!
```
[root@k8s-master-1 ~]# kubectl get pods 
No resources found.
[root@k8s-master-1 ~]# kubectl get pods -n kube-system
NAME                                   READY     STATUS    RESTARTS   AGE
etcd-k8s-master-1                      1/1       Running   4          5m
kube-apiserver-k8s-master-1            1/1       Running   2          5m
kube-controller-manager-k8s-master-1   1/1       Running   0          5m
kube-dns-545bc4bfd4-hbxgq              0/3       Pending   0          6m
kube-proxy-pzw56                       1/1       Running   0          6m
kube-scheduler-k8s-master-1            1/1       Running   0          5m
```
Lets look back at what kubeadm actually did for us: 

## Docker Images

## Docker Running Containers

## Kubernetes config 

## kubectl test
