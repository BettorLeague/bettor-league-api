#!/bin/bash
docker pull nadjim/bettor-league-api
docker-compose -f /bettor-league-api/docker-compose.prod.yml down --remove-orphans
docker-compose -f /bettor-league-api/docker-compose.prod.yml up -d
