#!/usr/bin/env python

import boto
import time
import os


def terminateAll():
    allReservations = conn.get_all_instances()
    print '******* Terminating Instances *******'
    for res in allReservations:
        for inst in res.instances:
            inst.update()
            if inst.state != 'terminated':
                print inst.id + ' in state: ' + inst.state + ' - now terminating'
                inst.terminate()

def checkForRunning(instance):
    instance.update()
    print "checkForRunning : %s" % instance.id
    print "state : %s" % instance.state
    
    if instance.state == 'running':
        associateIP(instance)
        ssh()
    else:
        time.sleep( 1 )
        checkForRunning(instance)



def associateIP(instance):
    print '***** Associate IP******'

    addresses = conn.get_all_addresses();

    print 'IP: ' + addresses[0].public_ip
    #print 'allocation_id: ' + addresses[0].allocation_id
    #print 'association_id: ' + addresses[0].association_id

    addresses[0].associate(instance.id)

#    print 'allocation_id: ' + addresses[0].allocation_id
#    print 'association_id: ' + addresses[0].association_id

def ssh():
    print '***** SSH ******'
    #time.sleep( 2 )
    #print os.system('"C:\Program Files (x86)\putty\putty.exe" -load esim-wintermute-west')


ami = 'ami-' + '8f114fca'
key_name = 'esimWestCoast'
security_groups = ['esim']
instance_type='t1.micro'


conn = boto.connect_ec2()
print conn
print conn.region


groups = conn.get_all_security_groups()
print groups

images = conn.get_all_images(image_ids=[ami])
print images


terminateAll()

reservation = images[0].run(1,1, key_name, security_groups, None, None, instance_type)
#print reservation.instances

print '*****New Instance******'
for inst in reservation.instances:
    print inst

checkForRunning(inst)





