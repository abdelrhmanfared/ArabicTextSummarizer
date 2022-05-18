import numpy as np
import pandas as pd
import pickle
import sys

print("1")
mlFile = pd.read_csv("TextVectors.csv")
print("2")
#mlFile = pd.read_csv("ML_Data_0.csv")
Feature = mlFile[['KPF', 'KPL' ,'PNV' ,'First Sentence in First Paragraph',
 'First sentence in last Paragraph',
 'First sentence in any of other paragraphs',
 'Sentence location in other paragraphs',
 'Sentence location in first and last paragraph',
 'cosine Similarity with title','common keyphrases with title',
 'sentence centrality' ,'sentence length is short/long',
 'sentence length equation', 'cue phrases' ,'strong words' ,'number scores',
 'sentence begins with weak word',
 'weak word score in other location in sentence']]
print("3")
#print((Feature))
#F = mlFile[mlFile.columns.values]
#print(F)
X = np.asarray(Feature)
print("4")

model = pickle.load(open('my_file.sav', 'rb'))
print("5")

label = model.predict(X)
print("6")

a = ""
print("7")
for lab in label:
 a+=str(lab)+','
print("8")

f = open("demofile3.txt", "x")
f.write("Woops! I have deleted the content!")
f.close()
print(a[0:len(a)-1])