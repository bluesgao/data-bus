-- mls_statement_waybill
select
	mws.waybill_id as id,
	mws.sjb_id as waybill_sn,
	mws.waybill_id as waybill_id,
	mws.protocol_order_id as order_id,
	mws.protocol_order_sn as order_sn,
	case
		mws.daily_account_status 
		when 0 then 0
		when 1 then 1
		when 2 then 1
		else 0
	end as is_day,
	case
		mws.month_account_status 
		when 0 then 0
		when 1 then 1
		when 2 then 1
		else 0
	end as is_month,
	bsu.center_id  as operator_id,
	mws.create_user_name as operator_name,
	mws.row_create_time ,
	mws.row_update_time 
from
	mls_waybill_settle mws left join bop_sys_user bsu  on mws.create_user_id=bsu.id
where mws.waybill_id ='215165519539227645';


-- mls_statement-1
select
	mwsd.id as id,
	mwsd.daily_bill_sn as statement_sn,
	0 as statement_type,
	mwsd.check_status as status,
	mwsd.check_time_start as start_time,
	mwsd.check_time_end as end_time,
	mwsd.row_create_time ,
	mwsd.row_update_time 
from
	mls_waybill_settle_daily mwsd
where
	mwsd.daily_bill_sn = 'RD2012080001';

-- mls_statement-2
select
	mwsm.id as id,
	mwsm.month_bill_sn as statement_sn,
	1 as statement_type,
	mwsm.check_status as status,
	mwsm.check_time_start as start_time,
	mwsm.check_time_end as end_time,
	bsu.center_id as operator_id,
	mwsm.create_user_name as operator_name,
	mwsm.row_create_time ,
	mwsm.row_update_time 
from
	mls_waybill_settle_month mwsm left join bop_sys_user bsu  on mwsm.create_user_id=bsu.id
where
	mwsm.month_bill_sn = 'RM2012010001';

-- mls_statement_detail-1
select
    UUID_SHORT() as id,
	mwsd.daily_bill_sn as statement_sn,
	mwsdr.waybill_id as waybill_id,
	mwsdr.sjb_id as waybill_sn,
	tpow.send_count as send_qty,
	tpow.receive_count as receive_qty,
	mwsdr.row_create_time ,
	mwsdr.row_update_time 
from
	mls_waybill_settle_daily_rel mwsdr ,
	mls_waybill_settle_daily mwsd ,
	trade_protocol_order_waybill tpow 
where
   mwsd.id =mwsdr.daily_statement_id 
   and 
	mwsdr.waybill_id=tpow.order_waybill_id 
and  mwsdr.waybill_id ='215165519539221712';


-- mls_statement_detail-2
select
    UUID_SHORT() as id,
	mwsm.month_bill_sn as statement_sn,
	mwsmr.waybill_id as waybill_id,
	mwsmr.sjb_id as waybill_sn,
	tpow.send_count as send_qty,
	tpow.receive_count as receive_qty,
	mwsmr.row_create_time ,
	mwsmr.row_update_time 
from
	mls_waybill_settle_month_rel mwsmr,
	mls_waybill_settle_month mwsm ,
	trade_protocol_order_waybill tpow 
where
	mwsmr.waybill_id=tpow.order_waybill_id 
and mwsm.id =mwsmr.month_statement_id 
and  mwsmr.waybill_id ='215165519539221712';


-- mls_statement_up_agent
select
	mwsd.id as id,
	mwsd.daily_bill_sn as main_statement_sn,
	mwsd.daily_bill_sn as statement_sn,
	mwsd.scp_company_id as buyer_company_id,
	mwsd.scp_company_name as buyer_company_name,
	mwsd.up_agent_id as seller_company_id,
	mwsd.up_agent_name as seller_company_name,
	mwsd.check_status as status,
	mwsd.waybill_count,
	mwsd.waybill_exception_count as exception_waybill_count,
	mwsd.send_count as send_qty,
	mwsd.supplier_receivable_amount *10000 as payable_amount,
	mwsd.row_create_time ,
	mwsd.row_update_time 
from
	mls_waybill_settle_daily mwsd
where
	mwsd.id = 1;

-- mls_statement_up_agent_detail
select
	mwsdr.id as id,
	mwsd.daily_bill_sn as main_statement_sn,
	mwsdr.daily_statement_id as statement_sn,
	mwsdr.waybill_id as waybill_id,
	mws.adjusted_protocol_price as detail_price,
	mws.adjusted_goods_deduction_amount as goods_deduction_amount,
	mws.payable_goods_amount as payable_amount,
	mws.row_create_time ,
	mws.row_update_time 
from
	mls_waybill_settle_daily_rel mwsdr ,
	mls_waybill_settle_daily mwsd,
	mls_waybill_settle mws
where
	mwsdr.waybill_id = mws.waybill_id
	and mwsd.id =mwsdr.daily_statement_id
	and mwsdr.id = 1;


-- mls_statement_buyer
select
	UUID_SHORT() as id,
	mwsm.month_bill_sn as main_statement_sn,
	mwsd.down_bill_sn as statement_sn,
	mwsm.scp_company_id as seller_company_id,
	mwsm.scp_company_name as seller_company_name,
	mwsd.purchase_company_id as buyer_company_id,
	mwsd.purchase_company_name as buyer_company_name,
	mwsd.check_status as status,
	mwsd.waybill_count as waybill_count,
	0 as exception_waybill_count,
	mwsd.send_count as send_qty,
	mwsd.receive_count as receive_qty,
	mwsd.goods_deduction_amount as goods_deduction_amount,
	mwsd.freight_deduction_amount as freight_deduction_amount,
	mwsd.pur_payable_goods_amount + mwsd.pur_paid_freight_amount as payable_amount,
	mwsd.pur_payable_goods_amount as payable_goods_amount,
	mwsd.settle_url as statement_url,
	mwsd.settle_file_size as statement_file_size,
	mwsd.settle_detail_url as statement_detail_url,
	mwsd.settle_detail_file_size as statement_detail_file_size,
	bsu.center_id as operator_id,
	mwsd.create_user_name as operator_name,
	mwsd.row_create_time ,
	mwsd.row_update_time
from
	mls_waybill_settle_month mwsm
left join mls_waybill_settle_down mwsd on
	mwsm.month_bill_sn = mwsd.month_bill_sn left join bop_sys_user bsu  on mwsd.create_user_id=bsu.id
where
	mwsd.down_bill_sn = 'RB2012010002';

-- mls_statement_buyer_detail
select
	UUID_SHORT() as id,
	mwsm.month_bill_sn as main_statement_sn,
	mwsd.down_bill_sn as statement_sn,
	mwsdr.waybill_id as waybill_id,
    mws.adjusted_platform_price as detail_goods_price,
    mws.adjusted_goods_deduction_amount as goods_deduction_amount,
    mws.freight_deduction_amount  as freight_deduction_amount,
    mws.receivable_goods_amount+ mws.receivable_freight_amount as payable_amount,
    mws.receivable_goods_amount as payable_goods_amount,
    mws.row_create_time ,
    mws.row_update_time 
from
	mls_waybill_settle_month mwsm
left join mls_waybill_settle_down mwsd on
	mwsm.month_bill_sn = mwsd.month_bill_sn
left join mls_waybill_settle_down_rel mwsdr on mwsd.id =mwsdr.down_statement_id 
left join mls_waybill_settle mws on mwsdr.waybill_id =mws.waybill_id 
where
	mwsdr.id = 1;
	
-- mls_statement_seller
select
	UUID_SHORT() as id,
	mwsm.month_bill_sn as main_statement_sn,
	mwsm.month_bill_sn as statement_sn,
	mwsm.scp_company_id as buyer_company_id,
	mwsm.scp_company_name as buyer_company_name,
	mwsm.supplier_company_id as seller_company_id,
	mwsm.supplier_company_name as seller_company_name,
	mwsm.waybill_count,
	0 as exception_waybill_count,
	mwsm.send_count as send_qty,
	mwsm.goods_deduction_amount ,
	mwsm.supplier_receivable_amount as payable_amount,
	bsu.center_id as operator_id,
	mwsm.create_user_name as operator_name,
	mwsm.row_create_time ,
	mwsm.row_update_time 
from
	mls_waybill_settle_month mwsm left join bop_sys_user bsu  on mwsm.create_user_id=bsu.id
where 
	mwsm.id = 1;

-- mls_statement_seller_detail
select
	UUID_SHORT() as id,
	mwsm.month_bill_sn as main_statement_sn,
	mwsm.month_bill_sn as statement_sn,
	mwsdr.waybill_id as waybill_id,
    mws.adjusted_protocol_price as detail_price,
    mws.adjusted_goods_deduction_amount as goods_deduction_amount,
    mws.payable_goods_amount as payable_amount,
    mws.row_create_time ,
    mws.row_update_time 
from
	mls_waybill_settle_month mwsm
left join mls_waybill_settle_down mwsd on
	mwsm.month_bill_sn = mwsd.month_bill_sn
left join mls_waybill_settle_down_rel mwsdr on mwsd.id =mwsdr.down_statement_id 
left join mls_waybill_settle mws on mwsdr.waybill_id =mws.waybill_id 
where
	mwsdr.id = 1;

-- mls_statement_log -???????????????
-- mls_statement_carrier ???????????????
-- mls_statement_carrier_detail ???????????????

-- mls_statement_platform_provider ?????????
-- mls_statement_platform_provider_detail ?????????

-- mls_statement_waybill_payment-1
select
    UUID_SHORT() as id,
	mws.waybill_id,
	mws.sjb_id as waybill_sn,
	0 as business_module,
	10 as business_type,
	tp.order_company_id as payer_company_id,
	tp.order_company_name as payer_company_name,
	tp.fin_company_id as payee_company_id,
	tp.fin_company_name as payee_company_name,
	mws.receivable_goods_amount as payable_amount,
	mws.received_goods_amount as actual_amount,
	mws.goods_paid_status as paid_status,
	if(mws.receivable_goods_amount<=0,0,mws.received_goods_amount /mws.receivable_goods_amount)  as paid_scale,
	mws.goods_paid_time as paid_time,
	mws.row_create_time,
	mws.row_update_time 
from
	mls_waybill_settle mws
left join trade_protocol tp on
	mws.protocol_id = tp.protocol_id
where
	mws.id = 1;

-- mls_statement_waybill_payment-2
select
    UUID_SHORT() as id,
	mws.waybill_id,
	mws.sjb_id as waybill_sn,
	0 as business_module,
	20 as business_type,
	tp.order_company_id as payer_company_id,
	tp.order_company_name as payer_company_name,
	tp.fin_company_id as payee_company_id,
	tp.fin_company_name as payee_company_name,
	mws.receivable_freight_amount as payable_amount,
	mws.received_freight_amount as actual_amount,
	mws.freight_paid_status as paid_status,
	if(mws.receivable_freight_amount <=0,0,mws.received_freight_amount /mws.receivable_freight_amount)  as paid_scale,
	mws.freight_paid_time as paid_time,
	mws.row_create_time,
	mws.row_update_time 
from
	mls_waybill_settle mws
left join trade_protocol tp on
	mws.protocol_id = tp.protocol_id
where
	mws.id = 1;

-- mls_statement_waybill_payment-3
select
   UUID_SHORT() as id,
	mws.waybill_id,
	mws.sjb_id as waybill_sn,
	1 as business_module,
	10 as business_type,
	tp.fin_company_id as payer_company_id,
	tp.fin_company_name as payer_company_name,
	tp.supplier_company_id as payee_company_id,
	tp.supplier_company_name as payee_company_name,
	mws.payable_goods_amount as payable_amount,
	mws.paid_goods_amount as actual_amount,
	mws.goods_paid_status as paid_status,
	if(mws.payable_goods_amount <=0,0,mws.paid_goods_amount /mws.payable_goods_amount)  as paid_scale,
	mws.goods_paid_time as paid_time,
	mws.row_create_time,
	mws.row_update_time 
from
	mls_waybill_settle mws
left join trade_protocol tp on
	mws.protocol_id = tp.protocol_id
where
	mws.id = 1;

-- mls_statement_waybill_payment-4
select
UUID_SHORT() as id,
	mws.waybill_id,
	mws.sjb_id as waybill_sn,
	1 as business_module,
	10 as business_type,
	tp.fin_company_id as payer_company_id,
	tp.fin_company_name as payer_company_name,
	tpol.carrier_id as payee_company_id,
	tpol.carrier_name as payee_company_name,
	mws.payable_freight_amount as payable_amount,
	mws.paid_freight_amount as actual_amount,
	mws.freight_settle_status as paid_status,
	if(mws.payable_freight_amount <=0,0,mws.paid_freight_amount /mws.payable_freight_amount)  as paid_scale,
	mws.freight_settle_time as paid_time,
	mws.row_create_time,
	mws.row_update_time 
from
	mls_waybill_settle mws
left join trade_protocol tp on
	mws.protocol_id = tp.protocol_id left join trade_protocol_order_logistics tpol on mws.protocol_order_id = tpol.protocol_order_id 
where
	mws.id = 1;
	
