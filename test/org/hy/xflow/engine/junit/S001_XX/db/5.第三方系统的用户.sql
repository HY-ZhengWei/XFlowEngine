SELECT  A.modelID   AS userID
       ,A.userName  
       ,A.roleID
       ,B.roleName
       ,A.orgID
       ,C.orgName
  FROM  Sys_User  A
       ,Sys_Role  B
       ,Sys_Org   C
 WHERE  A.orgID   = C.modelID
   AND  A.roleID  = B.modelID
   AND  A.userName LIKE '%霍%'