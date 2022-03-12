#!/bin/sh

COMMAND=$1

cd flyway

case ${COMMAND} in
  init)
    flyway -schemas=delivery -baselineVersion=0 baseline
    flyway -schemas=delivery -locations=filesystem:./sql/delivery migrate
    ;;
  migrate)
    flyway -schemas=delivery -locations=filesystem:./sql/delivery migrate
    ;;
  repair)
     flyway -schemas=delivery -locations=filesystem:./sql/delivery repair
    ;;
  clean)
    flyway -schemas=delivery clean
    ;;
esac
