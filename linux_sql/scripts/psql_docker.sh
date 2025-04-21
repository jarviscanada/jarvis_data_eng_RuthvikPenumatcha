#!/bin/sh

# Capture CLI arguments
export cmd=$1 #Using export so that these parameters are stored even if a child process is later executed
export db_username=$2
export db_password=$3

# Start docker
# Make sure you understand the double pipe operator
sudo systemctl status docker || sudo systemctl start docker # If docker is not started, then start docker

# Command to remove the container: docker container rm jrvs-psql

# Check container status (try the following cmds on terminal)
docker container inspect jrvs-psql
container_status=$? # Status code is 0 is container already exists

# User switch case to handle create|stop|start operations
case $cmd in
  create)

  # Check if the container is already created
  if [ $container_status -eq 0 ]; then
		echo 'Container already exists'
		exit 1
	else
	  echo 'Creating Container'
	fi

  # Check # of CLI arguments
  if [ $# -ne 3 ]; then
    echo 'Create requires username and password'
    exit 1
  fi

  # Create container
	docker volume create pgdata #Creating docker volume to store the data from this container. This is done so any changes made to the container will be persisted in the volume.

  # Start the container
	docker run --name jrvs-psql -e POSTGRES_USERNAME=db_username -e POSTGRES_PASSWORD=db_password  -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres:9.6-alpine

  # Make sure you understand what's `$?`
	exit $?
	;;

  start|stop)
  # Check instance status; exit code will be 1 if container has not been created
  if [ $container_status -ne 0 ]; then #Check if the status is not equal to 0, indicating error
    echo 'Container is not created. Exiting'
    exit 1
  else
    # Container has been successfully created
    # We can proceed further and start/stop the container
    echo 'Container has been successfully created'
  fi

  # Start or stop the container
	docker container $cmd jrvs-psql

  #Printing the action performed and the status of the process
	if [ $cmd = "stop" ]; then
	  echo "Stopped the Container"
    docker ps -f name=jrvs-psql #Printing the information about the container to ensure that the container has been created and it's running
	else
	  echo "Started the Container"
	  docker ps -f name=jrvs-psql #Should be empty. Done to ensure that the container has been stopped.
	fi

	exit $?
	;;

  *)
	echo 'Illegal command'
	echo 'Accepted Commands: start|stop|create'
	exit 1
	;;
esac