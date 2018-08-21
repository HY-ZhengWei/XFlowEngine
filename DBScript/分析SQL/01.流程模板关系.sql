-- 流程模板关系
SELECT  A.templateID
       ,A.templateName
       ,B.activityRouteID
       ,B.activityRouteCode
       ,B.activityRouteName
       ,E.routeType
       ,B.activityID
       ,B.nextActivityID
       ,C.activityName  + ' - ' + D.activityName  AS "节点A到B"
       ,CT.activityType + ' - ' + DT.activityType AS "节点类型A到B"
  FROM  TTemplate       A
       ,TActivityRoute  B
       ,TActivityInfo   C
       ,TActivityType   CT
       ,TActivityInfo   D
       ,TActivityType   DT
       ,TRouteType      E
 WHERE  A.templateID     = B.templateID
   AND  B.activityID     = C.activityID
   AND  B.nextActivityID = D.activityID
   AND  B.routeTypeID    = E.routeTypeID
   AND  C.activityTypeID = CT.activityTypeID
   AND  D.activityTypeID = DT.activityTypeID
 ORDER  BY A.templateID 
          ,B.activityRouteID;
          
          
 
-- 查看工作流实例
SELECT  A.*
  FROM  TProcess  A
 ORDER  BY A.createTime
          ,A.operateTime