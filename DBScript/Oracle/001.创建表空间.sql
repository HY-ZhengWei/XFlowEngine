-- 数据表空间
CREATE TABLESPACE XFLOW_DATA
         DATAFILE 'D:\Software\oracle\oradata\orcl\XFLOW_DATA01.DBF' 
             SIZE 209715200 
       AUTOEXTEND ON NEXT 104857600 
          MAXSIZE UNLIMITED
          LOGGING
           ONLINE;



-- 索引表空间
CREATE TABLESPACE XFLOW_IDX
         DATAFILE 'D:\Software\oracle\oradata\orcl\XFLOW_IDX01.DBF' 
             SIZE 209715200 
       AUTOEXTEND ON NEXT 104857600 
          MAXSIZE UNLIMITED
          LOGGING
           ONLINE;
