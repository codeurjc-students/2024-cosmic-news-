#!/bin/bash
docker login
docker build -t PedroCristino2020/cosmic_news -f docker/Dockerfile .
docker push PedroCristino2020/cosmic_news