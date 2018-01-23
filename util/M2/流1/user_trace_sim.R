user_trace_sim<-function(user,alltrain,d_array,modeles)
{
  #user字段为"user_id","income","entermament","baby","gender","shopping","user_lon","user_lati","arrTime","duration"
  #allTrain字段为"user_id","income","entermament","baby","gender","shopping","shop_id","class","user_lon","user_lati","shop_lon",
 # "shop_lati","arrTime","duration"
  len<-nrow(alltrain)
  user_lon<-user[7]$user_lon;
  user_lati<-user[8]$user_lati;
  user_labels<-user[2:6];
  
  #挑选出符合一些列规则的
  for(d in d_array )
  {
    #挑选出距离符合规定的
    a<-alltrain[abs(alltrain$user_lon-user_lon)<=d & abs(alltrain$user_lati-user_lati)<=d &
                  alltrain$shop_lon>user_lon & alltrain$shop_lati>user_lati,]
    #挑选出符合模型规则的
    b<-a[supportModel(user_labels,a$class,modeles),]
   # distances<-calDistance(c(user_lon,user_lati),b[,11:12]);
   # print(distances)
    #按照用户和商户之间的距离排序
    b<-b[order(calDistance(c(user_lon,user_lati),b[,11:12])),]
    #print(b)
    #print(nrow(b))
    #如果备选集足够，则不扩大d
    if(nrow(b)>4) break;
    
  }
  return(b);
  
  
}