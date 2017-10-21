



Client ---> [Kube Cluster] ---> [Router] 
                                   |
                                   |
                                   |
                                   |
                          _________ _______ __________________________________________________
                         |                 |                                                  |
                       [Poster]            [Watcher]                                          [Monitor]
                         |                 |                                                  |
                  [Python Kafka Save]      [Go WS Kafka Broadcastor]-----[Go Metric queuer]   [Node Metrics]
                         |                 |                                    |             |
                         |                 |                                    |             |             
                         ------------------                                     |             |
                                  |---------------------------------------------              |
                                  |                                             |             |
                               [Kafka]                                          |             |
                                                                            [RabbitMQ]---------
