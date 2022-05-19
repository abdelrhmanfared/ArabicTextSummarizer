from imblearn.over_sampling import SMOTE
import numpy as np
import pandas as pd
import pickle
from sklearn import svm
from sklearn.metrics import confusion_matrix, classification_report, accuracy_score
from sklearn.model_selection import train_test_split

mlFile = pd.read_csv('ML_Data_0.csv')
#print(mlFile.head(10)) # usefull for testing
#mlFile.count()
#print(mlFile.columns.values)
#print(mlFile.columns.values) # data frame 2d data srtucture labael acces
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
#print((Feature))
#F = mlFile[mlFile.columns.values]
#print(F)
X = np.asarray(Feature)

Y = np.asarray(mlFile['Label'])
oversample = SMOTE()
X, Y = oversample.fit_resample(X, Y)

x_train, x_test,y_train, y_test = train_test_split(X,Y,test_size=0.2)

classifierSVM = svm.SVC(kernel='rbf',class_weight='balanced',gamma=10,C=100)
classifierSVM.fit(x_train,y_train)
y_pred_logreg = classifierSVM.predict(x_test) #Making predictions to test the model on test data

print(' Train accuracy %s' % classifierSVM.score(x_train, y_train)) #Train accuracy
#Logistic Regression Train accuracy 0.8333333333333334
print(' Test accuracy %s' % accuracy_score(y_pred_logreg, y_test)) #Test accuracy

print(confusion_matrix(y_test, y_pred_logreg)) #Confusion matrix
print(classification_report(y_test, y_pred_logreg)) #Classification Report

file_name='my_file.sav'
f = open(file_name,'wb')
pickle.dump(classifierSVM,f)
f.close()