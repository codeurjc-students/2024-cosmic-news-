#!/bin/bash
docker login
docker build -t pedrocristino2020/cosmic_news -f docker/Dockerfile .
docker push pedrocristino2020/cosmic_news