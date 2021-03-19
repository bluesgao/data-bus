SELECT t1.*, t2.begin_time, t2.end_time FROM trade_protocol t1 LEFT JOIN trade_protocol_logistics t2 ON t1.protocol_id = t2.protocol_id WHERE t1.protocol_id = '0b6a512b197c46ca87febe8f90838576'


SELECT t1.protocol_id as id,t1.protocol_sn as plan_sn,t1.status ,t1.old_status as is_closed ,t1.complete_time as finish_time ,t1.`number` as plan_qty ,t1.order_user_id as operator_id ,t1.order_user_name as operator_name ,t1.version ,t1.create_time as row_create_time,t1.update_time as row_update_time,t1.is_delete , t2.begin_time as start_time, t2.end_time FROM trade_protocol t1 LEFT JOIN trade_protocol_logistics t2 ON t1.protocol_id = t2.protocol_id WHERE t1.protocol_id ='215165519539209204';

SELECT t1.protocol_id as id,t1.protocol_sn as plan_sn,t1.status ,t1.old_status as is_closed ,CONVERT(IFNULL(UNIX_TIMESTAMP(t1.complete_time),0),unsigned) as finish_time ,t1.`number` as plan_qty ,t1.order_user_id as operator_id ,t1.order_user_name as operator_name ,t1.version ,t1.create_time as row_create_time,t1.update_time as row_update_time,t1.is_delete as is_deleted, CONVERT(IFNULL(UNIX_TIMESTAMP(t2.begin_time),0),unsigned) as start_time, CONVERT(IFNULL(UNIX_TIMESTAMP(t2.end_time),0),unsigned) as end_time FROM trade_protocol t1 LEFT JOIN trade_protocol_logistics t2 ON t1.protocol_id = t2.protocol_id
insert into mls_plan (row_create_time,plan_qty,operator_id,end_time,version,finish_time,operator_name,start_time,is_deleted,row_update_time,id,plan_sn,status,is_closed)  values('2020-09-16 18:26:44.0','100.0','16887888549595062445','null','0','null','新金融企业','null','false','2021-01-13 19:25:01.0','215165519539208429','P2009160001','20','0') ON DUPLICATE KEY UPDATE id = '215165519539208429'

-- 200
select * from trade_protocol_logistics t1 where t1.protocol_id ='215165519539208801';
select * from trade_protocol tp where tp.protocol_id ='215165519539208801';


select  convert(ifnull(t1.end_time,0),unsigned)as end_time,convert(ifnull( t1.begin_time,0) ,unsigned)as begin_time from trade_protocol_logistics t1 where t1.protocol_id ='215165519539208193';

-- 1561636501
-- 1561636501 时间戳转long
select tp.complete_time ,FROM_UNIXTIME(LEFT(tp.complete_time,10), '%Y-%m-%d')as complete_time1,ifnull(UNIX_TIMESTAMP(tp.complete_time),0)  AS createTime from trade_protocol tp where tp.protocol_id in('215165519539208199','215165519539208193');

select convert(ifnull(tp.complete_time,0) ,unsigned)as complete_time from trade_protocol tp where tp.protocol_id ='215165519539208199';
select  * from trade_protocol_logistics t1 where t1.protocol_id ='215165519539208193';


select * from trade_protocol_goods g where g.protocol_id ='215165519539208801';
select * from trade_protocol_payment_plan p where p.protocol_id ='215165519539208801';
select * from trade_protocol_coal_mine m where m.protocol_id ='215165519539208801';

update renewscc_dev.trade_protocol_log tpl set tpl.create_time = now();

update renewscc_dev.trade_protocol tpl set tpl.update_time = now();

update renewscc_dev.trade_protocol_goods tpl set tpl.update_time = now();
update renewscc_dev.trade_protocol_logistics tpl set tpl.update_time = now();

select count(*) from renewscc_dev.trade_protocol;
 
select UUID_SHORT() as id, l.protocol_id as plan_id, l.content as log_content, convert(ifnull(l.create_company_id ,0) ,unsigned)as operator_id, l.create_user_name as operator_name, convert(ifnull(l.create_company_id ,0) ,unsigned)as operator_company_id, l.create_company_name as operator_company_name, l.log_type as log_type , l.create_time as row_create_time, l.create_time as row_update_time from trade_protocol_log l where l.order_log_type = 0;


show variables like '%time_zone%';



select tpc.protocol_order_id ,tpc.contract_sn ,tpc.contract_type ,tpc.contract_url ,tpc.create_time ,tpc.sign_time ,tpc.is_sign from trade_protocol_contract tpc where tpc.protocol_order_id =?;


-- 订单表
select
	tpo.protocol_order_id as id,
	tpo.protocol_order_sn as order_sn,
	tpo.protocol_id as plan_id,
	tpo.protocol_sn as plan_sn ,
	tpo.status as status,
	tpo.negotiate_status as negotiate_status,
	tpo.send_status as is_send,
	tpo.`number` as order_qty,
	tpo.tax_rate as tax_rate,
	tpo.payment_path as up_business_type,
	tpo.down_business_type as down_business_type ,
	tpo.version as version,
	tpo.is_close as is_closed,
	tpo.is_delete as is_deleted,
	tpo.online_charge as is_online_charge,
	tpo.create_time as row_create_time,
	tpo.update_time as row_update_time,
	ifnull(UNIX_TIMESTAMP(tpol.begin_time),0) as start_time,
	ifnull(UNIX_TIMESTAMP(tpol.end_time),0) as end_time,
	tppp.delivery_type as delivery_type,
	tppp.delivery_pay_type as down_pay_type,
	tpl.create_time as finish_time
from
	trade_protocol_order tpo ,
	trade_protocol_order_logistics tpol ,
	trade_protocol_payment_plan tppp,
	trade_protocol_log tpl 
where
	tpo.protocol_order_id = tpol.protocol_order_id
	and tpo.protocol_id = tppp.protocol_id 
	or(tpo.protocol_order_id = tpl.protocol_order_id 
	and tpl.order_log_type =0
	and tpl.log_type =24)
	and tpo.protocol_order_id ='215165519539213273';
	

-- 215165519539213273 215165519539216656
select * from trade_protocol_order t where t.protocol_order_id ='215165519539213273';
select * from trade_protocol_order_logistics t where t.protocol_order_id ='215165519539213273';
select * from trade_protocol_payment_plan t where t.protocol_id ='215165519539212996';
select * from trade_protocol_log t where t.protocol_order_id in('215165519539213273','215165519539216656');

select
	tpo.protocol_order_id as id,
	tpo.protocol_order_sn as order_sn,
	tpo.protocol_id as plan_id,
	tpo.protocol_sn as plan_sn ,
	tpo.status as status,
	tpo.negotiate_status as negotiate_status,
	tpo.send_status as is_send,
	tpo.`number` as order_qty,
	tpo.tax_rate as tax_rate,
	tpo.payment_path as up_business_type,
	tpo.down_business_type as down_business_type ,
	tpo.version as version,
	tpo.is_close as is_closed,
	tpo.is_delete as is_deleted,
	tpo.online_charge as is_online_charge,
	tpo.create_time as row_create_time,
	tpo.update_time as row_update_time,
	ifnull(UNIX_TIMESTAMP(tpol.begin_time),0) as start_time,
	ifnull(UNIX_TIMESTAMP(tpol.end_time),0) as end_time,
	tppp.delivery_type as delivery_type,
	tppp.delivery_pay_type as down_pay_type,
	tpl.create_time as finish_time
from
	trade_protocol_order tpo left join trade_protocol_order_logistics tpol on tpo.protocol_order_id =tpol.protocol_order_id 
	left join trade_protocol_payment_plan tppp on tpo.protocol_id =tppp.protocol_id 
	-- left join trade_protocol_log tpl on tpo.protocol_order_id =tpl.protocol_order_id 
where tpo.protocol_order_id in('215165519539213273','215165519539216656')
or (tpl.order_log_type =0
	and tpl.log_type =24);

select * from renewscc_dev.trade_protocol_order;
update renewscc_dev.trade_protocol_order tpo set tpo.update_time = now();
-- mls_order

select
	temp.*,
	ifnull(UNIX_TIMESTAMP(temp_tpl.create_time),0) as finish_time
from
	(
	select
		tpo.protocol_order_id as id,
		tpo.protocol_order_sn as order_sn,
		tpo.protocol_id as plan_id,
		tpo.protocol_sn as plan_sn ,
		tpo.status as status,
		tpo.negotiate_status as negotiate_status,
		tpo.send_status as is_send,
		tpo.`number` as order_qty,
		tpo.tax_rate as tax_rate,
		tpo.payment_path as up_business_type,
		tpo.down_business_type as down_business_type ,
		tpo.version as version,
		tpo.is_close as is_closed,
		tpo.is_delete as is_deleted,
		tpo.create_user_id  as operator_id,
		tp.order_user_name as operator_name,
		tpo.online_charge as is_online_charge,
		tpo.create_time as row_create_time,
		tpo.update_time as row_update_time,
		ifnull(UNIX_TIMESTAMP(tpol.begin_time), 0) as start_time,
		ifnull(UNIX_TIMESTAMP(tpol.end_time), 0) as end_time,
		tppp.delivery_type as delivery_type,
		tppp.delivery_pay_type as down_pay_type
	from
		trade_protocol_order tpo
	left join trade_protocol_order_logistics tpol on
		tpo.protocol_order_id = tpol.protocol_order_id
	left join trade_protocol_payment_plan tppp on
		tpo.protocol_id = tppp.protocol_id left join trade_protocol tp on tp.protocol_id =tpo.protocol_id 
	where
		tpo.protocol_order_id ='215165519539213273') temp
left join (
	select
		tpl.protocol_order_id,
		tpl.create_time
	from
		trade_protocol_log tpl
	where
		tpl.protocol_order_id = '215165519539213273'
		and tpl.order_log_type = 0
		and tpl.log_type = 24) temp_tpl on
	temp.id = temp_tpl.protocol_order_id;
	
-- mls_order_contract
update renewscc_dev.trade_protocol_contract t set t.create_time = now();

select
	UUID_SHORT() as id,
	tpo.protocol_order_id as order_id,
	case
		tpc.contract_type 
		when 'PURCHASE' then 0
		when 'CARRIER' then 10
		when 'AGE_SERPRI' then 20
		else 0
	end as contract_type,
	tpc.contract_sn as contract_sn,
	tpc.contract_url as contract_url,
	ifnull(UNIX_TIMESTAMP(tpc.create_time), 0) as contract_create_time,
	ifnull(UNIX_TIMESTAMP(tpc.sign_time), 0) as contract_sign_time,
	tpc.is_sign as is_sign,
	tpc.create_time as row_create_time,
	tpc.create_time as row_update_time
from
	trade_protocol_contract tpc
left join trade_protocol_order tpo on
	tpc.protocol_id = tpo.protocol_id
where
	tpc.contract_id = '0002c3fa6500416987d158d386e23eae';
	
select
	tpc.*,
	tpo.*
from
	trade_protocol_contract tpc ,
	trade_protocol_order tpo
where
	tpc.protocol_id = tpo.protocol_id
	and tpc.contract_id = '0002c3fa6500416987d158d386e23eae';


-- PURCHASE:0  CARRIER:10 AGE_SERPRI:20
select
	tpc.*,
	case
		tpc.contract_type 
		when 'PURCHASE' then 0
		when 'CARRIER' then 10
		when 'AGE_SERPRI' then 20
		else 0
	end
from
	trade_protocol_contract tpc
where
	tpc.contract_id = '0002c3fa6500416987d158d386e23eae';

-- mls_order_goods
update renewscc_dev.trade_protocol_goods t set t.update_time = now();

select
	UUID_SHORT() as id,
	tpo.protocol_order_id as order_id,
	tpg.goods_id ,
	tpg.goods_name ,
	tpg.goods_type ,
	(select group_concat(distinct(tgsa.service_type)) from trade_goods_service_attach tgsa where tgsa.goods_id = tpg.goods_id) as goods_service_type,
	tpg.goods_product_img ,
	tpg.goods_target ,
	tpcm.coal_mine_id as coalmine_id,
	tpcm.coal_mine_name as coalmine_name,
	tpcm.coal_mine_nick_name as coalmine_nickname,
	tpcm.province as province_code,
	tpcm.province_name ,
	tpcm.city as city_code,
	tpcm.city_name ,
	tpcm.area as area_code,
	tpcm.area_name ,
	tpcm.address 
from
	trade_protocol_goods tpg ,
	trade_protocol_coal_mine tpcm,
	trade_protocol_order tpo
where
	tpg.protocol_id = tpo.protocol_id
	and tpo.protocol_id = tpcm.protocol_id
	and tpg.protocol_id ='215165519539208192';

-- trade_goods_service_attach
select group_concat(distinct(tgsa.service_type))list from trade_goods_service_attach tgsa where tgsa.goods_id ='215165519539232982';

select tpg.*, (select group_concat(distinct(tgsa.service_type)) from trade_goods_service_attach tgsa where tgsa.goods_id = tpg.goods_id) as st from 	trade_protocol_goods tpg ;

select * from  trade_goods_service_attach tgsa where tgsa.goods_id in('215165519539232982','215165519539232871','215165519539233104','215165519539233672','215165519539232560');

-- mls_order_log 和 mls_plan_log 冲突
select
	UUID_SHORT() as id,
	tpl.protocol_order_id as order_id,
	tpl.content as log_content,
	tpl.log_type as log_type,
	tpl.create_company_id as operator_company_id,
	tpl.create_company_name as operator_company_name,
	tpl.create_user_id as operator_id,
	tpl.create_user_name as operator_name,
	tpl.create_time as row_create_time ,
	tpl.create_time as row_update_time
from
	trade_protocol_log tpl
where
	 tpl.order_log_type = 0
	and tpl.protocol_log_id ='03cae3071e8e4470bc2a99d290dc5a60';

-- mls_order_logistics
update renewscc_dev.trade_protocol_order_logistics t set t.remark = 'test';

select
	UUID_SHORT() as id,
	tpol.protocol_order_id as order_id,
	0 as address_type,
	tpol.target_address_id as address_id,
	tpol.target_province as province_code ,
	tpol.target_province_name as province_name,
	tpol.target_city as city_code,
	tpol.target_city_name as city_name,
	tpol.target_area as area_code,
	tpol.target_area_name as area_name,
	tpol.target_address as address  
from
	trade_protocol_order_logistics tpol
where
	tpol.protocol_order_id = '215165519539213272';

select
	UUID_SHORT() as id,
	tpol.protocol_order_id as order_id,
	10 as address_type,
	tpol.source_province as province_code ,
	tpol.source_province_name as province_name,
	tpol.source_city as city_code,
	tpol.source_city_name as city_name,
	tpol.source_area as area_code,
	tpol.source_area_name as area_name,
	tpol.source_address as address  
from
	trade_protocol_order_logistics tpol
where
	tpol.protocol_order_id = '215165519539213272';

-- mls_order_negotiate
update renewscc_dev.trade_protocol_order_negotiate t set t.row_update_time = now();

select
    tpon.id as id,
	tpon.protocol_order_id as order_id,
	tpon.negotiate_status as status,
	tpon.is_expired,
	tpon.before_price,
	tpon.after_price,
	tpon.before_platform_price,
	tpon.after_platform_price,
	ifnull(UNIX_TIMESTAMP(tpon.effective_time), 0) as effective_time,
	tpon.`version`,
	case when  (tpon.create_user_id='' or tpon.create_user_id is null) then 0 else tpon.create_user_id end as operator_id,
	tpon.create_user_name as operator_name,
	tpon.row_create_time,
	tpon.row_update_time 
from
	trade_protocol_order_negotiate tpon
where
	tpon.id = 1;


-- mls_order_short_url
select
	tpsu.short_code,
	tpsu.real_url,
	tpsu.real_url,
	tpsu.is_delete,
	tpsu.creat_time as row_create_time,
	tpsu.creat_time as row_update_time
from
	trade_protocol_short_url tpsu
where
	tpsu.id = '2344d6de2fb646c0afe47ee3323e3751';

-- mls_order_snapshot
select 
tpos.id,
tpos.protocol_order_id as order_id,
tpos.snapshot_version ,
tpos.freight_price ,
tpos.tax_rate/10000 as tax_rate,
tpos.tax_freight_price as pithead_price,
tpos.platform_price ,
tpos.price as market_price,
tpos.discounts_price ,
 ifnull(UNIX_TIMESTAMP(tpos.carrier_contract_create_time), 0) as carrier_contract_create_time,
 ifnull(UNIX_TIMESTAMP(tpos.carrier_contract_sign_time), 0) as carrier_contract_sign_time,
tpos.carrier_contract_sn ,
tpos.carrier_contract_url ,
ifnull(UNIX_TIMESTAMP(tpos.purchase_contract_create_time), 0) as purchase_contract_create_time,
ifnull(UNIX_TIMESTAMP(tpos.purchase_contract_sign_time), 0) as purchase_contract_sign_time,
tpos.purchase_contract_sn ,
tpos.purchase_contract_url ,
tpos.row_create_time,
tpos.row_create_time 
from trade_protocol_order_snapshot tpos where tpos.id =1;

-- mls_order_negotiate_audit
select
	UUID_SHORT() as id,
	tpon.id as order_negotiate_id,
	tpon.bop_audit_type  as `type`,
	tpon.bop_audit_remarks as remark,
	tpon.bop_audit_time as row_create_time,
	tpon.bop_audit_user_id as operator_id,
	tpon.bop_audit_user_name as operator_name,
	tpon.bop_audit_time as row_update_time,
	tpon.row_create_time as row_create_time,
	'platfrom' as audit_type
from
	trade_protocol_order_negotiate tpon
where
	tpon.id = 1;

-- mls_order_negotiate_audit
select
	UUID_SHORT() as id,
	tpon.id as order_negotiate_id,
	tpon.bop_audit_type  as `type`,
	tpon.bop_audit_remarks as remark,
	tpon.bop_audit_user_id as operator_id,
	tpon.bop_audit_user_name as operator_name,
	tpon.bop_audit_time as row_create_time,
	tpon.row_update_time as row_update_time,
	'platfrom' as audit_type
from
	trade_protocol_order_negotiate tpon
where
	tpon.id = 1;

select
	UUID_SHORT() as id,
	tpon.id as order_negotiate_id,
	tpon.down_confirm_remarks as remark,
	tpon.down_confirm_user_id as operator_id,
	tpon.down_confirm_user_name as operator_name,
	tpon.down_confirm_time as row_create_time,
	tpon.row_update_time as row_update_time,
	'purchase' as audit_type
from
	trade_protocol_order_negotiate tpon
where
	tpon.id = 1;
	
-- mls_order_transport_agent
select
	UUID_SHORT() as id,
	moac.transport_agent_id,
	moac.transport_agent_code,
	tpo.protocol_order_id as order_id,
	tpo.protocol_order_sn as order_sn,
	moac.bill_amount,
	moac.bill_amount_remain_rate ,
	moac.info_amount,
	moac.info_amount_remain_rate ,
	moac.operator_id,
	moac.operator_name ,
	moac.is_deleted,
	moac.row_create_time ,
	moac.row_update_time
from
	mls_order_agent_config moac
left join trade_protocol_order tpo on
	moac.protocol_order_sn = tpo.protocol_order_sn
where
	moac.id = 1;
	
-- mls_order_price-1
select
	UUID_SHORT() as id,
	tpo.protocol_order_id as order_id,
	tpo.freight_price as price,
	tp.fin_company_id  as company_id,
	tp.fin_company_name as company_name,
	'CARRIER' as company_type,
	'FREIGHT' as price_code,
	'运费(不含税)' as price_name
from
	trade_protocol tp ,
	trade_protocol_order tpo
where
	tpo.protocol_id = tp.protocol_id 
	and tpo.protocol_order_id = '215165519539213272';
	
-- mls_order_price-2 
select
UUID_SHORT() as id,
	tpo.protocol_order_id as order_id,
	tpo.tax_freight_price as price,
	tp.fin_company_id as company_id,
	tp.fin_company_name as company_name,
	'CARRIER' as company_type,
	'TAX_FREIGHT' as price_code,
	'运费(含税)' as price_name
from
	trade_protocol tp ,
	trade_protocol_order tpo
where
	tpo.protocol_id = tp.protocol_id 
	and tpo.protocol_order_id = '215165519539213272';
	
-- mls_order_price-3
select
	UUID_SHORT() as id,
	tpo.protocol_order_id as order_id,
	tpo.tax_unit_price as price,
	tp.supplier_company_id as company_id,
	tp.supplier_company_name as company_name,
	'SUPPLIER' as company_type,
	'TAX_UNIT_PRICE' as price_code,
	'坑口含税价' as price_name
from
	trade_protocol tp ,
	trade_protocol_order tpo
where
	tpo.protocol_id = tp.protocol_id 
	and tpo.protocol_order_id = '215165519539213272';
	
-- mls_order_price-4
select
	UUID_SHORT() as id,
	tpo.protocol_order_id as order_id,
	tpo.price as price,
	tp.fin_company_id as company_id,
	tp.fin_company_name as company_name,
	'FIN' as company_type,
	'MARKET_PRICE' as price_code,
	'含税价(市场价)' as price_name
from
	trade_protocol tp ,
	trade_protocol_order tpo
where
	tpo.protocol_id = tp.protocol_id 
	and tpo.protocol_order_id = '215165519539213272';

-- mls_order_price-5
select
	UUID_SHORT() as id,
	tpo.protocol_order_id as order_id,
	tpo.fix_price as price,
	tp.fin_company_id as company_id,
	tp.fin_company_name as company_name,
	'FIN' as company_type,
	'FIN_PRICE' as price_code,
	'一口价' as price_name
from
	trade_protocol tp ,
	trade_protocol_order tpo
where
	tpo.protocol_id = tp.protocol_id 
	and tpo.protocol_order_id = '215165519539213272';
	
-- mls_order_price-6
select
	UUID_SHORT() as id,
	tpo.protocol_order_id as order_id,
	tpof.platform_service_price as price,
	tp.fin_company_id as company_id,
	tp.fin_company_name as company_name,
	'FIN' as company_type,
	'PLATFORM_SERVICE_PRICE' as price_code,
	'平台服务费' as price_name
from
	trade_protocol tp left join trade_protocol_order tpo on 	tpo.protocol_id = tp.protocol_id 
 left join trade_protocol_order_fee tpof on tpof.protocol_order_id =tpo.protocol_order_id 
where
	 tpo.protocol_order_id = '215165519539213272';

-- mls_order_price-7
select
	UUID_SHORT() as id,
	tpo.protocol_order_id as order_id,
	tpo.discounts_price as price,
	tp.fin_company_id as company_id,
	tp.fin_company_name as company_name,
	'FIN' as company_type,
	'DISCOUNTS_PRICE' as price_code,
	'含税价(优惠价)' as price_name
from
	trade_protocol tp ,
	trade_protocol_order tpo
where
	tpo.protocol_id = tp.protocol_id 
	and tpo.protocol_order_id = '215165519539213272';

-- mls_order_price-8
select
	UUID_SHORT() as id,
	tpo.protocol_order_id as order_id,
	tpo.service_charge as price,
	tp.broker_company_id as company_id,
	tp.broker_company_name as company_name,
	'BROKER' as company_type,
	'SERVICE_CHARGE' as price_code,
	'经纪人代发服务费' as price_name
from
	trade_protocol tp ,
	trade_protocol_order tpo
where
	tpo.protocol_id = tp.protocol_id 
	and tpo.protocol_order_id = '215165519539213272';

-- mls_order_price-9
select
	UUID_SHORT() as id,
	tpo.protocol_order_id as order_id,
	tpof.platform_price as price,
	tp.fin_company_id as company_id,
	tp.fin_company_name as company_name,
	'FIN' as company_type,
	'PLATFORM_PRICE' as price_code,
	'平台含税价' as price_name
from
	trade_protocol tp left join trade_protocol_order tpo on 	tpo.protocol_id = tp.protocol_id 
 left join trade_protocol_order_fee tpof on tpof.protocol_order_id =tpo.protocol_order_id 
where
	 tpo.protocol_order_id = '215165519539213272';
	
-- mls_order_business-1
select
	UUID_SHORT() as id,
	tpo.protocol_order_id as order_id,
	tp.fin_company_id as buyer_company_id,
	tp.fin_company_name as buyer_company_name,
	'FIN' as buyer_company_type,
	tp.supplier_company_id as seller_company_id,
	tp.supplier_company_name as seller_company_name,
	'SUPPLIER' as seller_company_type,
	tp.supplier_user_id as seller_user_id,
	tp.supplier_user_name as seller_user_name,
	tp.supplier_user_phone as seller_user_phone,
	tpo.tax_unit_price as price ,
	1 as business_module
from
	trade_protocol tp ,
	trade_protocol_order tpo
where
	tpo.protocol_id = tp.protocol_id 
	and tpo.protocol_order_id = '215165519539213272';

-- mls_order_business-2 不需要
select
	UUID_SHORT() as id,
	tpo.protocol_order_id as order_id,
	tp.fin_company_id as buyer_company_id,
	tp.fin_company_name as buyer_company_name,
	'FIN' as buyer_company_type,
	tp.broker_company_id as seller_company_id,
	tp.broker_company_name as seller_company_name,
	'BROKER' as seller_company_type,
	tp.broker_user_id as seller_user_id,
	tp.broker_user_name as seller_user_name,
	tp.broker_user_phone as seller_user_phone,
	tpo.tax_unit_price as price ,
	1 as business_module
from
	trade_protocol tp ,
	trade_protocol_order tpo
where
	tpo.protocol_id = tp.protocol_id 
	and tpo.protocol_order_id = '215165519539213272';

-- mls_order_business-3
select
	UUID_SHORT() as id,
	tpof.protocol_order_id as order_id,
	tp.order_company_id as buyer_company_id,
	tp.order_company_name as buyer_company_name,
	'PURCHASE' as buyer_company_type,
	tp.order_user_id as buyer_user_id,
	tp.order_user_name as buyer_user_name,
	tp.order_user_phone as buyer_user_phone,
	tp.fin_company_id as seller_company_id,
	tp.fin_company_name as seller_company_name,
	'FIN' as seller_company_type,
	tpof.platform_price as price ,
	0 as business_module
from
	trade_protocol tp left join trade_protocol_order tpo on 	tpo.protocol_id = tp.protocol_id 
 left join trade_protocol_order_fee tpof on tpof.protocol_order_id =tpo.protocol_order_id 
where
	 tpof.protocol_order_id = '215165519539213272';

	
-- mls_order_relation
select
	UUID_SHORT() as id,
	tpo.protocol_order_id as order_id,
	tp.fin_company_id as fin_company_id,
	tp.fin_company_name as fin_company_name,
	tp.order_company_id as purchase_company_id,
	tp.order_company_name as purchase_company_name,
	tp.supplier_company_id as supplier_company_id,
	tp.supplier_company_name as supplier_company_name,
	tp.broker_company_id as up_agent_company_id,
	tp.broker_company_name as up_agent_company_name
from
	trade_protocol tp ,
	trade_protocol_order tpo
where
	tpo.protocol_id = tp.protocol_id 
	and tpo.protocol_order_id = '215165519539213272';

-- mls_plan_business_extension-1
select
	UUID_SHORT() as id,
	tp.protocol_id as plan_id,
	tp.broker_company_id as service_company_id,
	tp.broker_company_name as service_company_name,
	tp.broker_user_id as service_user_id,
	tp.broker_user_name as service_user_name,
	tp.broker_user_phone as service_user_phone,
	"BROKER" as service_company_type,
	1 as business_module
from
	trade_protocol tp ,
	trade_protocol_order tpo,
	trade_protocol_order_logistics tpol 
where
	tpo.protocol_id = tp.protocol_id 
	and tpol.protocol_order_id =tpo.protocol_order_id 
	and tpo.protocol_order_id = '215165519539213272';


-- mls_plan_business_extension-2
select
	UUID_SHORT() as id,
	tpol.protocol_id  as plan_id,
	tpol.carrier_id as service_company_id,
	tpol.carrier_name as service_company_name,
	0 as service_user_id,
	tpol.carrier_contacts as service_user_name,
	tpol.carrier_phone as service_user_phone,
	"CARRIER" as service_company_type,
	0 as business_module
from
	trade_protocol_order_logistics tpol 
where
 tpol.protocol_order_id = '215165519539213272';


-- mls_order_business_extension-1
select
	UUID_SHORT() as id,
	tpol.protocol_order_id as order_id,
	tp.broker_company_id as service_company_id,
	tp.broker_company_name as service_company_name,
	tp.broker_user_id as service_user_id,
	tp.broker_user_name as service_user_name,
	tp.broker_user_phone as service_user_phone,
	"BROKER" as service_company_type,
	1 as business_module
from
	trade_protocol tp ,
	trade_protocol_order tpo,
	trade_protocol_order_logistics tpol 
where
	tpo.protocol_id = tp.protocol_id 
	and tpol.protocol_order_id =tpo.protocol_order_id 
	and tpo.protocol_order_id = '215165519539213272';


-- mls_order_business_extension-2
select
	UUID_SHORT() as id,
	tpol.protocol_order_id  as order_id,
	tpol.carrier_id as service_company_id,
	tpol.carrier_name as service_company_name,
	0 as service_user_id,
	tpol.carrier_contacts as service_user_name,
	tpol.carrier_phone as service_user_phone,
	"CARRIER" as service_company_type,
	0 as business_module
from
	trade_protocol_order_logistics tpol 
where
 tpol.protocol_order_id = '215165519539213272';
	---------
	update renewscc_dev.trade_protocol_order_negotiate tpl set tpl.row_update_time = now();
	update renewscc_dev.trade_protocol_short_url tpl set tpl.creat_time = now();
	update renewscc_dev.trade_protocol_order_snapshot tpl set tpl.row_update_time = now();
	update renewscc_dev.mls_order_agent_config tpl set tpl.row_update_time = now();
	update renewscc_dev.trade_protocol_order tpl set tpl.update_time = now();
	update renewscc_dev.trade_protocol_order_fee tpl set tpl.row_update_time = now();
	update renewscc_dev.mls_order_agent_config tpl set tpl.row_update_time = now();
	update renewscc_dev.trade_protocol_order_logistics t set t.remark ='data-bus-1';
select protocol_order_sn from mls_order_agent_config moac group by moac.protocol_order_sn  ;
select transport_agent_code from mls_order_agent_config moac group by moac.transport_agent_code  ;
select t.protocol_order_id,t.transport_agent_id,count(*) from(select tpo.protocol_order_id ,moac.transport_agent_code ,moac.transport_agent_id ,moac.protocol_order_sn from mls_order_agent_config moac left join trade_protocol_order tpo on moac.protocol_order_sn =tpo.protocol_order_sn) t group by t.protocol_order_id,t.transport_agent_id ;



