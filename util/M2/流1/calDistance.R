calDistance<-function(pos1,pos2)
{
  #pos1,pos2字段有两个前一个为经度，后一个为纬度
  lonDiff<-abs(pos1[1]-pos2[1]);
  latiDiff<-abs(pos1[2]-pos2[2]);
  distance<-sqrt(lonDiff*lonDiff+latiDiff*latiDiff);
  return(distance)
}