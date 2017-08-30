REM 备份数据，及其相关图片数据

REM 读取系统日期
set ip=${DB_IP}
set port=${DB_PORT}
set username=root
set password=
set database=zcgl
set today=%DATE:~0,4%%DATE:~5,2%%DATE:~8,2%
set corp_name=潮涌科技
set app_id=17
set backup_path=d:\ls
set zclx_img_backup_path=E:/work2/zcgl2/src/main/webapp/upload/zclx_images/%app_id%
set zc_img_backup_path=E:/work2/zcgl2/src/main/webapp/upload/zc_images/%app_id%
set zczt_medias_backup_path=E:/work2/zcgl2/src/main/webapp/media/%app_id%

mkdir %backup_path%\%app_id%

mysql -h%ip% -P%port% -u%username% -p%password% -D%database% < backup_%app_id%.sql

REM 压缩备份指定目录下的图片
REM 分卷压缩，每个文件大小不超过2GB
rar a -m0 -r -v2g %backup_path%/%corp_name%_%today%_数据备份 %backup_path%/%app_id% %zclx_img_backup_path% %zc_img_backup_path% %zczt_medias_backup_path%

REM 清理操作
del /q %backup_path%\%app_id%\*.*
rmdir /q %backup_path%\%app_id%