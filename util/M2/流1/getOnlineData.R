getOnlineData<-function(trainData,testData,fileadd)
{
#options(digits=10)
#trainData<-read.csv("E:/data_contest/ChinaUnicomContest/复赛数据/123.csv")
#testData<-read.csv("E:/data_contest/ChinaUnicomContest/复赛数据/testpre.csv")

#trainData[1,]
#testData[1,]
#trainData<-trainData[,1:14]

colnames(testData)<-c("user_id","income","entermament","baby","gender","shopping","user_lon","user_lati","arrTime","duration")
colnames(trainData)<-c("user_id","income","entermament","baby","gender","shopping","shop_id","class","user_lon","user_lati","shop_lon",
                       "shop_lati","arrTime","duration");

model<-read.csv("D:/代码目录及运行说明/data2/model.csv")
d_array<-c(0,0.01,0.02,0.03,0.04,0.05,0.06,0.07,0.08,0.09,0.10,0.011,0.12,0.13,0.14,0.15,0.16,0.17,0.18,0.19);
colnames(model)<-c("income","entermament","baby","gender","shopping","class","num");

testOnline8<-matrix(ncol = 40)

nrow_test<-nrow(testData)


for(order1 in 1:nrow_test)
{
    #找出用户轨迹
   user<-testData[order1,];
   #如果为空值
   if(user$duration<16) next();
   
  # print(user$user_lon)
  
   traces<-user_trace_sim(user,trainData,d_array,model)
   
  # print(nrow(traces));
   #用户id
   user_id<-user[1];
   #用户标签信息
   user_labels<-user[2:6];
   #用户位置信息
   user_pos<-user[7:8];
   #用户日期信息
   user_date<-getDateInfo(user[9]);
  # print(user_date);
   user_dur<-user[10];
   
   nrow_trace<-nrow(traces);
   if(nrow_trace==0) next();
   
   shopid_set<-c();
   
   for(order2 in 1:nrow_trace)
   {
     trace<-traces[order2,]
     trace_shop_id<-trace[7];
     trace_shop_class<-trace[8]
     #如果shopid出现过
     if(trace_shop_id %in% shopid_set) next();
     shopid_set<-append(shopid_set,trace_shop_id$shop_id);
     
     trace_user_pos<-trace[9:10];
     trace_shop_pos<-trace[11:12];
     
     
     db<-c(calDistance(user_pos,trace_user_pos),calDistance(user_pos,trace_shop_pos));
   #  print(db)
     #userHis1......userHis5
     userHis_set<-trainData[trainData$user_id==user_id$user_id,]
     
     userHis1<-nrow(userHis_set[userHis_set[,7]==trace_shop_id$shop_id,])
     #print(userHis1)
     userHis2<-nrow(userHis_set[userHis_set$class==trace_shop_class$class,])
     userHis3<-nrow(userHis_set)
     #如果分母为0
     if(userHis3==0){
       userHis<-c(userHis1,userHis2,userHis3,0,0)
     }
     else{
        userHis<-c(userHis1,userHis2,userHis3,(userHis1/userHis3),(userHis2/userHis3))
     }
    
   #  print(userHis)
     #nei特征
     neiRec1<-nrow(traces[traces$shop_id==trace_shop_id$shop_id,]);
     neiNum<-nrow(traces)
     #print(neiRec1);
    # print(neiNum);
     
     #shopHis1,2
     shop_His_set<-trainData[trainData$shop_id==trace_shop_id$shop_id,]
     shopHis1<-nrow(shop_His_set[shop_His_set$income==user_labels[1]$income & 
                                 shop_His_set$entermament==user_labels[2]$entermament &
                                 shop_His_set$baby==user_labels[3]$baby &
                                 shop_His_set$gender==user_labels[4]$gender & 
                                 shop_His_set$shopping==user_labels[5]$shopping,
                                 ])
   if(nrow(shop_His_set)==0) shopHis2<-0
   else{
       shopHis2<-shopHis1/nrow(shop_His_set);
     }
     #print(shopHis1);
     #print(shopHis2);
     
     
     sim<-abs(user_labels-trace[2:6]);
     #print(sim)

    
     #根据模型规则，该用户对应标签所匹配的商户类别
     model_rules<-model[model[,1]==user_labels[1]$income & model[,2]==user_labels[2]$entermament& model[,3]==user_labels[3]$baby&
                          model[,4]==user_labels[4]$gender& model[,5]==user_labels[5]$shopping,]
     model_classes<-model_rules$class;
     
    
     pos_rules<-trainData[trainData$user_id==user_id$user_id &trainData$user_lon==user_pos[1]$user_lon &trainData$user_lati==user_pos[2]$user_lati,]
     pos_classes<-pos_rules$class;
     
     isNotAppear<-0
     if(length(pos_classes)==0) isNotAppear<-1;
     if(trace_shop_class %in% model_classes & !(trace_shop_class %in% pos_classes)) isNotAppear<-1;
    # print(isNotAppear)
     
     
      
      isSameUser<-0;
     if(user_id==trace[1]) isSameUser<-1;
    # print(isSameUser)
      
     
     #时间特征
     neiDate<-getDateInfo(trace[13]);
     #print(neiDate)
     dataDiff<-abs(user_date-neiDate);
     
     #持续时间特征
     dura<-c(abs(user_dur-trace[14]),user_dur,trace[14]);
     
    
     allCharact<-c(user[c(1,9)],db,user_labels,userHis,neiRec1,neiRec1/neiNum,shopHis1,shopHis2,
                   sim,neiNum,trace_shop_class,isNotAppear,isSameUser,user_date,neiDate,dataDiff,dura,trace_shop_id);
    # print(allCharact)
    
     
     #print(allCharact)
     testOnline8<-  insertRow(testOnline8,length(testOnline8[,1]),allCharact);
    # print(testOnline8)
    # m<-error;
     
     
   }
}
  
nlen<-nrow(testOnline8);
testOnline8<-testOnline8[1:nlen-1,];
colnames(testOnline8)<-c("userid","arrTime"	,"dbu","dbs","userLabel1","userLabel2","userLabel3","userLabel4","userLabel5"	,"userHis1"	,"userHis2","userHis3","userHis4","userHis5","neiRec1","neiRec2"
                           ,"shopHis1","shopHis2"	,"simbu1","simbu2","simbu3"	,"simbu4"	,"simbu5"	,"neiNum"	,"class","isNotAppear","isSameUser"	,	"userDate1"	,"userDate2","userDate3"	,"neiDate1","neiDate2"
                           ,"neiDate3","dateDiff1","dateDiff2" ,"dateDiff3","duraDiff"	,"user_dur"	,"trace_dur","shopid"	)	;	

write.csv(testOnline8,fileadd,row.names = F)

}




