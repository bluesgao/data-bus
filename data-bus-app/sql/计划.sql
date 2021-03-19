--mls_plan
SELECT
	t1.protocol_id AS id,
	t1.protocol_sn AS plan_sn,
	t1.status ,
	t1.old_status AS is_closed ,
	CONVERT(IFNULL(UNIX_TIMESTAMP(t1.complete_time),
	0),
	unsigned) AS finish_time ,
	t1.`number` AS plan_qty ,
	t1.order_user_id AS operator_id ,
	t1.order_user_name AS operator_name ,
	t1.version ,
	t1.create_time AS row_create_time,
	t1.update_time AS row_update_time,
	t1.is_delete AS is_deleted,
	CONVERT(IFNULL(UNIX_TIMESTAMP(t2.begin_time),
	0),
	unsigned) AS start_time,
	CONVERT(IFNULL(UNIX_TIMESTAMP(t2.end_time),
	0),
	unsigned) AS end_time
FROM
	trade_protocol t1
LEFT JOIN trade_protocol_logistics t2 ON
	t1.protocol_id = t2.protocol_id
WHERE
	t1.protocol_id =?
	

	
--mls_plan_goods
SELECT
	UUID_SHORT() AS id,
	g.protocol_id AS plan_id,
	g.goods_id,
	g.goods_name ,
	g.goods_product_img ,
	g.goods_target,
	g.goods_type ,
	p.delivery_pay_type AS down_pay_type,
	p.delivery_type ,
	p.price AS goods_price,
	p.service_charge,
	m.coal_mine_name AS coalmine_name,
	m.coal_mine_nick_name AS coalmine_nickname,
	m.province AS province_code,
	m.province_name AS province_name,
	m.city AS city_code,
	m.city_name AS city_name,
	m.area AS area_code,
	m.area_name AS area_name,
	m.address AS address,
	(select group_concat(distinct(tgsa.service_type)) from trade_goods_service_attach tgsa where tgsa.goods_id = g.goods_id) as goods_service_type
FROM
	trade_protocol_goods g,
	trade_protocol_payment_plan p,
	trade_protocol_coal_mine m
WHERE
	g.protocol_id = p.protocol_id
	AND g.protocol_id = m.protocol_id
	AND g.protocol_id = ?

--mls_plan_logistics
SELECT
	l.protocol_id AS id,
	l.target_province AS province_code,
	l.target_province_name AS province_name,
	l.target_city AS city_code,
	l.target_city_name AS city_name,
	l.target_area AS area_code,
	l.target_area_name AS area_name,
	l.target_address AS address ,
	l.target_contact_people AS contact_people,
	l.target_contact_number AS contact_number,
	l.target_remark AS remark
FROM
	trade_protocol_logistics l
WHERE
	l.protocol_id = ?

--mls_plan_log
SELECT
	UUID_SHORT() AS id,
	l.protocol_id AS plan_id,
	l.content AS log_content,
	CONVERT(ifnull(l.create_company_id ,
	0) ,
	unsigned)AS operator_id,
	l.create_user_name AS operator_name,
	CONVERT(ifnull(l.create_company_id ,
	0) ,
	unsigned)AS operator_company_id,
	l.create_company_name AS operator_company_name,
	l.log_type AS log_type ,
	l.create_time AS row_create_time,
	l.create_time AS row_update_time
FROM
	trade_protocol_log l
WHERE
	l.order_log_type = 0
	AND l.protocol_log_id = ?
	
--mls_plan_business
