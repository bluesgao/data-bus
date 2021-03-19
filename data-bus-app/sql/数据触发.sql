-- 新增列
-- 计划
ALTER TABLE trade_protocol_logistics ADD COLUMN `data_sync` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据同步时间';

ALTER TABLE trade_protocol_goods ADD COLUMN `data_sync` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据同步时间';

-- 订单
ALTER TABLE trade_protocol_log ADD COLUMN `data_sync` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据同步时间';

ALTER TABLE trade_protocol_order_logistics ADD COLUMN `data_sync` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据同步时间';

ALTER TABLE trade_protocol_order_negotiate ADD COLUMN `data_sync` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据同步时间';

ALTER TABLE trade_protocol_short_url ADD COLUMN `data_sync` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据同步时间';

ALTER TABLE trade_protocol_order_snapshot ADD COLUMN `data_sync` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据同步时间';

-- 更新列
update trade_protocol_logistics twv set twv.data_sync =now();
update trade_protocol_goods twv set twv.data_sync =now();
update trade_protocol_log twv set twv.data_sync =now();
update trade_protocol_order_logistics twv set twv.data_sync =now();
update trade_protocol_order_negotiate twv set twv.data_sync =now();
update trade_protocol_short_url twv set twv.data_sync =now();
update trade_protocol_order_snapshot twv set twv.data_sync =now();

update trade_protocol_order_fee tpof set tpof.row_update_time = now();

-- 运单
ALTER TABLE trade_protocol_order_waybill ADD COLUMN `data_sync` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据同步时间';
ALTER TABLE trade_waybill_kafka_backup ADD COLUMN `data_sync` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据同步时间';
ALTER TABLE trade_waybill_verify ADD COLUMN `data_sync` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据同步时间';

update trade_protocol_order_waybill twv set twv.data_sync =now();
update trade_waybill_kafka_backup twv set twv.data_sync =now();
update trade_waybill_verify twv set twv.data_sync =now();

DELETE FROM wyw_dev.mls_waybill_sign_down_extension;
DELETE FROM wyw_dev.mls_waybill_sign_up_extension;
DELETE FROM wyw_dev.mls_waybill_amount ;

select * from trade_protocol_order_waybill t where t.order_waybill_id ='215165519539228542';
update trade_protocol_order_waybill twv set twv.data_sync =now()  where twv.order_waybill_id ='215165519539222775';


-- 结算
update mls_waybill_settle mws set mws.data_sync =now();
update mls_waybill_settle_daily mws set mws.data_sync =now();
update mls_waybill_settle_month mws set mws.data_sync =now();

update mls_waybill_settle_daily_rel mws set mws.data_sync =now();
update mls_waybill_settle_month_rel mws set mws.data_sync =now();

update mls_waybill_settle_down mws set mws.data_sync =now();
update mls_waybill_settle_down_rel mws set mws.data_sync =now();

ALTER TABLE mls_waybill_settle ADD COLUMN `data_sync` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据同步时间';
ALTER TABLE mls_waybill_settle_daily ADD COLUMN `data_sync` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据同步时间';
ALTER TABLE mls_waybill_settle_month ADD COLUMN `data_sync` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据同步时间';
ALTER TABLE mls_waybill_settle_daily_rel ADD COLUMN `data_sync` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据同步时间';
ALTER TABLE mls_waybill_settle_month_rel ADD COLUMN `data_sync` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据同步时间';
ALTER TABLE mls_waybill_settle_down ADD COLUMN `data_sync` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据同步时间';
ALTER TABLE mls_waybill_settle_down_rel ADD COLUMN `data_sync` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据同步时间';

