
-- mls_waybill
select
	tpow.order_waybill_id as id,
	tpow.sjb_id as waybill_sn,
	tpow.protocol_order_id as order_id,
	tpow.protocol_order_sn as order_sn,
	tpow.status as status ,
	tpow.verify_status as up_status,
	tpow.purchase_status as down_status,
	tpow.delivery_pay_type as delivery_pay_type,
	tpo.payment_path as up_pay_type,
	tpo.down_business_type as down_pay_type,
	tpow.formula_type as formula_type,
	IF(ISNULL(tpg.goods_id) OR tpg.goods_id='',0,tpg.goods_id) as goods_id,
	tpow.goods_name as goods_name ,
	ifnull(tgb.coalmine_id, 0) as coalmine_id,
	tpow.coalmine_name as coalmine_name ,
	tpow.plate_number ,
	tpow.driver_code ,
	tpow.driver_name,
	tpow.driver_phone,
	tpow.source_address,
	tpow.target_address,
	ifnull(UNIX_TIMESTAMP(tpow.sjb_send_time), 0)  as send_time,
	ifnull(UNIX_TIMESTAMP(tpow.source_sign_time), 0)  as source_time,
	ifnull(UNIX_TIMESTAMP(tpow.target_sign_time), 0)  as target_time,
	tpow.offline_flag as waybill_source,
	tpow.snapshot_version ,
	tpow.create_time as row_create_time,
	tpow.create_time as row_update_time,
	tpow.tax_rate as tax_rate,
	tpow.goods_count as goods_qty,
	tpow.source_address as source_address_nickname,
	tpow.target_address as target_address_nickname
from
	trade_protocol_order_waybill tpow
left join trade_protocol_order tpo on
	tpow.protocol_order_id = tpo.protocol_order_id
left join trade_protocol_goods tpg on
	tpow.protocol_id = tpg.protocol_id
left join trade_goods_base tgb on
	tgb.goods_id = tpg.goods_id
where
	tpow.order_waybill_id = '215165519539219674';


-- mls_waybill_log 暂时不处理

-- mls_waybill_kafka_backup
select
    twkb.id as id,
	twkb.sjb_id as waybill_sn,
	twkb.message_type ,
	twkb.message,
	twkb.remark,
	twkb.waybill_status,
	twkb.retry_status,
	twkb.row_create_time,
	twkb.row_update_time
from
	trade_waybill_kafka_backup twkb
where
	twkb.sjb_id = '2020080412121400';



-- mls_waybill_sign-1
 select
    UUID_SHORT() as id,
	tpow.order_waybill_id as waybill_id,
	0 as business_module,
	twv.real_send_num as send_goods_qty,
	twv.receive_qty as receipts_goods_qty,
	ifnull(UNIX_TIMESTAMP(tpow.sign_time), 0)  as sign_time,
	tpow.purchase_status as status,
	twv.force_sign_remark,
	tp.order_company_id as operator_company_id,
	tp.order_company_name as operator_company_name,
	tp.order_user_id as operator_id,
	tp.order_user_name as operator_name,
	twv.create_time as row_create_time,
	twv.update_time as row_update_time
from
	trade_protocol_order_waybill tpow ,
	trade_waybill_verify twv,
	trade_protocol tp 
where
	tpow.sjb_id = twv.waybill_no
	and tp.protocol_id =tpow.protocol_id 
	and twv.company_type = 'PURCHASE'
	and twv.waybill_no ='2006291017500001';

-- mls_waybill_sign-2
 select
    UUID_SHORT() as id,
	tpow.order_waybill_id as waybill_id,
	2 as business_module,
	twv.real_send_num as send_goods_qty,
	twv.receive_qty as receipts_goods_qty,
	ifnull(UNIX_TIMESTAMP(tpow.sign_time), 0)  as sign_time,
	ifnull(UNIX_TIMESTAMP(tpow.sign_over_time), 0)  as sign_over_time,
	tpow.status as status,
	tpow.sign_flag as is_force_sign,
	twv.create_time as row_create_time,
	twv.update_time as row_update_time
from
	trade_protocol_order_waybill tpow ,
	trade_waybill_verify twv
where
	tpow.sjb_id = twv.waybill_no
	and twv.company_type = 'FIN'
	and twv.waybill_no ='215165519539223826';


-- mls_waybill_sign-3
 select
    UUID_SHORT() as id,
	tpow.order_waybill_id as waybill_id,
	1 as business_module,
	tpow.verify_status as status,
	twv.real_send_num as send_goods_qty,
	twv.receive_qty as receipts_goods_qty,
	ifnull(UNIX_TIMESTAMP(tpow.sign_time), 0)  as sign_time,
	IF(ISNULL(tp.broker_company_id) OR tp.broker_company_id='',0,tp.broker_company_id) as operator_company_id,
	tp.broker_company_name as operator_company_name,
	IF(ISNULL(tp.broker_user_id) OR tp.broker_user_id='',0,tp.broker_user_id) as operator_id,
	tp.broker_user_name as operator_name,
	twv.create_time as row_create_time,
	twv.update_time as row_update_time
from
	trade_protocol_order_waybill tpow ,
	trade_waybill_verify twv,
	trade_protocol tp 
where
	tpow.sjb_id = twv.waybill_no
	and tp.protocol_id =tpow.protocol_id 
	and twv.company_type = 'BROKER'
	and twv.waybill_no ='215165519539222541';

update renewscc_dev.trade_waybill_verify twv set twv.coal_remark ='';

-- mls_waybill_sign_file-1
 select
    twva.attach_id as id,
	tpow.order_waybill_id as waybill_id,
	0 as business_module,
	twva.attach_type  as file_type,
	twva.img_url as file_url,
	twva.del_flag as is_deleted,
	twva.create_date as row_create_time,
	twva.create_date as row_update_time
from
	trade_protocol_order_waybill tpow ,
	trade_waybill_verify twv,
	trade_waybill_verify_attach twva 
where
	tpow.sjb_id = twv.waybill_no 
	and twv.verify_id =twva.verify_id 
	and twv.company_type = 'BROKER'
	and twva.del_flag =0
	and tpow.order_waybill_id ='215165519539222541';

-- mls_waybill_sign_file-2
 select
    twva.attach_id as id,
	tpow.order_waybill_id as waybill_id,
	1 as business_module,
	twva.attach_type  as file_type,
	twva.img_url as file_url,
	twva.del_flag as is_deleted,
	twva.create_date as row_create_time,
	twva.create_date as row_update_time
from
	trade_protocol_order_waybill tpow ,
	trade_waybill_verify twv,
	trade_waybill_verify_attach twva 
where
	tpow.sjb_id = twv.waybill_no 
	and twv.verify_id =twva.verify_id 
	and twv.company_type = 'PURCHASE'
	and twva.del_flag =0
	and tpow.order_waybill_id ='215165519539222541';

update trade_protocol_order_waybill tpow set tpow.remark='test11' where tpow.order_waybill_id in('215165519539222541');


-- mls_waybill_sign_up_extension
 select
    UUID_SHORT() as id,
	tpow.order_waybill_id as waybill_id,
	twv.tool_number,
	twv.create_time as row_create_time,
	twv.update_time as row_update_time
from
	trade_protocol_order_waybill tpow ,
	trade_waybill_verify twv
where
	tpow.sjb_id = twv.waybill_no
	and twv.company_type = 'BROKER'
	and tpow.order_waybill_id ='215165519539222541';
	
-- mls_waybill_sign_down_extension
 select
    UUID_SHORT() as id,
	tpow.order_waybill_id as waybill_id,
	twv.goods_deduction_amount as coal_deduction_amount,
	twv.coal_deduction_reason as coal_deduction_type,
	twv.coal_remark as coal_deduction_remark,
	tpow.coal_duct_status as coal_deduction_status,
	twv.freight_deduction_amount as freight_deduction_amount,
	twv.deduction_reason as freight_deduction_type,
	twv.remark as freight_deduction_remark,
	tpow.freight_duct_status as freight_deduction_status,
	twv.rule_name as rule_name,
	twv.logistics_rule_code as formula_code,
	twv.formula_code as formula_name,
	twv.constant_param_json as constant_param_json,
	twv.variate_param_json as variate_param_json,
	twv.create_time as row_create_time,
	twv.update_time as row_update_time
from
	trade_protocol_order_waybill tpow ,
	trade_waybill_verify twv
where
	tpow.sjb_id = twv.waybill_no
	and twv.company_type = 'PURCHASE'
	and tpow.order_waybill_id ='215165519539222541';


update trade_protocol_order_waybill tpow set tpow.remark='test' where tpow.order_waybill_id in('215165519539222541');
	
-- mls_waybill_price-1
select
	UUID_SHORT() as id,
	tpow.order_waybill_id as waybill_id,
	if(isnull(tp.broker_company_id) or tp.broker_company_id='',0,tp.broker_company_id) as company_id,
	tp.broker_company_name as company_name,
	'BROKER' as company_type,
	tpow.service_charge as price,
	'SERVICE_CHARGE' as price_code,
	'上游经纪人代发费价格' as price_name,
	tpow.create_time as row_create_time,
	tpow.create_time as row_update_time
from
	trade_protocol_order_waybill tpow ,
	trade_protocol tp
where
	tp.protocol_id = tpow.protocol_id
	and tpow.order_waybill_id = '215165519539218733';

-- mls_waybill_price-2
select
    UUID_SHORT() as id,
	tpow.order_waybill_id as waybill_id,
	tp.order_company_id as company_id,
	tp.order_company_name as company_name,
	'PURCHASE' as company_type,
	tpow.platform_price as price,
	'PLATFORM_PRICE' as price_code,
	'采购商平台含税价格' as price_name,
	tpow.create_time as row_create_time,
	tpow.create_time as row_update_time
from
	trade_protocol_order_waybill tpow ,
	trade_protocol tp
where
	tp.protocol_id = tpow.protocol_id
	and tpow.order_waybill_id = '215165519539218733';


-- mls_waybill_price-3
select
    UUID_SHORT() as id,
	tpow.order_waybill_id as waybill_id,
	tp.fin_company_id as company_id,
	tp.fin_company_name as company_name,
	'FIN' as company_type,
	tpow.platform_service_price as price,
	'PLATFORM_SERVICE_PRICE' as price_code,
	'平台服务费价格' as price_name,
	tpow.create_time as row_create_time,
	tpow.create_time as row_update_time
from
	trade_protocol_order_waybill tpow ,
	trade_protocol tp
where
	tp.protocol_id = tpow.protocol_id
	and tpow.order_waybill_id = '215165519539218733';


-- mls_waybill_price-4
select
    UUID_SHORT() as id,
	tpow.order_waybill_id as waybill_id,
	tp.supplier_company_id as company_id,
	tp.supplier_company_id as company_name,
	'SUPPLIER' as company_type,
	tpow.protocol_price as price,
	'PROTOCOL_PRICE' as price_code,
	'供应商坑口含税价' as price_name,
	tpow.create_time as row_create_time,
	tpow.create_time as row_update_time
from
	trade_protocol_order_waybill tpow ,
	trade_protocol tp
where
	tp.protocol_id = tpow.protocol_id
	and tpow.order_waybill_id = '215165519539218733';


-- mls_waybill_price-5
select
    UUID_SHORT() as id,
	tpow.order_waybill_id as waybill_id,
	tp.order_company_id as company_id,
	tp.order_company_id as company_name,
	'PURCHASE' as company_type,
	tpow.freight_price as price,
	'FREIGHT_PRICE' as price_code,
	'采购商运价不含税' as price_name,
	tpow.create_time as row_create_time,
	tpow.create_time as row_update_time
from
	trade_protocol_order_waybill tpow ,
	trade_protocol tp
where
	tp.protocol_id = tpow.protocol_id
	and tpow.order_waybill_id = '215165519539218733';
	
-- mls_waybill_price-6 不要了
select
    UUID_SHORT() as id,
	tpow.order_waybill_id as waybill_id,
	tp.order_company_id as company_id,
	tp.order_company_id as company_name,
	'PURCHASE' as company_type,
	tpow.tax_rate as price,
	'TAX_RATE' as price_code,
	'采购商运价税率' as price_name,
	tpow.create_time as row_create_time,
	tpow.create_time as row_update_time
from
	trade_protocol_order_waybill tpow ,
	trade_protocol tp
where
	tp.protocol_id = tpow.protocol_id
	and tpow.order_waybill_id = '215165519539218733';

-- mls_waybill_price-7
select
    UUID_SHORT() as id,
	tpow.order_waybill_id as waybill_id,
	tp.order_company_id as company_id,
	tp.order_company_id as company_name,
	'PURCHASE' as company_type,
	tpow.goods_price as price,
	'GOODS_PRICE' as price_code,
	'一票优惠价' as price_name,
	tpow.create_time as row_create_time,
	tpow.create_time as row_update_time
from
	trade_protocol_order_waybill tpow ,
	trade_protocol tp
where
	tp.protocol_id = tpow.protocol_id
	and tpow.order_waybill_id = '215165519539218733';


-- mls_waybill_price-8
select
    UUID_SHORT() as id,
	tpow.order_waybill_id as waybill_id,
	tp.order_company_id as company_id,
	tp.order_company_id as company_name,
	'PURCHASE' as company_type,
	tpow.fix_price as price,
	'FIX_PRICE' as price_code,
	'一口价' as price_name,
	tpow.create_time as row_create_time,
	tpow.create_time as row_update_time
from
	trade_protocol_order_waybill tpow ,
	trade_protocol tp
where
	tp.protocol_id = tpow.protocol_id
	and tpow.order_waybill_id = '215165519539218733';

update renewscc_dev.trade_protocol_order_waybill tpow set tpow.remark='test12345' where tpow.order_waybill_id in('215165519539218733');


-- mls_waybill_amount-1
select
    UUID_SHORT() as id,
	tpow.order_waybill_id as waybill_id,
	tpol.carrier_id as company_id,
	tpol.carrier_name as company_name,
	tpow.sjb_order_real_price as amount,
	'SJB_ORDER_REAL_AMOUNT' as amount_code,
	'实付司机金额' as amount_name,
	tpow.create_time as row_create_time,
	tpow.create_time as row_update_time
from
	trade_protocol_order_waybill tpow ,
	trade_protocol_order_logistics tpol
where
	tpol.protocol_order_id = tpow.protocol_order_id
	and tpow.order_waybill_id = '215165519539218733';

-- mls_waybill_amount-2
select
    UUID_SHORT() as id,
	tpow.order_waybill_id as waybill_id,
	tpol.carrier_id as company_id,
	tpol.carrier_name as company_name,
	tpow.schdule_fee*10000 as amount,
	'SCHDULE_FEE_AMOUNT' as amount_code,
	'调度费' as amount_name,
	tpow.create_time as row_create_time,
	tpow.create_time as row_update_time
from
	trade_protocol_order_waybill tpow ,
	trade_protocol_order_logistics tpol
where
	tpol.protocol_order_id = tpow.protocol_order_id
	and tpow.order_waybill_id = '215165519539218733';

-- mls_waybill_amount-3
select
    UUID_SHORT() as id,
	tpow.order_waybill_id as waybill_id,
	tp.order_company_id as company_id,
	tp.order_company_name as company_name,
	tpow.frozen_price *10000 as amount,
	'FROZEN_AMOUNT' as amount_code,
	'冻结金额' as amount_name,
	tpow.create_time as row_create_time,
	tpow.create_time as row_update_time
from
	trade_protocol_order_waybill tpow ,
	trade_protocol tp
where
	tp.protocol_id = tpow.protocol_id 
	and tpow.order_waybill_id = '215165519539218733';


-- mls_waybill_payment-1
select
    UUID_SHORT() as id,
	tpow.order_waybill_id as waybill_id,
	0 as business_module,
	10 as business_type,
	tp.order_company_id as payer_company_id,
	tp.order_company_name as payer_company_name,
	tp.fin_company_id as payee_company_id,
	tp.fin_company_name as payee_company_name,
	tpow.payment_goods_amount as payable_amount,
	tpow.create_time as row_create_time,
	tpow.create_time as row_update_time
from
	trade_protocol_order_waybill tpow ,
	trade_protocol tp
where
	tp.protocol_id = tpow.protocol_id 
	and tpow.payment_fix_amount=0
	and tpow.order_waybill_id = '215165519539218733';


-- mls_waybill_payment-2
select
    UUID_SHORT() as id,
	tpow.order_waybill_id as waybill_id,
	0 as business_module,
	20 as business_type,
	tp.order_company_id as payer_company_id,
	tp.order_company_name as payer_company_name,
	tp.fin_company_id as payee_company_id,
	tp.fin_company_name as payee_company_name,
	tpow.payment_freight_amount as payable_amount,
	tpow.create_time as row_create_time,
	tpow.create_time as row_update_time
from
	trade_protocol_order_waybill tpow ,
	trade_protocol tp
where
	tp.protocol_id = tpow.protocol_id 
	and tpow.payment_fix_amount=0
	and tpow.order_waybill_id = '215165519539218733';
	
-- mls_waybill_payment-3
select
    UUID_SHORT() as id,
	tpow.order_waybill_id as waybill_id,
	0 as business_module,
	0 as business_type,
	tp.order_company_id as payer_company_id,
	tp.order_company_name as payer_company_name,
	tp.fin_company_id as payee_company_id,
	tp.fin_company_name as payee_company_name,
	tpow.payment_fix_amount  as payable_amount,
	tpow.create_time as row_create_time,
	tpow.create_time as row_update_time
from
	trade_protocol_order_waybill tpow ,
	trade_protocol tp
where
	tp.protocol_id = tpow.protocol_id 
	and tpow.payment_fix_amount>0
	and tpow.order_waybill_id = '215165519539218733';
	

-- mls_waybill_payment-4
select
    UUID_SHORT() as id,
	tpow.order_waybill_id as waybill_id,
	2 as business_module,
	40 as business_type,
	tp.fin_company_id as payer_company_id,
	tp.fin_company_name as payer_company_name,
	tp.fin_company_id as payee_company_id,
	tp.fin_company_name as payee_company_name,
	tpow.platform_service_amount as payable_amount,
	tpow.create_time as row_create_time,
	tpow.create_time as row_update_time
from
	trade_protocol_order_waybill tpow ,
	trade_protocol tp
where
	tp.protocol_id = tpow.protocol_id 
	and tpow.order_waybill_id = '215165519539218733';
	

-- mls_waybill_payment-5
select
    UUID_SHORT() as id,
	tpow.order_waybill_id as waybill_id,
	1 as business_module,
	50 as business_type,
	tp.fin_company_id as payer_company_id,
	tp.fin_company_name as payer_company_name,
	IF(ISNULL(tp.broker_company_id) OR tp.broker_company_id='',0,tp.broker_company_id) as payee_company_id,
	IF(ISNULL(tp.broker_company_name) OR tp.broker_company_name='','',tp.broker_company_name) as payee_company_name,
	tpow.payable_service_charge_amount as payable_amount,
	tpow.create_time as row_create_time,
	tpow.create_time as row_update_time
from
	trade_protocol_order_waybill tpow ,
	trade_protocol tp
where
	tp.protocol_id = tpow.protocol_id
	and (tp.broker_company_id is not null or tp.broker_company_name is not null)
	and tpow.order_waybill_id = '215165519539232082';
	
-- mls_waybill_payment-6
select
    UUID_SHORT() as id,
	tpow.order_waybill_id as waybill_id,
	1 as business_module,
	10 as business_type,
	tp.fin_company_id as payer_company_id,
	tp.fin_company_name as payer_company_name,
	tp.supplier_company_id as payee_company_id,
	tp.supplier_company_name as payee_company_name,
	tpow.payable_goods_amount as payable_amount,
	tpow.create_time as row_create_time,
	tpow.create_time as row_update_time
from
	trade_protocol_order_waybill tpow ,
	trade_protocol tp
where
	tp.protocol_id = tpow.protocol_id 
	and tpow.order_waybill_id = '215165519539218733';
	

-- mls_waybill_payment-7
select
    UUID_SHORT() as id,
	tpow.order_waybill_id as waybill_id,
	1 as business_module,
	20 as business_type,
	tp.fin_company_id as payer_company_id,
	tp.fin_company_name as payer_company_name,
	tpol.carrier_id as payee_company_id,
	tpol.carrier_name as payee_company_name,
	tpow.payment_sjb_price *10000 as payable_amount,
	tpow.create_time as row_create_time,
	tpow.create_time as row_update_time
from
	trade_protocol_order_waybill tpow ,
	trade_protocol tp,
	trade_protocol_order_logistics tpol 
where
	tp.protocol_id = tpow.protocol_id 
and tpow.protocol_order_id =tpol.protocol_order_id 
	and tpow.order_waybill_id = '215165519539218733';
	