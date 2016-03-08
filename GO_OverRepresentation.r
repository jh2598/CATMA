ggo.filter = function (input.ds) {
  filtered = c()
  for (i in 1:nrow(input.ds)) {
     if (input.ds[i,"Count"]>5 & 
           eval(parse(text=as.character(input.ds[i,"GeneRatio"]))) > 0.2 ) {
         filtered = c(filtered,i)
     }      
  }
  input.ds = input.ds[filtered,]
  return(input.ds)
}

## find in biological process -------------------
ggo.up.bp <- groupGO(gene = genes.up,
                  organism = "human",
                  ont = "BP",    # biological process
                  level = 3,
                  readable = TRUE)

sum.ggo.up.bp = summary(ggo.up.bp)
#nrow(sum.ggo.up.bp)
#head(sum.ggo.up.bp)

# we can filter the result if we wnat
sum.ggo.up.bp = ggo.filter(sum.ggo.up.bp)
#nrow(sum.ggo.up.bp)
#sum.ggo.up.bp[,c(1,2,4)]

## find in Cellular Component ----------------------
ggo.up.cc <- groupGO(gene = genes.up,
                  organism = "human",
                  ont = "CC",    # Cellular Component
                  level = 3,
                  readable = TRUE)

sum.ggo.up.cc = summary(ggo.up.cc)
#nrow(sum.ggo.up.cc)
#head(sum.ggo.up.cc)

# we can filter the result if we wnat
sum.ggo.up.cc = ggo.filter(sum.ggo.up.cc)
nrow(sum.ggo.up.cc)
sum.ggo.up.cc[,c(1,2,4)]

## find in Molecular Function ----------------------
ggo.up.mf <- groupGO(gene = genes.up,
                  organism = "human",
                  ont = "MF",    # Molecular Function
                  level = 3,
                  readable = TRUE)
                  
sum.ggo.up.mf = summary(ggo.up.mf)
#nrow(sum.ggo.up.mf)
#head(sum.ggo.up.mf)

# we can filter the result if we wnat
sum.ggo.up.mf = ggo.filter(sum.ggo.up.mf)
nrow(sum.ggo.up.mf)
sum.ggo.up.mf[,c(1,2,4)]

#BP
ego.up.bp <- enrichGO(gene = genes.up,
                organism = "human",
                ont = "BP",
                pAdjustMethod = "BH",
                pvalueCutoff = 0.01,
                qvalueCutoff = 0.05,
                readable = TRUE)

#summary(ego.up.bp)[,c(1,2,3,5)]
#nrow(summary(ego.up.bp))

#ToDo