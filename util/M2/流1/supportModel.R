supportModel<-function(user_labels,classification,modeles)
{
  rules<-modeles[modeles[,1]==user_labels[1]$income & modeles[,2]==user_labels[2]$entermament& modeles[,3]==user_labels[3]$baby& modeles[,4]==user_labels[4]$gender& modeles[,5]==user_labels[5]$shopping,]
  classes<-rules$class;
  return(classification %in% classes)
}