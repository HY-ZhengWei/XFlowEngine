CREATE USER XFlow 
 IDENTIFIED BY "xflow"  
    DEFAULT TABLESPACE "XFLOW_DATA"
  TEMPORARY TABLESPACE "TEMP";



ALTER USER XFlow DEFAULT ROLE ALL;
GRANT UNLIMITED TABLESPACE TO XFlow WITH ADMIN OPTION;
GRANT CONNECT              TO XFlow;
GRANT CREATE SESSION       TO XFlow;
GRANT ALTER  SESSION       TO XFlow;
GRANT CREATE ANY TABLE     TO XFlow WITH ADMIN OPTION;
GRANT ALTER  ANY TABLE     TO XFlow WITH ADMIN OPTION;
GRANT DROP   ANY TABLE     TO XFlow WITH ADMIN OPTION;

GRANT CREATE ANY INDEX     TO XFlow WITH ADMIN OPTION;
GRANT ALTER  ANY INDEX     TO XFlow WITH ADMIN OPTION;
GRANT DROP   ANY INDEX     TO XFlow WITH ADMIN OPTION;

GRANT CREATE ANY VIEW      TO XFlow WITH ADMIN OPTION;
GRANT DROP   ANY VIEW      TO XFlow WITH ADMIN OPTION;

GRANT CREATE ANY PROCEDURE TO XFlow WITH ADMIN OPTION;
GRANT DROP   ANY PROCEDURE TO XFlow WITH ADMIN OPTION;

GRANT CREATE ANY SEQUENCE  TO XFlow WITH ADMIN OPTION;


GRANT SELECT ON SYS.V_$MYSTAT     TO XFlow;
GRANT SELECT ON SYS.V_$SESSION    TO XFlow;
GRANT SELECT ON SYS.V_$Lock       TO XFlow;
GRANT SELECT ON SYS.V_$PROCESS    TO XFlow;
GRANT SELECT ON SYS.V_$ACCESS     TO XFlow;
GRANT SELECT ON SYS.DBA_DDL_LOCKS TO XFlow;
GRANT SELECT ON SYS.DBA_JOBS      TO XFlow;
GRANT SELECT ON DBA_OBJECTS       TO XFlow;
