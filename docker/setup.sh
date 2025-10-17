#!/bin/bash
set -e

echo "=== 🚀 Initialisation du Raspberry Pi ==="

# --- 1️⃣ Installation des paquets essentiels ---
echo "[1/5] Installation des dépendances..."
sudo apt update
sudo apt install -y git curl default-jdk docker.io docker-compose

# --- 2️⃣ Création de l’arborescence ---
echo "[2/5] Création des répertoires..."
sudo mkdir -p /docker/{mariadb/data,nginx/{html,conf.d,certs},portainer} /git /api
sudo chown -R $USER:$USER /docker /git /api

# --- 3️⃣ Clonage du dépôt et préparation de l’API ---
echo "[3/5] Clonage du projet Hackathon dans /git..."
cd /git
if [ ! -d "/git/Hackathon-Groupe-6" ]; then
  git clone -b dev https://github.com/maxime64100/Hackathon-Groupe-6.git
else
  echo "🔁 Le dépôt existe déjà, mise à jour..."
  cd Hackathon-Groupe-6
  git pull
  cd ..
fi

echo "📦 Copie du dossier API vers /api..."
cp -r /git/Hackathon-Groupe-6/api/ /api

cd /api
chmod +x mvnw || true

echo "[4/5] Compilation de l’API Spring Boot..."
mvnw clean package -DskipTests

# --- 4️⃣ Déploiement des conteneurs Docker ---
echo "[5/5] Déploiement du stack Docker (MariaDB, phpMyAdmin, nginx)..."

cat > /docker/docker-compose.yml <<'EOF'
version: '3.8'

services:
  mariadb:
    image: mariadb:latest
    container_name: mariadb
    restart: always
    ports:
    - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: rootpass
      MYSQL_DATABASE: hackathon
      MYSQL_USER: hackuser
      MYSQL_PASSWORD: hackpass
    volumes:
      - /docker/mariadb/data:/var/lib/mysql
    networks:
      - hackathon_net

  phpmyadmin:
    image: phpmyadmin:latest
    container_name: phpmyadmin
    restart: always
    ports:
      - "3307:80"
    environment:
      PMA_HOST: mariadb
      PMA_PORT: 3306
      MYSQL_ROOT_PASSWORD: rootpass
    depends_on:
      - mariadb
    networks:
      - hackathon_net

  nginx:
    image: nginx:latest
    container_name: nginx
    restart: always
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - /docker/nginx/html:/usr/share/nginx/html:ro
      - /docker/nginx/conf.d:/etc/nginx/conf.d:ro
      - /docker/nginx/certs:/etc/nginx/certs:ro
    networks:
      - hackathon_net

  portainer:
    image: portainer/portainer-ce:latest
    container_name: portainer
    restart: always
    ports:
      - "9000:9000"
    volumes:
      - /docker/portainer:/data
    networks:
      - hackathon_net

networks:
  hackathon_net:
EOF

sudo docker-compose -f /docker/docker-compose.yml up -d

# --- 5️⃣ Création du service systemd pour l’API ---
echo "🛠️  Création du service systemd hackathon-api..."

sudo tee /etc/systemd/system/hackathon-api.service > /dev/null <<'EOF'
[Unit]
Description=Hackathon API Spring Boot
After=network.target docker.service
Requires=docker.service

[Service]
User=root
WorkingDirectory=/api
ExecStart=/usr/bin/java -jar /api/target/hackathon-0.0.1-SNAPSHOT.jar --server.port=8080
SuccessExitStatus=143
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload
sudo systemctl enable hackathon-api
sudo systemctl start hackathon-api

echo "✅ Déploiement terminé !"
echo "🌐 API : http://$(hostname -I | awk '{print $1}'):8080"
echo "🗃️  phpMyAdmin : http://$(hostname -I | awk '{print $1}'):8081"
echo "🌍 Nginx : http://$(hostname -I | awk '{print $1}')"
