# -*- coding: utf-8 -*-
"""
Created on Fri Nov  4 21:58:27 2016

@author: administrator
"""
import ast
import numpy as np
import math
#from math import sum, exp, factorial

from collections import Counter
from collections import OrderedDict
#from builtins import int

aspects=dict()
fields = []
alla = []
da = dict() #dict with rank as key and value as dict of freq of doc aspects in docs 
ta = dict() #dict with rank as key and value as dict of freq of topic aspects in doc
all_da = dict()#dict storing all aspects as keys and doc freq as value
new_a_score = dict()#dict to store score for all docs
observed_aspects = dict()
obs_a = []
counter = 0
topic = 0
all_fields = dict()

f = open("E:/Nirali/Nirali/2007/Metamap_res8", "w+")

with open('E:/python/NMetamap_1.txt', 'r+') as pf:
    for line in pf:
        
        counter = counter+1
        id = int(counter/2)
        #print(line)
        if(counter<=2000):
            if(counter%2 == 0):
                
                str = line[1:len(line)-2]
                aspects=ast.literal_eval(line)
                #print(aspects)
                alla.extend(aspects.keys())
                #print(aspects(dict.keys()[0]))
                da[id]=aspects
            #print(aspects.get('Lupus nephritis', 0))            
                #print(next (iter (aspects.keys())))
            
            else:
            #str=line[2:len(line)-2]
            #print(str)
                fields = ast.literal_eval(line)
                #print(fields)
                topic = int(fields[0])
                pmid = fields[1]
                rank = int(fields[2])
                rel = float(fields[3])
                offset =    int(fields[4])
                length =    int(fields[5])
                all_fields[rank]=fields
                print(topic , " " ,rank, " " , counter)
        else:
            ######################################################## 
        #print("Topic ",topic ," over")
            allafreq = Counter(alla)
            all_da = dict(allafreq)
            #print(allafreq.most_common(5))
            #f.write("%s\n", % all_fields[1])
            f.write("\n")
            r = 1
            new_fields = all_fields.get(1)
            new_fields[2] = r
            new_fields[6] = "Metamap2"
            print(new_fields[2])
            for item in new_fields:
                f.write("%s " % item)
            observed_aspects = dict(Counter(da[1]))
            print("written in file: ", all_fields[1])
            #print(da[1])
            del da[1]
            del all_fields[1]
            print("Protein: ",observed_aspects.get('Protein'))
            
            while(da):
                for doc in da.keys():
                #print(doc)
                    s=0.000000000000
                    for asp in (da.get(doc)).keys():
                        l = all_da.get(asp, 0)
                        x = observed_aspects.get(asp, 0)
                        aspc = asp.split("..")
                        w = float(aspc[1])
                        if(w > float(3)):
                            w = float(1.3)
                        else:
                            w = float(1)
                        #print(asp , " ", w)
                    #p_a = 
                        neg_l=0-l
                    #print(topic, doc, float(all_fields.get(doc)[3]))
                        pa = da.get(doc).get(asp)  
                        m = np.exp(neg_l)*np.sum((l**i)/math.factorial(i) for i in range(0,x))
                        #print (doc, "... " ,(all_fields.get(doc)))
                        ff = float((all_fields.get(doc))[3])
                        #print ff
                        #p.exp(np.array([1391.12694245],dtype=np.float128)) 
                        s = s + (1.0 -m)*ff*w/pa
                    #print(topic, doc, new_a_score[doc])
            #print(topic,doc,s)
                    new_a_score[doc] = s
                
                    #print(topic, doc, new_a_score[doc])
                v=list(new_a_score.values())
        #print("v: ", v)
                k=list(new_a_score.keys())
                #print("k: ", k)
                d = k[v.index(max(v))]
        #print(topic," ", d)
                #d = max(new_a_score, key=new_a_score.get)
                #f.write("%s\n" % all_fields[d])
                f.write("\n")
                r = r + 1
                new_fields = all_fields.get(d)
                new_fields[2] = r
                new_fields[3] = new_a_score.get(d)
                new_fields[6] = "Metamap2"
                print(new_fields)
                for item in new_fields:
                    f.write("%s " % item)
                observed_aspects = dict(Counter(da[d]))
                del da[d]
                del all_fields[d]
                del new_a_score[d]
        #============================================================
           
            
   #=============================================================================================         
                  
            counter=1
            topic = topic + 1
            fields = ast.literal_eval(line)
            topic = int(fields[0])
            pmid = fields[1]
            rank = int(fields[2])
            rel = float(fields[3])
            offset =    int(fields[4])
            length =    int(fields[5])
            all_fields[rank]=fields
            alla = [] #REMEMBER TO EMPTY ALL STORED DICTS AND LISTS ETC......................
            print(topic ," " ,rank)
            
    #print(topic , " " ,rank)
    print("Topic ",topic ," over")
    allafreq = Counter(alla)
    all_da = dict(allafreq)
            #print(allafreq.most_common(5))
            #f.write("%s\n", % all_fields[1])
    f.write("\n")
    #r = 1
    new_fields = all_fields.get(1)
    r = 1
    new_fields[2] = r
    new_fields[6] = "Metamap2"
    print(new_fields[2])
    for item in new_fields:
        f.write("%s " % item)
    observed_aspects = dict(Counter(da[1]))
    print("written in file: ", all_fields[1])
            #print(da[1])
    del da[1]
    del all_fields[1]
    #print("Protein: ",observed_aspects.get('Protein'))
            
    while(da):
        for doc in da.keys():
                #print(doc)
            s=0.000000000000
            for asp in (da.get(doc)).keys():
                l = all_da.get(asp, 0)
                x = observed_aspects.get(asp, 0)
                aspc = asp.split("..")
                w = float(aspc[1])
                if(w > float(3)):
                    w = float(1.3)
                else:
                    w = float(1)
                    #p_a = 
                neg_l=0-l
                    #print(topic, doc, float(all_fields.get(doc)[3]))
                pa = da.get(doc).get(asp)   
                #s = s + (1.0 - math.exp(neg_l)*np.sum(math.pow(l, i)/math.factorial(i) for i in range(0,x)))*float((all_fields.get(doc))[3])/pa
                m = np.exp(neg_l)*np.sum((l**i)/math.factorial(i) for i in range(0,x))
                ff = float((all_fields.get(doc))[3])
                        #p.exp(np.array([1391.12694245],dtype=np.float128)) 
                s = s + (1.0 -m)*w*ff/pa
                    #print(topic, doc, new_a_score[doc])
            #print(topic,doc,s)
            new_a_score[doc] = s
                
                    #print(topic, doc, new_a_score[doc])
        v=list(new_a_score.values())
        #print("v: ", v)
        k=list(new_a_score.keys())
        #print("k: ", k)
        d = k[v.index(max(v))]
        #print(topic," ", d)
                #d = max(new_a_score, key=new_a_score.get)
                #f.write("%s\n" % all_fields[d])
        f.write("\n")
        r = r + 1
        new_fields = all_fields.get(d)
        new_fields[2] = r
        new_fields[3] = new_a_score.get(d)
        new_fields[6] = "Metamap2"
        print(new_fields)
        for item in new_fields:
            f.write("%s " % item)
        observed_aspects = dict(Counter(da[d]))
        del da[d]
        del all_fields[d]
        del new_a_score[d]
f.close()           
#print(da)
