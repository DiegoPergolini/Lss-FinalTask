READ (and do) Soffritti/README.md
WRNING: the port hase been changed to 8090
	
On browser
	http://localhost:8090/
	
	
-----------------------------------------------------
1. connect the Raspberry to the PC with an Ethernet cable
2. share the (WIFI on windows) Internet connection with the Ethernet-Raspberry

	1. Aprire il menu start e cliccare su Impostazioni in basso a sinistra
	2. Seguire il percorso Rete e Internet->Wi-Fi
	3. Fare click sull’opzione “Gestisci le impostazioni Wi-Fi”
	4. Entrerete nelle impostazioni relative al Sensore Wi-Fi
	5. Scorrendo in fondo troverete Gestisci reti note, 
	un elenco di connessioni in cui ci sarà anche la rete wireless con l’etichetta Non Condiviso
	6. Cliccare sulla rete wireless e poi scegliere l’opzione Condividi
	7. Inserire la chiave di sicurezza della rete wireless e poi cliccare ancora su Condividi
	8. Vedrete adesso sotto Gestisci reti note, la rete wireless con l’etichetta Condiviso
	9. Adesso è necessario configurare l’opzione Condividi le reti selezionate con:


 Pannello di controllo --> Centro connessioni di rete e condivisione --> Modifica Impostazioni scheda --> 
 clic destro sulla scheda wifi --> Proprietà --> Condivisione --> 
 segno di spunta su Consenti ad altri utenti in rete di collegarsi tramite la connessione Internet di questo computer.



git clone ssh://isslm2014@137.204.107.21:80/lab2014Bo

cat /etc/network/interfaces
cat /boot/mywifi.conf
sudo nano /boot/mywifi.conf
ap_scan=1
eapol_version=1
fast_reauth=1

network={
        ssid="natspot"
        scan_ssid=1
        key_mgmt=WPA-PSK
        psk="lezione-natali"
}

network={
        ssid="TIM-94276267"
        scan_ssid=1
        key_mgmt=WPA-PSK
        psk="HkB6gle0LdyRPYE60FzMMYBM"
}

sudo cat /etc/wpa_supplicant/wpa_supplicant.conf
sudo nano /etc/wpa_supplicant/wpa_supplicant.conf
sudo echo nameserver 8.8.8.8 > /etc/resolv.conf
echo nameserver 192.168.137.1 > /etc/resolv.conf

uname -a
Linux raspberrypi 4.14.71-v7+ #1145 SMP Fri Sep 21 15:38:35 BST 2018 armv7l GNU/Linux

route -n
sudo route add default gw 192.168.137.1
sudo route add default gw 192.168.1.1
sudo route add default gw 192.168.0.1

sudo wget -O - https://raw.githubusercontent.com/audstanley/NodeJs-Raspberry-Pi/master/Install-Node.sh | bash;

curl -sL https://deb.nodesource.com/setup_7.x | sudo -E bash -
sudo apt-get install -y nodejs

sudo su -
 
sudo date -s "2018-11-01 11:35:42"
       
sudo cp /usr/share/zoneinfo/Europe/London /etc/localtime

3. check that the system is working: ping 8.8.8.8
4. set the DNS:
sudo bash
echo "nameserver 8.8.8.8" > /etc/resolv.conf
CRTRL D

check that the DNS is working: ping google.com
5. update the system: sudo apt-get update   CON PAZIENZA ...
6. install an ARM-version of Node:
pi@raspberrypi:~ $ sudo su -
root@raspberrypi:~ # apt-get remove nodered -y
root@raspberrypi:~ # apt-get remove nodejs nodejs-legacy -y
root@raspberrypi:~ # apt-get remove npm -y # if you installed npm
root@raspberrypi:~ # curl -sL https://deb.nodesource.com/setup_5.x | sudo bash -
curl -sL https://deb.nodesource.com/setup_8.x | sudo bash - # Node.js 8 LTS "Carbon" 
root@raspberrypi:~ # apt-get install nodejs-legacy -y		CON PAZIENZA ...
root@raspberrypi:~ node -v  (legacy)  nodejs -v  v0.10.29
sudo apt-get install nodejs npm		CON PAZIENZA ...
npm -v	1.4.21
CRTL D




gpio version: 2.29 WORKS on Model B+, Revision: 1.2, Memory: 512MB, Maker: Sony
gpio version: 2.46 DOES NOT WORK
One other option if you need to use the existing v1.1 release of Pi4J is to use the dynamic linking option 
where Pi4J is dynamically linked to WiringPi rather than the default static linking.

The simplest method to install Pi4J on your RaspberryPi is to execute 
the following command directly on your RaspberryPi.
curl -s get.pi4j.com | sudo bash
1) adds the Pi4J APT repository to the local APT repositories
2) downloads and installs the Pi4J GPG public key for signature validation
3) invokes the 'apt-get update' command on the Pi4J APT repository to update the local package database
4) invokes the 'apt-get install pi4j' command to perform the download and installation
An APT repository is a collection of deb packages with metadata that is readable by the 
apt-* family of tools, namely, apt-get . Having an APT repository allows you to perform package install, 
removal, upgrade, and other operations on individual packages or groups of packages.

