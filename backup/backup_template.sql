select * from c_jzlx into outfile "${BACKUP_PATH}/${APP_ID}/c_jzlx.txt";

select * from c_xq where app_id = ${APP_ID} into outfile "${BACKUP_PATH}/${APP_ID}/c_xq.txt";

