start "Mosquitto" mosquitto
cd Soffritti
start "Virtual Robot" startServer.bat
timeout 2
cd ../nodeCode/robotFrontend
start "Frontend" node frontendServerRobot.js
start http://localhost:8090/
start http://localhost:3000/


 