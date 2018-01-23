getModel<-function(a,depth,mcw,nrou,colsa,subsa)
{
  #a:为训练集数据,其中a的最后一项为label,其他均为特征项
  set.seed(100);
  len<-length(a[1,]);
  train<-a[,-len];
  
  p<-nrow(a[a[,len]==1,]);
  n<-nrow(a[a[,len]==0,]);
  weight<-p/n;
  #print(paste("过采样前：","p  :",p,"   n：",n,"   weight:",weight,"------------------------"));
  #对数据过采样
 # positiveIn<-a[a[,len]==1,]
 # split<-createDataPartition(a[,1],times=1,p=5/7,list = FALSE);
 # positiveIn<-postiveData[split,]
  
 # b<-rbind(a,positiveIn);
  #p<-nrow(b[b[,len]==1,]);
 # n<-nrow(b[b[,len]==0,]);
 # weight<-p/n;
 # print(paste("过采样后：","p  :",p,"   n：",n,"   weight:",weight,"------------------------"));
  
  # dtrain<-xgb.DMatrix(data = train,label=a[,len]);
 # train<-b[,-len];
  xg<-xgboost(
    data = train,
   # label = b[,len],
   label=a[,len],
   # data = dtrain,
    #
    eta = 0.3,
    gamma = 0.1,
    max_depth = depth, 
  
    min_child_weight=mcw, 
    #避免样本倾斜
    max_delta_step =1,
    subsample = subsa,
    scale_pos_weight  = weight,
    colsample_bytree=colsa,
    
    #lambda=0.2,
   # alpha=0.2,
   # missing = NaN,
    
    nround=nrou, 
    
    
    # seed = 100,
    eval_metric = "error",
    objective = "binary:logistic",
    nthread = 10
  )
  return(xg)
}