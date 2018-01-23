predicWithModelFromData<-function(model,df_test,to,result,filesava)
{
  
    #test为测试集数据的文件路径
    #to为预测结果规范化后的结果，fileSave为全局预测结果，用于分析
    #test字段包括userid,arrTime,.....,label(),shopid
    
    
  
    df_test<-as.matrix(df_test);
     #删除无用字段
    #print("删除无用字段")
    #print(colnames(df_test)[c(3,28,31)])
    #df_test<-df_test[,-c(3,28,31)];
    
    test_len<-length(df_test[1,])
     #去重
    df_test<-df_test[!duplicated(df_test[,c(1,2,test_len)]),];
    # print(train_len
    
    b<-df_test[,-c(1,2,test_len-1,test_len)];
    test_pro<-predict(model,b);
    #test_pro[1:5];
    #out的字段为uid,arrTime,shopid,label,test_pro
   
    out<-cbind(df_test[,c(1,2,test_len,test_len-1)],test_pro);
    
    #计算准确率
    #calCorrecRate(out);
    #先按照uid排序
    out_order<-out[order(out[,5],decreasing = T),];
    #再按照arrivaltime排序
    # print( head(out_order,5) );
     
    #print( head(out_order,5) );
    
    #out_order<-out_order[order(out_order[,2],decreasing = T),];
    #再按照概率排序
   # out_order<-out_order[order(out_order[,5],decreasing=T),];
    write.csv(out_order,"check1.csv");
    #出去重复的,重复的为同一个用户同一个时间
    out_result<-out_order[!duplicated(out_order[,c(1,2)]),];
    write.csv(out_result,"checkResult1.csv");
    
    correct<-nrow(out_result[out_result[,4]==1,]);
    count<-nrow(out_result);
    
    print(paste("正确数目  ：",correct));
    print(paste("实际预测数目 ：",count));
    print(paste("正确率   ：",correct/count))
    
    write.csv(out_result,result,row.names = FALSE);
    write.csv(out_order,to,row.names = FALSE);
    write.csv(cbind(df_test,test_pro),filesava,row.names = FALSE)
    return(correct/count);
  
}