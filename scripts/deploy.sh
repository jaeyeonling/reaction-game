#!/bin/bash
pm2 stop reaction-game
pm2 start java --name reaction-game -- \
    -Duser.timezone=Asia/Seoul \
    -Dfile.encoding=UTF-8 \
    -Xms1G \
    -Xmx1G \
    -jar /home/ubuntu/reaction-game.jar
