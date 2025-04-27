-- Connect to host_agent database
\c host_agent;

-- Group hosts by Host ID
select cpu_number, id, total_mem from host_info order by cpu_number, total_mem desc;

-- Average memory usage
select b.host_id, a.hostname, date_trunc('hour', b.timestamp) + date_part('minute', b.timestamp):: int / 5 * interval '5 min' as rounded_timestamp, avg(a.total_mem - b.memory_free) as avg_used_mem_percentage from host_info a inner join host_usage b on a.id=b.host_id group by rounded_timestamp, b.host_id, a.hostname;

-- Detect host failure
select host_id, date_trunc('hour', timestamp) + date_part('minute', timestamp):: int / 5 * interval '5 min' as rounded_timestamp, count(timestamp) as num_data_points from host_usage group by rounded_timestamp, host_id having count(timestamp)<3 order by rounded_timestamp;