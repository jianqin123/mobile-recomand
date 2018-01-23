preTest<-function(model,onlineData,final,filePro)
{
  
  #onlineData字段为uid,arrTime,.....,sid,
  onlineData<-as.matrix(onlineData);
  #删除无用字段
  #print("删除无用字段")
  #print(colnames(onlineData)[charaDel]);
  #onlineData<-onlineData[,-charaDel];
  
  test_len<-length(onlineData[1,])
  #去重
  #df_test<-df_test[!duplicated(df_test[,c(1,2,test_len)]),];
  # print(train_len
  
  b<-onlineData[,-c(1,2,test_len)];
  test_pro<-predict(model,b);
  #test_pro[1:5];
  #out的字段为uid,arrTime,shopid,label,test_pro
  out<-cbind(onlineData[,c(1,2,test_len)],test_pro);
  
  #计算准确率
  #calCorrecRate(out);
  #先按照uid排序
  out_order<-out[order(out[,1]),];
  #再按照arrivaltime排序
  out_order<-out_order[order(out_order[,2]),];
  #再按照概率排序
  out_order<-out_order[order(out_order[,4],decreasing=T),];
  #出去重复的,重复的为同一个用户同一个时间
  out_result<-out_order[!duplicated(out_order[,c(1,2)]),];
  write.csv(out_result[,c(1,3,2)],final,row.names = FALSE);
  write.csv(out_result[,c(1,3,2,4)],filePro,row.names = FALSE);
}