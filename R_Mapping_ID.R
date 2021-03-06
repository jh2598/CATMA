#library("clusterProfiler")
#library("hgu133a.db")                 # microarray chip �� ���� annotation db

#=========================================================================
# ID conversion (probe id -> symbol, entrez)
#=========================================================================
ds = tab

PROBES = as.character(row.names(tab))
ENTREZ = select(hgu133a.db, PROBES, c("ENTREZID"))  
SYMBOL = select(hgu133a.db, PROBES, c("SYMBOL"))


# single probe can be mapped to multiple entrez id  -> remove
ENTREZID = c()
SYMBOLID = c()
for (i in 1:nrow(ds)) {
   loc <- which(ENTREZ[,1]== PROBES[i])
   if (length(loc)>1) {
      ENTREZID[i] <- "NONE"
   } else {
      ENTREZID[i] <- ENTREZ[loc,2] 
   }
   loc2 <- which(SYMBOL[,1]== PROBES[i])
      SYMBOLID[i] <- paste(SYMBOL[loc2,2],collapse="//")
}  

ds = cbind(ds, ENTREZID, SYMBOLID)
ds= ds[with(ds, order(-logFC)),]   # sort genes by logFC

genes.up   = as.character(ds[ds[,"logFC"]>0 & 
              ds[,"ENTREZID"]!= "none" & !is.na(ds[,"ENTREZID"]),"ENTREZID"])
genes.down = as.character(ds[ds[,"logFC"]<0 & 
              ds[,"ENTREZID"]!= "none" & !is.na(ds[,"ENTREZID"]),"ENTREZID"])
genes.total = union(genes.up, genes.down)
