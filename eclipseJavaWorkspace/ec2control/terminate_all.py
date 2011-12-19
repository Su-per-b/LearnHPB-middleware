#!/usr/bin/env python

import boto

conn = boto.connect_ec2()
print conn
print conn.region

allReservations = conn.get_all_instances()

print '******* Running Reservations *******'
for res in allReservations:
    print res
    
print '******* Running Instances State*******'
for res in allReservations:
    for inst in res.instances:
        print inst.id + ': ' + inst.state
    
print '******* Terminating Instances *******'
for res in allReservations:
    for inst in res.instances:
        inst.update()
        print inst.id + ': ' + inst.state
        if inst.state != 'terminated':
            inst.terminate()

        
print '******* Running Instances State*******'
for res in allReservations:
    for inst in res.instances:
        inst.update()
        print inst.state

exit()
