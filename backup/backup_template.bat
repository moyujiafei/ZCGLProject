REM �������ݣ��������ͼƬ����

REM ��ȡϵͳ����
set ip=${DB_IP}
set port=${DB_PORT}
set username=root
set password=
set database=zcgl
set today=%DATE:~0,4%%DATE:~5,2%%DATE:~8,2%
set corp_name=��ӿ�Ƽ�
set app_id=17
set backup_path=d:\ls
set zclx_img_backup_path=E:/work2/zcgl2/src/main/webapp/upload/zclx_images/%app_id%
set zc_img_backup_path=E:/work2/zcgl2/src/main/webapp/upload/zc_images/%app_id%
set zczt_medias_backup_path=E:/work2/zcgl2/src/main/webapp/media/%app_id%

mkdir %backup_path%\%app_id%

mysql -h%ip% -P%port% -u%username% -p%password% -D%database% < backup_%app_id%.sql

REM ѹ������ָ��Ŀ¼�µ�ͼƬ
REM �־�ѹ����ÿ���ļ���С������2GB
rar a -m0 -r -v2g %backup_path%/%corp_name%_%today%_���ݱ��� %backup_path%/%app_id% %zclx_img_backup_path% %zc_img_backup_path% %zczt_medias_backup_path%

REM �������
del /q %backup_path%\%app_id%\*.*
rmdir /q %backup_path%\%app_id%