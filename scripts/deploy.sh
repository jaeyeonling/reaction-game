#!/bin/bash
pm2 start java --name /home/ubuntu/reaction-game.jar -- \
    -Duser.timezone=Asia/Seoul \
    -Dfile.encoding=UTF-8 \
    -Xms2G \
    -Xmx2G \
    -jar reaction-game.jar
