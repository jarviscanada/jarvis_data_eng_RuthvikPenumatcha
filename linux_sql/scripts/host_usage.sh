#!/bin/sh

# Variable Assignment
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

# Checking to make sure there are 5 arguments in CLI
if [ "$#" -ne 5 ]; then
    echo "Illegal number of parameters. 5 parameters are expected to execute this script."
    exit 1
fi

# Defining local variables to be used as part of other commands
vmstat_mb=$(vmstat --unit M)
hostname=$(hostname -f)

# Columns in host_usage table
timestamp=$(date +"%Y-%m-%d %H:%M:%S")
mem_free=$(echo "$vmstat_mb" | tail -1 | awk -v col="4" '{print $col}')
cpu_idle=$(echo "$vmstat_mb" | tail -1 | awk '{print $15}')
cpu_kernel=$(echo "$vmstat_mb" | tail -1 | awk '{print $14}')
disk_io=$(vmstat -d | tail -1 | awk -v col="10" '{print $col}')
disk_av=$(df -BM / | tail -1 | awk '{gsub(/M/, "", $4); print $4}')

host_id_stmt="SELECT id FROM host_info WHERE hostname='$hostname'";

export PGPASSWORD=$psql_password
host_id=$(psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -t -c "$host_id_stmt")

insert_statement_psql="INSERT INTO host_usage (timestamp, host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available) VALUES ('$timestamp', '$host_id', '$mem_free', '$cpu_idle', '$cpu_kernel', '$disk_io', '$disk_av')"

psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_statement_psql"

exit $?