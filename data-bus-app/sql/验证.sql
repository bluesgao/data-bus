delete from wyw_dev.mls_plan ;
delete from wyw_dev.mls_plan_business;
delete from wyw_dev.mls_plan_log ;



select count(*) from wyw_dev.mls_plan_log ;
select count(*) from wyw_dev.mls_plan ;
select count(*) from wyw_dev.mls_plan_business ;
select count(*) from wyw_dev.mls_plan_logistics mpl ;
select count(*) from wyw_dev.mls_plan_goods mpg ;


show variables like '%time_zone%';


delete from wyw_dev.mls_order ;
delete from wyw_dev.mls_order_negotiate;
delete from wyw_dev.mls_order_log ;
delete from wyw_dev.mls_order_contract ;
delete from wyw_dev.mls_order_logistics ;

select count(*) from wyw_dev.mls_order ;
select count(*) from wyw_dev.mls_order_negotiate ;
select count(*) from wyw_dev.mls_order_log ;
select count(*) from wyw_dev.mls_order_contract ;
select count(*) from wyw_dev.mls_order_logistics ;



select count(*) from wyw_dev.mls_order_short_url ;
select count(*) from wyw_dev.mls_order_snapshot ;
select count(*) from wyw_dev.mls_order_negotiate_audit ;
select count(*) from wyw_dev.mls_order_price ;
select count(*) from wyw_dev.mls_order_business ;
select count(*) from wyw_dev.mls_order_transport_agent ;
select count(*) from wyw_dev.mls_order_relation  ;
select count(*) from wyw_dev.mls_plan_business_extension  ;
select count(*) from wyw_dev.mls_order_business_extension  ;

select * from wyw_dev.mls_order_goods mog where length(mog.goods_service_type)>1; 

delete from wyw_dev.mls_order_transport_agent ;



INSERT INTO mls_waybill_price (id, waybill_id, company_id, company_name, company_type, price, price_code, price_name, row_create_time, row_update_time) VALUES ('5215183955335073043', '215165519539218733', '16887888549595061058', '森林小镇采购商', 'PURCHASE', '10000', 'platform_price', '采购商平台含税价格', '2019-06-20 03:06:29.0', '2019-06-20 03:06:29.0') ON DUPLICATE KEY UPDATE id = '5215183955335073043', waybill_id = '215165519539218733', company_id = '16887888549595061058', company_name = '森林小镇采购商', company_type = 'PURCHASE', price = '10000', price_code = 'platform_price', price_name = '采购商平台含税价格', row_create_time = '2019-06-20 03:06:29.0', row_update_time = '2019-06-20 03:06:29.0'



SELECT * FROM mls_order_business mob where mob.order_id ='888520248084136734';



select * from mls_waybill twv  where twv.id ='215165519539222775';


