allTrain<-function(all)
{
  colnames(all)<-c("user_id","income","entermament","baby","gender","shopping","shop_id","class","user_lon","user_lati","shop_lon",
                     "shop_lati","arrTime","duration")
  seed<-45649;
  all[1,]
  x1<-splitData(all,seed)
  for(index in 1:6){
  seed<-seed+232;
  x2<-splitData(all,seed)
 # if(x2==0) next();
  
  x1<-rbind(x1,x2);
  }
 return(x1)
}