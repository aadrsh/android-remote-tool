import socket
import pynput

from pynput.mouse import Button,Controller
from pynput.keyboard import Controller as KeyboardController

import math
import numpy as np
import thread

def quit():
	sock.send('Close')
	sock.close()
	exit()

def recvData():
	str=""
	while(True):
		rxchar=sock.recv(1)
		str+=rxchar
		if not rxchar : quit()
		if(rxchar=='@'):
			str=''
		if(str!='' and str[-1]=='#'):
			break
	print('Received '+str[:-1])
	return str[0:-1]

def selectDevice(command):
	if(command[:2]=='MO'):
		devmouse(command[3:])
	elif(command[:2]=='JS'):
		thread.start_new_thread(devjoystick,(command[3:],))


def devmouse(command):
	action=command[:4]
	data=command[5:]
	print(data)
	if(action=='MOVE'):
		x,y=data.split(',')
		mouse.move(float(x),-float(y))
	elif(action=='PRES'):
		if(data=='LEFT'):
			mouse.press(Button.left)
		elif(data=='RIGHT'):
			mouse.press(Button.right)
	elif(action=='RLSE'):
		if(data=='LEFT'):
			mouse.release(Button.left)
		elif(data=='RIGHT'):
			mouse.release(Button.right)

def devjoystick(command):
	action=command[:4]
	data=command[5:]
	if(command[:4]=='PRES'):
		for each in tuple(data):
			keyboard.press(each)
			print("To press "+each)
	elif(command[:4]=='RLSE'):
		for each in tuple(data,):
			keyboard.release(each)

mouse=Controller()
keyboard=KeyboardController()
sock=socket.socket()
sock.connect(("192.168.43.229",12345))#change ip here
while(True):
	try:
		data=recvData()
		selectDevice(data)
	except KeyboardInterrupt:
		quit()

