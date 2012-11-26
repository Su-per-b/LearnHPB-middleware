#!/usr/bin/env python



from bs4 import BeautifulSoup

from Order import Order
from Order import Address


HTML_SOURCE_FILE = r'E:\SOG\temp\orders.html'

def main(argv=None):


    fileHandle = open(HTML_SOURCE_FILE)
    soup = BeautifulSoup(fileHandle)
    
    
    allTables = soup.findAll('table',{'class':"zebra"})
    unshippedTable =  allTables[0]
    intransetTable =  allTables[1]
    
    unshippedRows = unshippedTable.findAll('tr')
    popped = unshippedRows.pop(0)
    
    for row in unshippedRows:
        #ce = row.findAll('td')
        o = Order(row)
        
    x = 0

if __name__ == "__main__":
  main()


