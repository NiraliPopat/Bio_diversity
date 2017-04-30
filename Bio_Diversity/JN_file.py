# -*- coding: utf-8 -*-
"""
Created on Sat Nov  5 19:19:49 2016

@author: administrator
"""

# -*- coding: utf-8 -*-
"""
Created on Wed Nov  2 00:59:57 2016

@author: administrator
"""
import string
import html2text
import nltk
from nltk.tokenize import word_tokenize
from bs4 import BeautifulSoup 
from nltk.tokenize import PunktSentenceTokenizer
from collections import Counter
from collections import OrderedDict
from math import log
import mwapi
import json

sentence_re = r'(?:(?:[A-Z])(?:.[A-Z])+.?)|(?:\w+(?:-\w+)*)|(?:\$?\d+(?:.\d+)?%?)|(?:...|)(?:[][.,;"\'?():-_`])'
session = mwapi.Session('https://en.wikipedia.org')


#Chunk Grammar
bracket = "("
print (bracket)
chunks = ""
grammar = r"""
    NBAR:
        {<JJ>*<NN|NNP|NNS>+}  # Nouns and Adjectives, terminated with Nouns
       
    NP:
        {<NBAR>}
        {<NBAR><IN><NBAR>}  # Above, connected with in/of/etc...
        #}<NNP>*<NN><NN>+<NN.*>*{
"""
#grammar = r"""
 #   NBAR:
  #      {<NN.*|JJ>*<NN.*>}  # Nouns and Adjectives, terminated with Nouns
        
   # NP:
    #    {<NBAR>}
     #   {<NBAR><IN><NBAR>}  # Above, connected with in/of/etc...
#"""
chunker = nltk.RegexpParser(grammar)

def acceptable_word(word):
    """Checks conditions for acceptable word: length, stopword."""
    accepted = bool(2 <= len(word) <= 40)
    return accepted
    
def leaves(tree):
    """Finds NP (nounphrase) leaf nodes of a chunk tree."""
    for subtree in tree.subtrees(filter = lambda t: t.label()=='NP'):
        yield subtree.leaves()
        
def get_terms(tree):
    for leaf in leaves(tree):
        term = [w for w,t in leaf if acceptable_word(w) ]
        yield term


qid = 200
t_asp= []
d = dict()
da = dict()
a = dict()
adict = dict()
all_aspects = []
f = open("JN_chunk3", "a+")
path = "/home/administrator/Documents/Nirali/2006/documents/text/"
with open('/home/administrator/Documents/Nirali/2007/submissions/NLMinter2', 'r') as pf:
    for line in pf: 
        
        line = line.strip()
      
        fields = line.split()
        
        
        if not fields:
            #print("Empty")
            break
        
        topic = int(fields[0])
        pmid = fields[1]
        rank = int(fields[2])
        rel = float(fields[3])
        offset =	int(fields[4])
        length =	int(fields[5])
        
        
        with open(path+fields[1]+".html", 'r',encoding='utf-8', errors='replace') as foo:
            #foo = BeautifulSoup(html)
            chunks=""
            
            position = foo.seek(offset, 0);
            str = foo.read(length);
            #soup = BeautifulSoup(str)
            #text = soup.get_text()
            text = html2text.html2text(str)
            #print(text)
            text = text.replace("*", "")
            text = text.replace("_", "")
            text = text.replace("|", "")
            text = text.replace("--", "")
            #print(str)
            #print("tect\n: " +soup.get_text())
            
            #toks = nltk.regexp_tokenize(text, sentence_re)
            toks = nltk.word_tokenize(text)
            postoks = nltk.tag.pos_tag(toks)


            #print (postoks)

            tree = chunker.parse(postoks)

            
            terms = get_terms(tree)

            aspects = []
            chunks=""
            counter = 0
            for term in terms:
                if (counter<45):  
                    chunk=""
                    for word in term:
                        chunk = chunk+ word+" "
                    chunks = chunks + chunk + "|"
                    counter=counter+1
                    continue
                #print(counter)                    
                output= session.get(action='query', titles=chunks, redirects=1)
                chunks=""
                #print(counter)
                counter=0
                    #print(output)
                for each in output['query']['pages']:
                    if(int(each) > -1):
                        aspect=(output['query']['pages'][each]['title'])
                        #print(aspect)
                        if (bracket not in aspect and len(aspect)>1):
                            aspects=aspects+[aspect]
                #print(aspects)
                        
            if(counter):
                output= session.get(action='query', titles=chunks, redirects=1)
                chunks=""
                #print(counter)
                counter=0
                    #print(output)
                for each in output['query']['pages']:
                    if(int(each) > -1):
                        aspect=(output['query']['pages'][each]['title'])
                    #print(aspect)
                        if(bracket not in aspect and len(aspect)>1):
                            aspects=aspects+[aspect]
              
            afreq = Counter(aspects)
            #all_aspects.extend(set(aspects))
            adict = dict(afreq)
            print(fields)
            f.write("%s\n" % fields)
            f.write("%s\n" % adict)
            #da[rank] = afreq # aspects for docs
            #d[rank] = fields # doc details
            #print(afreq)
            
f.close()       
            
            

    