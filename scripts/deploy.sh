#!/bin/bash

export NVM_DIR="$HOME/.nvm"
[ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"

echo "사용자 정보: $USER"

NVM_VER=${nvm --version}

echo "nvm 정보: $NVM_VER"

NODE_VER=${node --version}

echo "node 정보: $NODE_VER"

REPOSITORY=/home/ubuntu/app

echo "pm2 실행중인 프로세스 종료"

pm2 kill

echo "> 새 애플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/*SNAPSHOT.jar | tail -n 1)

echo "> JAR NAME: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

pm2 start /home/footbolic/ecosystem.config.js