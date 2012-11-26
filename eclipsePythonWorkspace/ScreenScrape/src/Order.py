
#0 - checkbox
#1 - tx #
#2 - item
#3 - quantity
#4 - postage
#5 - address
#6 - buyer
#7 - stats
#8 - late_in

import re
import os
import gnupg
from pprint import pprint

#PRIVATE_KEY_ASC = r'E:\SOG\Dist\Security\keys\myPrivateKeys\Freeside\freeside-secret-key-BB572E30.asc'
#PRIVATE_KEY = r'E:\SOG\Dist\Security\keys\myPrivateKeys\Freeside\key.secret.rsa.gpg'
#PRIVATE_KEY2 = r'C:\Program Files (x86)\GNU\GnuPG\key.secret.rsa.gpg'

KEY_PUB = r'E:\SOG\Dist\Security\keys\myPrivateKeys\Freeside\BB572E30.key'
KEY_PRIV = r'E:\SOG\Dist\Security\keys\myPrivateKeys\Freeside\BB572E30.priv.key'

GNUPG_HOME = r'C:\Program Files (x86)\GNU\GnuPG'
GNUPG_PASSPHRASE = r'ciprn666'


#from Crypto import Random
from Crypto.PublicKey import RSA

#import Crypto.Random.OSRNG


class Order ():
    
    def __init__(self, row):
         
        cells = row.findAll('td')
        self.cells_ = cells
        
        
        self.tx_ = self.parseLink(cells[1])
        self.item_ = self.parseLink(cells[2])
        
        self.quantity_ = self.parseCell(cells[3])
        self.postage_ = self.parseCell(cells[4])
        self.address_ = Address(cells[5])
        
        self.buyer_  = self.parseLink(cells[6])
        self.stats_ = self.parseLink(cells[7])
        self.stats_ = self.parseCell(cells[8])
        

            
    def parseLink(self, cell):
        return cell.find('a',text=True).contents[0]
        
    def parseCell(self, cell):
        return cell.contents[0]
    
    def parseMultilineCell(self, cell):
        return cell.contents[0]
    
         
    
    
class Address():
        def __init__(self, cell):
            

            regexStr = r'-----BEGIN PGP MESSAGE-----(.*)-----END PGP MESSAGE-----'
            self.compiledSearch_ = re.compile(regexStr,  re.DOTALL )
        
            self.cell_ = cell
            self.clearText_ = ''
            self.cypherText_ = ''
            str =''
            
            brLines = self.cell_.findAll('br')
            
            for oneBrLine in brLines:
                oneBrLine.extract()
                
                
            for oneLine in self.cell_:
                sreMatch = re.search(r'Version:', oneLine)
                if (sreMatch == None):
                    oneBrLine.extract()
                    str += oneLine
            
            

            match  = self.compiledSearch_.search(str)
            
            
            if (match  != None):
                groups = match.groups()
                self.cypherText_ = '-----BEGIN PGP MESSAGE-----\n' + groups[0] + '-----END PGP MESSAGE-----\n'
                self.clearText_ = self.decrypt(self.cypherText_)
            else:
                 self.clearText_ = str
                 
                 
        
            self.extractName()
            
        def extractName(self):
            
            rows = self.clearText_.upper().split('\n')
            l = len(rows)
            
            self.name = rows[0]
            self.street = rows[1]
            if (l>3):
                self.street += '\n' + rows[2]
            
            self.cityStateZip = rows[l-1]
            
            strStateMatch = R'\s([A-Z]{2})\s'
            sreMatch = re.search(strStateMatch, self.cityStateZip)


            
        def decrypt(self, cypherText):
            
            
            gpg = gnupg.GPG(gnupghome=GNUPG_HOME)
            #gpg.encoding = 'utf-8'
            

            key_data = open(KEY_PUB).read()
            import_result = gpg.import_keys(key_data)
            if (import_result.count < 1):
                raise Exception("Order.Address.decrypt() no key imported: " + import_result.stderr)
                
                
            key_data = open(KEY_PRIV).read()
            import_result = gpg.import_keys(key_data)
            if (import_result.count < 1):
                raise Exception("Order.Address.decrypt() no key imported: " + import_result.stderr)
            
               
            pprint(import_result.results)
            

            #status = gpg.decrypt_file(cypherText, passphrase='ciprn666', output='my-decrypted.txt')
            
            result = gpg.decrypt(cypherText, passphrase=GNUPG_PASSPHRASE)
            pprint(result)
            
            if ('decryption ok' == result.status):
                return result.data
            else:
                pprint('status: ' + result.status)
                pprint('err: ' + result.stderr)
            
            
            

            

