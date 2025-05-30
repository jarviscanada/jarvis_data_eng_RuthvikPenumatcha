-- Show table schema 
\d+ retail;

-- Show first 10 rows
SELECT * FROM retail limit 10;

-- Check # of records
SELECT count(*) as count FROM retail;

-- Number of Unique clients
SELECT count(DISTINCT(customer_id)) as count FROM retail;

-- Invoice date range
SELECT max(invoice_date) as max, min(invoice_date) as min FROM retail;

-- Number of SKU/Merchants
SELECT count(DISTINCT(stock_code)) as count FROM retail;

-- Average Invoice amount excluding invoices with a negative amount
SELECT avg(inv_sum) as avg FROM (
	SELECT SUM(unit_price*quantity) as inv_sum
       		FROM retail
	       		GROUP BY invoice_no
		       		HAVING SUM(unit_price*quantity)>0)
	as inv;

-- Total revenue
SELECT sum(unit_price*quantity) as sum from retail;

-- Calculate total revenue by YYYYMM
SELECT EXTRACT(YEAR from invoice_date)*100 + EXTRACT(MONTH from invoice_date) as yyyymm,
	SUM(unit_price*quantity) as sum
       		FROM retail 
			GROUP BY yyyymm
		       		ORDER BY yyyymm asc;

