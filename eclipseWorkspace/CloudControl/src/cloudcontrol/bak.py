'''
Created on Dec 28, 2011

@author: raj
'''
import boto
import time
import datetime
import subprocess
import os
import paramiko

def sshTest() :
    ssh = paramiko.SSHClient()
    ssh.set_missing_host_key_policy(
    paramiko.AutoAddPolicy())
    
    norm = os.path.normpath(r'C:\putty\esimWestCoast.pem')
    
    try: 
        ssh.connect(
                    'wintermute.rajlab.com', 
                    username='ec2-user', 
                    key_filename=norm
                    )
    except:
        print 'except'
        ssh.close()
        return False
        
    stdin, stdout, stderr = ssh.exec_command('echo "test"')
    print stdout.readlines()
    ssh.close()
    return True


def sshBak() :

    output = None
    
    connected = False 
    #while connected == False :
        #print  '%s output == None' % count
        #time.sleep(1)
    print "attempting ssh connection..."
    proc = subprocess.Popen(["C:\putty\plink.exe", "-ssh", "ec2-user@wintermute.rajlab.com", "-i", 'C:\putty\esimWestCoast2.ppk'], stdin=subprocess.PIPE, stdout=subprocess.PIPE)
    
    #print "wait..."
    #proc.wait()
    #print "wait complete"
    
    
    
    #time.sleep(3)\
    count = 1
    output =  proc.stdout
    theError = proc.stderr
    
    try:
        print "communicate"
        output2, error2 = proc.communicate(input="y\n")
        print "communicate complete"
    except:
        print "except"
    else:
        print "else"
        return False

        
    print "past try catch block"
    return True

def terminateByName(name):
    print "terminateByName: %s" %  name
    connection = boto.connect_ec2()
    allReservations = connection.get_all_instances()
    print "-found %s reservations " % len(allReservations)
    
    for res in allReservations:
        for inst in res.instances:
            inst.update()
            if inst.state == 'running':
                print inst.tags
                if 'Name' in inst.tags :
                    instanceName = inst.tags['Name']
                    print 'Name: ' + instanceName
                    if instanceName == name :
                        print inst.id + ' in state: ' + inst.state + ' - now terminating'
                        inst.terminate()
                else :
                    print 'Name: unknown'
                

    


def terminateAll():
    print '*****terminateAll******'
    connection = boto.connect_ec2()
    print connection
    print connection.region
    
    allReservations = connection.get_all_instances()
    
    print '******* Terminating Instances *******'
    for res in allReservations:
        for inst in res.instances:
            inst.update()
            if inst.state != 'terminated':
                print inst.id + ' in state: ' + inst.state + ' - now terminating'
                inst.terminate()