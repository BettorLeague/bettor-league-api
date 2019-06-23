#!/bin/bash
docker pull nadjim/bettor-league-api
docker-compose -f /bettor-league-api/docker-compose.yml down
docker-compose -f /bettor-league-api/docker-compose.yml up -d
