'''
Created on Dec 22, 2011

@author: raj
'''

import boto
import time
import os
import paramiko
import paramiko.sftp
import os.path

UBUNTU_AMI = 'ami-c9a1fe8c'
IP_WINTERMUTE = '50.18.174.1'
IP_STRAYLIGHT = '50.18.252.97'

ISTANCE_TYPE_MICRO='t1.micro'
ISTANCE_TYPE_SMALL='m1.small'



def ssh(name, username) :
    instance = Server()
    instance.load(name, username)
    instance.openShell()
    
    

      
def uploadToStraylight() :
    instance = Server()
    instance.load("Straylight")
    #instance.uploadFile(r'resources\build.sh')
    instance.uploadScript(r'resources\setup.sh')
    
      

def launchFromScratch(hostname='Straylight') :
    print '*****launchFromScratch: luanching ' + hostname + ' From Scratch******'
    instance = Server()
    instance.create(UBUNTU_AMI, hostname)
    instance.instance_type = ISTANCE_TYPE_SMALL
    instance.launchOne()
    associateIP(instance)
    instance.uploadScript(r'resources\setup.sh')
    instance.uploadScript(r'resources\ctrl.sh')
    associateIP(instance)
    instance.openShell()
    

def launchFromImage(hostname='Straylight', imagename='StrayLightImageUnuntu4'):
    print '*****launchFromImage: luanching ' + hostname + ' From Image******'
    print 'using image: ' + imagename

    instance = Server()
    instance.createByName(imagename, hostname)
    instance.instance_type = 'm1.small'
    instance.launchOne()
    associateIP(instance)
    instance.openShell()
    

def associateIP(instance) :
    if (instance.name == 'Wintermute'):
       instance.associateIP(IP_WINTERMUTE)                  
    else :
        if (instance.name == 'Straylight') :
            instance.associateIP(IP_STRAYLIGHT)
    
    
    
    
    
class Server :
    def __init__(self):
        self.address = None
        self.key_file_paramiko = os.path.normpath('C:\putty\esimWestCoast.pem')
        self.key_file_putty = os.path.normpath('C:\putty\esimWestCoast.ppk')
        self.path_to_putty = os.path.normpath('C:\putty\putty.exe')
        self.username = 'ubuntu'
        self.connect()
        
        
        
    def createByName (self, amiName, newInstanceName): 
        f = {'tag:Name': amiName} 
        images = self.connection.get_all_images(filters=f)
        
        if (len(images) != 1) :
            raise Exception("Error self.connection.get_all_images should return 1 image")
        
        
        self.create(images[0].id, newInstanceName)
        
        #self.reservation = theImage.run(1,1, self.key_name, self.security_groups, None, None, self.instance_type)
        
        
        
    def create (self, amiNumber, newInstanceName): 
        self.ami = amiNumber
        self.name = newInstanceName
        self.key_name = 'esimWestCoast'
        self.security_groups = ['esim']
        self.instance_type = 't1.micro'

        
    
    def load (self, name, username):
        if username != None:
            self.username = username
            
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
    
    
    def runRemoteScript(self, filePath):
        print '*****runRemoteScript******'
        
        result = self.connectViaSSH()
        
        if not result:
            return False
        
        
        self.instance.update()
        host = self.getHostName()
        
        fileName = os.path.basename(filePath)
        remotePath = '/home/%s/%s' % (self.username, fileName)
        stdin, stdout, stderr = self.ssh.exec_command('./' + fileName)
        
        
        self.ssh.close()
        
        
    def uploadScript(self, filePath):
        print '*****uploadScript******'
        
        self.uploadFile(filePath)
        
        result = self.connectViaSSH()
        if not result:
            return False
        
        self.instance.update()
        host = self.getHostName()
        
        fileName = os.path.basename(filePath)
        remotePath = '/home/%s/%s' % (self.username, fileName)
        stdin, stdout, stderr = self.ssh.exec_command('chmod 777 %s' % remotePath)
        print 'chmod complete'
        
        self.ssh.close()
        
           
    def uploadFile(self, filePath):
        print '*****uploadFile******'
        self.blockThreadUntilRunning()
        self.blockThreadUntilSSH()
        
        localPath = os.path.normpath(filePath)
        fileName = os.path.basename(filePath)
        remotePath = '/home/%s/%s' % (self.username, fileName)
        
        self.instance.update()
        host = self.getHostName()
        port = 22
        mykey = paramiko.RSAKey.from_private_key_file(self.key_file_paramiko)
        
        transport = paramiko.Transport((host, port))
        transport.connect(username = self.username, pkey = mykey)
        
        sftp = paramiko.SFTPClient.from_transport(transport)

        result = sftp.put(localPath, remotePath)
        
        sftp.close()
        transport.close()
        print 'upload complete'
        
    def connectViaSSH(self):
        print '***********getSSH***********'
        ssh = paramiko.SSHClient()
        ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
        
        self.instance.update()
        host = self.getHostName()
        
        try: 
            ssh.connect(
                        host, 
                        username=self.username, 
                        key_filename=self.key_file_paramiko
                        )
        except:
            print 'SSH not available'
            ssh.close()
            return False
        
        stdin, stdout, stderr = ssh.exec_command('echo "test ssh connection"')

        lines = stdout.readlines()
        oneLine = lines[0]
        print oneLine
        
        if oneLine == "test ssh connection\n":
            self.ssh = ssh
            return True
        else:
            ssh.close()
            return False
        
        
    def blockThreadUntilSSH(self):
        print '***** blockThreadUntilSSH ******'
        result = False
        while not result :
            result = self.connectViaSSH()
            
        self.ssh.close()
        
        
        
        
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
                        raise Exception("filter request failed")
                
        
        
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
        

        
        
        
        
        
            

