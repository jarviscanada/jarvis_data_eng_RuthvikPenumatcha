#!/bin/sh

user_command=$1

case $user_command in
	create)
	export PGUSER='postgres-sql'
	export PGPASSWORD='password'

	# Creating a docker volume
	docker volume create psql-data

	# Creating a docker container
       	docker run --name psql-container -e POSTGRES_USER=$PGUSER -e POSTGRES_PASSWORD=$PGPASSWORD -d -v psqldata:/var/lib/postgresql/data2 -p 5432:5432 postgres:9.6-alpine
	
	exit $?
	;;

	start|stop)
	docker container $user_command psql-container
	docker ps -f name="psql-container"
			
	exit $?
	;;
esac
