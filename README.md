# Sadombo

## Installing Mediator Ubuntu 16.04
To create the ubuntu service use the files in src_install

1. Create a user to use for the service using adduser --system --group --disabled-password sadombo-mediator
2. Create a folder named deploy in /home/sadombo-mediator. Copy the jar into /home/sadombo-mediator/deploy and change the owner of deploy eg chown sadombo-mediator:sadombo-mediator deploy 
2. Create systemd unit file /etc/systemd/system/sadombo-mediator.service and specify the user and group created earlier. Example available in src_install
2. Create startup script /usr/local/bin/sadombo-mediator.sh. Example available in src_install
2. Give the user execute permissions on the shell script. For example chown sadombo-mediator:sadombo-mediator sadombo-mediator.sh and chmod u+x sadombo-mediator.sh
2. Create pid folder /var/run/sadombo-mediator/
2. Give the the service account ownership of the folders using chown sadombo-mediator:sadombo-mediator -R
2. Enable service at startup by using systemctl enable sadombo-mediator.service
2. Start the service using systemctl start sadombo-mediator.service

## Installing OpenHIM Ubuntu 16.04
To create the ubuntu service use the files in src_install

1. Create a user to use for the service using adduser --system --group --disabled-password openhim
2. Create systemd unit file /etc/systemd/system/openhim-core.service and specify the user and group created earlier. Example available in src_install
2. Create startup script /usr/local/bin/openhim-core.sh. Example available in src_install
2. Give the user execute permissions on the shell script.
2. Create pid folder /var/run/openhim-core/
2. Give the the service account ownership of the folders using chown openhim:openhim -R
2. Enable service at startup by using systemctl enable openhim-core.service
2. Start the service using systemctl start openhim-core.service
