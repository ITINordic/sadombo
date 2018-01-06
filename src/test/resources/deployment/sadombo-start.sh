 #!/bin/bash

# Start Mongod 
service mongod restart
echo "Mongodb started"

# Delay 60 seconds
sleep 60;

# Start OpenHIM Core
nohup openhim-core > /var/log/sadombo/openhim-core.log &
echo "OpenHIM started"

# Delay 60 seconds
sleep 60;

# Start Sadombo
nohup java -jar /usr/lib/sadombo/sadombo-jar-with-dependencies-0.0.1.jar > /var/log/sadombo/sadombo-dhis-mediator.log &

echo "Sadombo started"


