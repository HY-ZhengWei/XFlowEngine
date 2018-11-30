UPDATE  A
   SET  A.x = B.x
       ,A.y = B.y
       ,A.backgroudColor = B.backgroudColor
       ,A.flagColor      = B.flagColor
       ,A.lineColor      = B.lineColor
  FROM  TActivityInfo  A
       ,TActivityInfo  B
 WHERE  A.activityID LIKE '%V3'
   AND  B.activityID LIKE '%V2'
   AND  A.activityCode = B.activityCode