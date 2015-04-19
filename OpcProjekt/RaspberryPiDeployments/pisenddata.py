#!/usr/bin/env python
import nxppy
import pika
import time
import calendar
import xml.etree.cElementTree as ET

#Globale Variablen
sourceSystem = 'pi'
type = 'PiTag'
queueName = 'OPC_QUEUED_DATA'
queueIP = '192.168.176.20'
queueUserName = 'opc'
queuePassword = '123456'
queuePort = 5672

#Variable fuer Tag
tag = '0'
thisTime = '0'

mifare = nxppy.Mifare()
while True:
	try:
		#read Tag and get read-Timestamp
		tag = mifare.select()
		thisTime = time.time()
		
		#Convert float Timestamp into String
		#Float cant converted to XML
		thisTimeStr = "%.15f" % thisTime
		#Remove the . after conversion
		timeStr = str.split(thisTimeStr, '.')
		#tmp includes the timestamp as a string
		tmp = timeStr[0]

		#Generate XML
		piTag = ET.Element("PiNfc")

		ET.SubElement(piTag, "tag").text = tag
		ET.SubElement(piTag, "readTime").text = '' + tmp

		tree = ET.ElementTree(piTag)
		xmlstr = ET.tostring(piTag, encoding='utf8', method='xml')

		#this queue is the destination queue
		credentials = pika.PlainCredentials(queueUserName, queuePassword)
		parameters = pika.ConnectionParameters(queueIP, queuePort, '/', credentials)
		connection = pika.BlockingConnection(parameters)
		print("Connection created")

		channel = connection.channel()
		channel.queue_declare(queue=queueName)

		#send data + send-Timestamp
		channel.basic_publish(
			exchange='',
			routing_key=queueName,
			body=xmlstr,
			properties=pika.BasicProperties(headers={'sourceSystem':sourceSystem, 'type':type}, timestamp=calendar.timegm(time.gmtime())))

		print("Sended" + tag)

		connection.close()

	#error handling	
	except nxppy.SelectError:
		pass
