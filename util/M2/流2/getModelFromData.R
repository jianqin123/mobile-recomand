getMOdelFromData<-function(df_train,depth,mcw,nrou,colsa,subsa)
{
  #train字段包括userid,arrTime,.....,label(),shopid
  #读取数据并装换为矩阵
 # df_train<-read.csv(train,header = TRUE,stringsAsFactors = FALSE);
  df_train<-as.matrix(df_train);
  #删除无用特征
 # print("删除无用特征")
  #print(colnames(df_train)[charaDel]);
 # df_train<-df_train[,-charaDel];
  
 # print(head(df_train,1));
 # print(typeof(df_train));
  train_len<-length(df_train[1,])
  
  p<-nrow(df_train[df_train[,train_len-1]==1,]);
  n<-nrow(df_train[df_train[,train_len-1]==0,]);
  print(paste("去重之前的样本比： ",p/n," 正样本数 ：",p,"  负样本数  ：",n," 总样本数 ：",p+n))
  #去重
 
  #df_train<-df_train[!duplicated(df_train[,c(1,2,train_len)]),];
  
  #p<-nrow(df_train[df_train[,train_len-1]==1,]);
  #n<-nrow(df_train[df_train[,train_len-1]==0,]);
  
  #print(paste("去重之后的样本比： ",p/n," 正样本数 ：",p,"  负样本数  ：",n," 总样本数 ：",p+n))
 
  a<-df_train[,-c(1,2,train_len)];
  return(getModel(a,depth,mcw,nrou,colsa,subsa))
}