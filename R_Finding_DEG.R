#library(affy)
#library(limma)
#setwd("C:/data")
getwd()
#fns <- list.celfiles(path="./celfiles",full.names=TRUE)
data <- ReadAffy(filenames=fns)
eset <- rma(data)
#pheno = read.csv("sample.csv", sep=",", header=TRUE)
data.filenames =  rownames(attributes(phenoData(data))$data)
pheno.filenames = pheno[,1]
neworder = c()
for (i in 1:length(pheno.filenames))
{
ss <- which(data.filenames==pheno.filenames[i])
if(length(ss)==1)
{neworder = c(neworder, ss)}
}
if (length(neworder) != length(pheno.filenames)) {
   print ("sample.csv is incorrect. Please check file names in sample.csv" )
   # stop process
} else {   
   pheno = pheno[neworder,]
   colnames(exprs(data)) =  pheno[,2]
}

# log2 transform if needs .. --------------------------------
ex <- exprs(eset)
qx <- as.numeric(quantile(ex, c(0., 0.25, 0.5, 0.75, 0.99, 1.0), na.rm=T))
LogC <- (qx[5] > 100) ||
          (qx[6]-qx[1] > 50 && qx[2] > 0) ||
          (qx[2] > 0 && qx[2] < 1 && qx[4] > 1 && qx[4] < 2)
if (LogC) { 
  ex[which(ex <= 0)] <- NaN
  exprs(eset) <- log2(ex)
}

#===========================================================
# DEG finding 
# limma(including fold-change, t-test)  
#===========================================================
# limma -------------
fl <- as.factor(pheno[,3])
eset$description <- fl
design <- model.matrix(~ description + 0, eset)
colnames(design) <- levels(fl)


# Fiting a linear model for each gene in the expression set eset given the design matrix
fit <-lmFit(eset,design)
# calculate the differential expression by empirical Bayes shrinkage
# of the standard errors towards a common value, by computing the moderated t-statistics,
# moderated F-statistic, and log-odds:
cont.matrix <- makeContrasts(TR-WT, levels=design)  ## change ##
fit2 <- contrasts.fit(fit, cont.matrix)
fit2 <- eBayes(fit2, 0.01)

#------------------------------------------------
tab <- topTable(fit2, adjust = "fdr", n = nrow(exprs(eset)))
condi.2 = tab[,4] < searchValue[1] & (abs(tab[,1]) > log(searchValue[2])) #searchValue[1] = pValue, searchValue[2] = foldChange
tab = tab[condi.2,]
# sort tab by logFC
up   = tab[tab[,"logFC"]>0,]
attach(up)
up   = up[order(-logFC),]
down = tab[tab[,"logFC"]<0,]
attach(down)
down = down[order(logFC),]
tab = rbind(up,down)

esetSel = exprs(eset)
esetSel = esetSel[c(rownames(tab)),]
colnames(esetSel) = pheno[,2]