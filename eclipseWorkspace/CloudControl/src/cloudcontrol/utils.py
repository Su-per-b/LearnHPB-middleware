'''
Created on Dec 22, 2011

@author: raj
'''

import boto
import time
import os
import paramiko


def launchWintermute() :
    print '*****launch Wintermute******'
    instance = Instance()
    instance.create("9b0a54de", "Wintermute")
    instance.launchOne()
    instance.associateIP('50.18.174.1')
    instance.openShell()

    
def launchStraylight() :
    print '*****launch Straylight******'
    instance = Instance()
    instance.create("9b0a54de", "Straylight")
    instance.launchOne()
    instance.associateIP('50.18.252.97')
    instance.openShell()
    
def launchBuilder() :
    print '*****launch Builder******'
    instance = Instance()
    instance.create("9b0a54de", "Builder")
    instance.launchOne()
    instance.openShell()

def ssh(name) :
    instance = Instance()
    instance.load(name)
    instance.openShell()
    
    
class Instance :
    def __init__(self):
        self.connect()
        self.address = None
        self.key_file_paramiko = os.path.normpath('C:\putty\esimWestCoast.pem')
        self.key_file_putty = os.path.normpath('C:\putty\esimWestCoast.ppk')
        self.path_to_putty = os.path.normpath('C:\putty\putty.exe')
        self.username = 'ec2-user'
        
        
    def create (self, amiNumber, name): 
        self.ami = 'ami-' + amiNumber
        self.name = name
        self.key_name = 'esimWestCoast'
        self.security_groups = ['esim']
        self.instance_type = 't1.micro'

        
    
    def load (self, name):
        self.connect()

        self.name = name
        
        f = {'tag:Name': name, 'instance-state-code':'16'} 
        
        allReservations = self.connection.get_all_instances(filters=f)
        #allReservations = self.connection.get_all_instances()
        
        if len (allReservations) != 1 or len (allReservations[0].instances) != 1 :
            print "Could not load %s" % name
            return False
        else:
            print "%s loaded " % name
            self.instance = allReservations[0].instances[0]
            
            f2 = {'instance_id': self.instance.id} 
            addresses = self.connection.get_all_addresses(filters=f2);
    
            if (len(addresses) == 1) :
                self.address = addresses[0]
            
            
        return True

        
        
    def getHostName(self):
        if self.address != None :
            return self.address.public_ip
        else :
            return self.instance.public_dns_name
            
    def checkForSSH(self):
        print 'checking for SSH daemon on remote host'
        
        ssh = paramiko.SSHClient()
        ssh.set_missing_host_key_policy(
        paramiko.AutoAddPolicy())
        
        self.instance.update()
        host = self.getHostName()

        try: 
            ssh.connect(
                        host, 
                        username='ec2-user', 
                        key_filename=self.key_file_paramiko
                        )
        except:
            print 'SSH not available'
            ssh.close()
            return False
            
        stdin, stdout, stderr = ssh.exec_command('echo "test ssh connection"')
        #print stdout.readlines()
        lines = stdout.readlines()
        oneLine = lines[0]
        print lines
        
        ssh.close()
        
        if oneLine == "test ssh connection\n":
            return True
        else:
            return False
        
        
    def blockThreadUntilSSH(self):
        print '***** blockThreadUntilSSH ******'
        result = False
        while not result :
            result = self.checkForSSH()
        
        
    def blockThreadUntilRunning(self):
        print '***** blockThreadUntilRunning ******'
        count = 1
        
        while self.instance.state != 'running': 
            self.instance.update(validate=True)
            self.printRunState(count)
            time.sleep(1)
            count += 1
            
        self.printRunState(count)
        
            
    def printRunState(self, count):
        print "%s - name:%s - instance.id:%s - state:%s" % (count, self.name, self.instance.id, self.instance.state)
    
    def associateIP(self, ip):
        print '***** Associate IP******'
        self.blockThreadUntilRunning()
        
        addresses = self.connection.get_all_addresses(addresses=[ip]);

        if (len(addresses) != 1) :
            raise Exception("Error associateIP should return 1 address")
        
        print 'associating IP: %s' % ip
        self.address = addresses[0] 
        self.address.associate(self.instance.id)
        

    
    #    print 'allocation_id: ' + addresses[0].allocation_id
    #    print 'association_id: ' + addresses[0].association_id
    def connect(self):
        self.connection = boto.connect_ec2()
        print self.connection
        print self.connection.region
  
    def terminateOthersWithSameName(self):
        print '***** terminateOthersWithSameName ******'
        
        f = {'tag:Name': self.name, 'instance-state-code':'16'} 
        allReservations = self.connection.get_all_instances(filters=f)
        
        if len (allReservations) == 0  :
            print "No other running instances named %s" % self.name
        else:
            for res in allReservations:
                for inst in res.instances:
                    inst.update()
                    if inst.state == 'running':
                        print inst.id + ' in state: ' + inst.state + ' - now terminating'
                        inst.terminate()
                    else:
                        print inst.id + ' in state: ' + inst.state
                        raise ("filter request failed")
                
        
        
    def launchOne(self):
        self.terminateOthersWithSameName()
        self.launch()
        
    def printInfo(self):
        print '***** printInfo ******'
        groups = self.connection.get_all_security_groups()
        print 'groups: ' + groups
        
    def launch(self):
        
        images = self.connection.get_all_images(image_ids=[self.ami])
        print images
        
        
        if (len(images) != 1) :
            raise Exception("Error self.connection.get_all_images should return 1 image")
        
        theImage = images[0]
        self.reservation = theImage.run(1,1, self.key_name, self.security_groups, None, None, self.instance_type)
        
        self.instance = self.reservation.instances[0]
        print 'self.instance.state %s' %  self.instance.state
        time.sleep(1)
        
        self.instance.add_tag('Name', self.name)
        
    def getPublicDNS(self):
        print  "public_dns_name: %s" % self.instance.public_dns_name
        self.blockThreadUntilRunning()
        print  "public_dns_name: %s" % self.instance.public_dns_name
        return self.instance.public_dns_name
    
    
    def openShell(self):
        print '***** openShell ******'
        self.blockThreadUntilRunning()
        self.blockThreadUntilSSH()

        
        host = self.getHostName()
        
        usernameHost = "%s@%s" % (self.username, host) 
        key = '-i ' + self.key_file_putty
        
        os.system(self.path_to_putty + ' -ssh %s %s' % (usernameHost, key))
        

        
        
        
        
        
            

