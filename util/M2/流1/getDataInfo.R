getDateInfo<-function(dat)
{
  #dat×Ö¶Î±ÈÈçËµ20140819123409
#print(dat)
if(dat>10000){
data_time<- as.POSIXlt(toString(dat),format="%Y%m%d%H%M%S");
}
else{
data_time<- as.POSIXlt(toString(paste("201408",dat)),format="%Y%m%d%H");
}
m_day<-data_time$mday;
hour<-data_time$h;
week<-data_time$wday;

if(week==0) week<-7;

#print(hour)
area<-0;
if(hour<7 || hour>22)   {area<-0;}
 else if(hour<=12 & hour>=7)  { area<-1;}
 else if(hour<=14 & hour>12)  { area<-2;}
 else if (hour>14 &hour<19)      { area<-3;}
 else if(hour>=19 & hour<=22)   { area<-4;}
return(c(week,hour,area))

  
  
}