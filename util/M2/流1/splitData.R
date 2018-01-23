splitData<-function(allTrain,seed)
{
 # alltrain的字段为 "user_id","income","entermament","baby","gender","shopping","shop_id","class","user_lon","user_lati","shop_lon",
#  "shop_lati","arrTime","duration"
  set.seed(seed);
  nlen<-nrow(allTrain);
  
  test_c<-c();
  train_c<-c();
  for(index in 1:nlen)
  {
    rand<-sample(0:6,1);
    
    date_info<-getDateInfo(allTrain[index,][13]);
    #根据星期抽，每周抽取一天
    if(date_info[1]==(rand+1))
    {
      test_c<-append(test_c,index);
    }
    else{
      train_c<-append(train_c,index);
    }
    
  }
  train<-allTrain[train_c,];
  test<- allTrain[test_c,];
 # colnames(train)<-c("user_id","income","entermament","baby","gender","shopping","shop_id","class","user_lon","user_lati","shop_lon",
                    # "shop_lati","arrTime","duration")
 # colnames(test)<-c("user_id","income","entermament","baby","gender","shopping","shop_id","class","user_lon","user_lati","shop_lon",
                  #   "shop_lati","arrTime","duration")
  #print(nrow(train))
 # print(nrow(test))
 # if(nrow(train)==0 || nrow(test)==0) return(0);
  return(prePareTrain(train,test));
}