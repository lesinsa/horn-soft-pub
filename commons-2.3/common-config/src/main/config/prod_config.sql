-- TODO сменить, когда поднимут сервисы elife в DMZ
insert into mAppConfig(ns, param_name, param_value)
values('ru.prbb.soap-config', 'elifeService', 'http://192.168.113.7:2014/api/v1.0/ElifeBus.svc');

insert into mAppConfig(ns, param_name, param_value)
values('ru.prbb.soap-config', 'elifeService2', 'http://192.168.113.7:2014/api/v2.0/ElifeBus.svc');

insert into mAppConfig(ns, param_name, param_value)
values('ru.prbb.soap-config', 'smsService', 'http://172.16.21.116:8181/services/OutMessageService');

insert into mAppConfig(ns, param_name, param_value)
values('ru.prbb.soap-config', 'soaInfraService', 'http://192.168.109.1:8001/soa-infra/services/default/pcidssWS/PcidssWS');

insert into mAppConfig(ns, param_name, param_value)
values('ru.prbb.soap-config', 'crmService', 'http://172.16.23.7/CrmService/CrmService');