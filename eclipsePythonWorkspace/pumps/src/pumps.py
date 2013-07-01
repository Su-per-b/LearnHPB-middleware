'''
Created on Mar 13, 2013

@author: raj
'''

import pycurl
 
def main(argv=None):
    c = pycurl.Curl()
    c.setopt(c.URL, 'http://news.ycombinator.com')
    c.perform()
    
if __name__ == "__main__":
  main()
    
    
 
 



