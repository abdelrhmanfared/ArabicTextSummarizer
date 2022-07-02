import gc
import os
import jpysocket as jpysocket
import numpy as np
import pandas as pd
import pickle
import socket  # Import socket module

# filename = os.path.dirname(os.path.abspath(__file__)) + "\\svm_model.sav"

filename = "svm_model.sav"
svm_model = pickle.load(open(filename, 'rb'))

filename = "nn_model.sav"
nn_model = pickle.load(open(filename, 'rb'))

soc = socket.socket()  # Create a socket object
host = "localhost"  # Get local machine name
port = 1000  # Reserve a port for your service.
soc.bind((host, port))  # Bind to the port
soc.listen(5)  # Now wait for client connection.
while True:
    print('Server is listening.......')
    conn, addr = soc.accept()  # Establish connection with client.
    print("Got connection from", addr)

    msg = conn.recv(1024).decode("utf-8")
    spl = msg.split("\r\n")

    csv_path = spl[0]  # from socket
    teq = spl[1]  # from socket

    mlFile = pd.read_csv(csv_path).fillna(0)
    Feature = mlFile[['KPF', 'KPL', 'PNV', 'First Sentence in First Paragraph',
                      'First sentence in last Paragraph',
                      'First sentence in any of other paragraphs',
                      'Sentence location in other paragraphs',
                      'Sentence location in first and last paragraph',
                      'cosine Similarity with title', 'common keyphrases with title',
                      'sentence centrality', 'sentence length is short/long',
                      'sentence length equation', 'cue phrases', 'strong words', 'number scores',
                      'sentence begins with weak word',
                      'weak word score in other location in sentence']]

    X = np.asarray(Feature)
    # msg = conn.recv(1024)
    # print (msg)
    # if (msg == "Hello Server"):
    #     print("Hii everyone")
    # else:
    #     print("Go away")

    label = svm_model.predict(X)
    if teq == "false":
        label = nn_model.predict(X)

    a = ""
    for lab in label:
        a += str(lab) + ','

    conn.send(jpysocket.jpyencode(a[0:len(a) - 1]))
    print(a[0:len(a) - 1])

# end_time = time.time()
# print(end_time - start_time)
