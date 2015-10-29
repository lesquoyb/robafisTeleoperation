import pygame
import math
from math import pi
import socket

WINDOW_WIDTH = 1000
WINDOW_HEIGHT= 600

# Define some colors
BLACK    = (   0,   0,   0)
GRAY     = ( 120, 120, 120)
LIGHTGRAY= ( 230, 230, 230)
WHITE    = ( 255, 255, 255)

LIGHTBLUE= ( 100, 100, 255)
BLUE     = (   0,   0, 255)
GREEN    = (   0, 255,   0)
RED      = ( 255,   0,   0)
YELLOW   = ( 255, 255,   0)


import pygame
from pygame.locals import *

pygame.font.init()
PYGBUTTON_FONT = pygame.font.Font('freesansbold.ttf', 14)


class PygButton(object):
    def __init__(self, rect=None, caption='', bgcolor=LIGHTGRAY, fgcolor=BLACK, font=None, normal=None, down=None, highlight=None):
        """Create a new button object. Parameters:
            rect - The size and position of the button as a pygame.Rect object
                or 4-tuple of integers.
            caption - The text on the button (default is blank)
            bgcolor - The background color of the button (default is a light
                gray color)
            fgcolor - The foreground color (i.e. the color of the text).
                Default is black.
            font - The pygame.font.Font object for the font of the text.
                Default is freesansbold in point 14.
            normal - A pygame.Surface object for the button's normal
                appearance.
            down - A pygame.Surface object for the button's pushed down
                appearance.
            highlight - A pygame.Surface object for the button's appearance
                when the mouse is over it.

            If the Surface objects are used, then the caption, bgcolor,
            fgcolor, and font parameters are ignored (and vice versa).
            Specifying the Surface objects lets the user use a custom image
            for the button.
            The normal, down, and highlight Surface objects must all be the
            same size as each other. Only the normal Surface object needs to
            be specified. The others, if left out, will default to the normal
            surface.
            """
        if rect is None:
            self._rect = pygame.Rect(0, 0, 30, 60)
        else:
            self._rect = pygame.Rect(rect)

        self._caption = caption
        self._bgcolor = bgcolor
        self._fgcolor = fgcolor

        if font is None:
            self._font = PYGBUTTON_FONT
        else:
            self._font = font

        # tracks the state of the button
        self.buttonDown = False # is the button currently pushed down?
        self.mouseOverButton = False # is the mouse currently hovering over the button?
        self.lastMouseDownOverButton = False # was the last mouse down event over the mouse button? (Used to track clicks.)
        self._visible = True # is the button visible
        self.customSurfaces = False # button starts as a text button instead of having custom images for each surface

        if normal is None:
            # create the surfaces for a text button
            self.surfaceNormal = pygame.Surface(self._rect.size)
            self.surfaceDown = pygame.Surface(self._rect.size)
            self.surfaceHighlight = pygame.Surface(self._rect.size)
            self._update() # draw the initial button images
        else:
            # create the surfaces for a custom image button
            self.setSurfaces(normal, down, highlight)

    def handleEvent(self, eventObj):
        """All MOUSEMOTION, MOUSEBUTTONUP, MOUSEBUTTONDOWN event objects
        created by Pygame should be passed to this method. handleEvent() will
        detect if the event is relevant to this button and change its state.

        There are two ways that your code can respond to button-events. One is
        to inherit the PygButton class and override the mouse*() methods. The
        other is to have the caller of handleEvent() check the return value
        for the strings 'enter', 'move', 'down', 'up', 'click', or 'exit'.

        Note that mouseEnter() is always called before mouseMove(), and
        mouseMove() is always called before mouseExit(). Also, mouseUp() is
        always called before mouseClick().

        buttonDown is always True when mouseDown() is called, and always False
        when mouseUp() or mouseClick() is called. lastMouseDownOverButton is
        always False when mouseUp() or mouseClick() is called."""

        if eventObj.type not in (MOUSEMOTION, MOUSEBUTTONUP, MOUSEBUTTONDOWN) or not self._visible:
            # The button only cares bout mouse-related events (or no events, if it is invisible)
            return []

        retVal = []

        hasExited = False
        if not self.mouseOverButton and self._rect.collidepoint(eventObj.pos):
            # if mouse has entered the button:
            self.mouseOverButton = True
            self.mouseEnter(eventObj)
            retVal.append('enter')
        elif self.mouseOverButton and not self._rect.collidepoint(eventObj.pos):
            # if mouse has exited the button:
            self.mouseOverButton = False
            hasExited = True # call mouseExit() later, since we want mouseMove() to be handled before mouseExit()

        if self._rect.collidepoint(eventObj.pos):
            # if mouse event happened over the button:
            if eventObj.type == MOUSEMOTION:
                self.mouseMove(eventObj)
                retVal.append('move')
            elif eventObj.type == MOUSEBUTTONDOWN:
                self.buttonDown = True
                self.lastMouseDownOverButton = True
                self.mouseDown(eventObj)
                retVal.append('down')
        else:
            if eventObj.type in (MOUSEBUTTONUP, MOUSEBUTTONDOWN):
                # if an up/down happens off the button, then the next up won't cause mouseClick()
                self.lastMouseDownOverButton = False

        # mouse up is handled whether or not it was over the button
        doMouseClick = False
        if eventObj.type == MOUSEBUTTONUP:
            if self.lastMouseDownOverButton:
                doMouseClick = True
            self.lastMouseDownOverButton = False

            if self.buttonDown:
                self.buttonDown = False
                self.mouseUp(eventObj)
                retVal.append('up')

            if doMouseClick:
                self.buttonDown = False
                self.mouseClick(eventObj)
                retVal.append('click')

        if hasExited:
            self.mouseExit(eventObj)
            retVal.append('exit')

        return retVal

    def draw(self, surfaceObj):
        """Blit the current button's appearance to the surface object."""
        if self._visible:
            if self.buttonDown:
                surfaceObj.blit(self.surfaceDown, self._rect)
            elif self.mouseOverButton:
                surfaceObj.blit(self.surfaceHighlight, self._rect)
            else:
                surfaceObj.blit(self.surfaceNormal, self._rect)


    def _update(self):
        """Redraw the button's Surface object. Call this method when the button has changed appearance."""
        if self.customSurfaces:
            self.surfaceNormal    = pygame.transform.smoothscale(self.origSurfaceNormal, self._rect.size)
            self.surfaceDown      = pygame.transform.smoothscale(self.origSurfaceDown, self._rect.size)
            self.surfaceHighlight = pygame.transform.smoothscale(self.origSurfaceHighlight, self._rect.size)
            return

        w = self._rect.width # syntactic sugar
        h = self._rect.height # syntactic sugar

        # fill background color for all buttons
        self.surfaceNormal.fill(self.bgcolor)
        self.surfaceDown.fill(self.bgcolor)
        self.surfaceHighlight.fill(self.bgcolor)

        # draw caption text for all buttons
        captionSurf = self._font.render(self._caption, True, self.fgcolor, self.bgcolor)
        captionRect = captionSurf.get_rect()
        captionRect.center = int(w / 2), int(h / 2)
        self.surfaceNormal.blit(captionSurf, captionRect)
        self.surfaceDown.blit(captionSurf, captionRect)

        # draw border for normal button
        pygame.draw.rect(self.surfaceNormal, BLACK, pygame.Rect((0, 0, w, h)), 1) # black border around everything
        pygame.draw.line(self.surfaceNormal, WHITE, (1, 1), (w - 2, 1))
        pygame.draw.line(self.surfaceNormal, WHITE, (1, 1), (1, h - 2))
        pygame.draw.line(self.surfaceNormal, DARKGRAY, (1, h - 1), (w - 1, h - 1))
        pygame.draw.line(self.surfaceNormal, DARKGRAY, (w - 1, 1), (w - 1, h - 1))
        pygame.draw.line(self.surfaceNormal, GRAY, (2, h - 2), (w - 2, h - 2))
        pygame.draw.line(self.surfaceNormal, GRAY, (w - 2, 2), (w - 2, h - 2))

        # draw border for down button
        pygame.draw.rect(self.surfaceDown, BLACK, pygame.Rect((0, 0, w, h)), 1) # black border around everything
        pygame.draw.line(self.surfaceDown, WHITE, (1, 1), (w - 2, 1))
        pygame.draw.line(self.surfaceDown, WHITE, (1, 1), (1, h - 2))
        pygame.draw.line(self.surfaceDown, DARKGRAY, (1, h - 2), (1, 1))
        pygame.draw.line(self.surfaceDown, DARKGRAY, (1, 1), (w - 2, 1))
        pygame.draw.line(self.surfaceDown, GRAY, (2, h - 3), (2, 2))
        pygame.draw.line(self.surfaceDown, GRAY, (2, 2), (w - 3, 2))

        # draw border for highlight button
        self.surfaceHighlight = self.surfaceNormal


    def mouseClick(self, event):
        pass # This class is meant to be overridden.
    def mouseEnter(self, event):
        pass # This class is meant to be overridden.
    def mouseMove(self, event):
        pass # This class is meant to be overridden.
    def mouseExit(self, event):
        pass # This class is meant to be overridden.
    def mouseDown(self, event):
        pass # This class is meant to be overridden.
    def mouseUp(self, event):
        pass # This class is meant to be overridden.


    def setSurfaces(self, normalSurface, downSurface=None, highlightSurface=None):
        """Switch the button to a custom image type of button (rather than a
        text button). You can specify either a pygame.Surface object or a
        string of a filename to load for each of the three button appearance
        states."""
        if downSurface is None:
            downSurface = normalSurface
        if highlightSurface is None:
            highlightSurface = normalSurface

        if type(normalSurface) == str:
            self.origSurfaceNormal = pygame.image.load(normalSurface)
        if type(downSurface) == str:
            self.origSurfaceDown = pygame.image.load(downSurface)
        if type(highlightSurface) == str:
            self.origSurfaceHighlight = pygame.image.load(highlightSurface)

        if self.origSurfaceNormal.get_size() != self.origSurfaceDown.get_size() != self.origSurfaceHighlight.get_size():
            raise Exception('foo')

        self.surfaceNormal = self.origSurfaceNormal
        self.surfaceDown = self.origSurfaceDown
        self.surfaceHighlight = self.origSurfaceHighlight
        self.customSurfaces = True
        self._rect = pygame.Rect((self._rect.left, self._rect.top, self.surfaceNormal.get_width(), self.surfaceNormal.get_height()))



    def _propGetCaption(self):
        return self._caption


    def _propSetCaption(self, captionText):
        self.customSurfaces = False
        self._caption = captionText
        self._update()


    def _propGetRect(self):
        return self._rect


    def _propSetRect(self, newRect):
        # Note that changing the attributes of the Rect won't update the button. You have to re-assign the rect member.
        self._update()
        self._rect = newRect


    def _propGetVisible(self):
        return self._visible


    def _propSetVisible(self, setting):
        self._visible = setting


    def _propGetFgColor(self):
        return self._fgcolor


    def _propSetFgColor(self, setting):
        self.customSurfaces = False
        self._fgcolor = setting
        self._update()


    def _propGetBgColor(self):
        return self._bgcolor


    def _propSetBgColor(self, setting):
        self.customSurfaces = False
        self._bgcolor = setting
        self._update()


    def _propGetFont(self):
        return self._font


    def _propSetFont(self, setting):
        self.customSurfaces = False
        self._font = setting
        self._update()


    caption = property(_propGetCaption, _propSetCaption)
    rect = property(_propGetRect, _propSetRect)
    visible = property(_propGetVisible, _propSetVisible)
    fgcolor = property(_propGetFgColor, _propSetFgColor)
    bgcolor = property(_propGetBgColor, _propSetBgColor)
    font = property(_propGetFont, _propSetFont)

	
	
	
	
	
	
	
	
    
def draw_rect(x, y, w, h, c, f):
    if f:
        pygame.draw.rect(screen, c, [x, y, w, h])
    else:
        pygame.draw.rect(screen, c, [x, y, w, h], 2)

def draw_circle(x, y, w, h, c, f):
    if f:
        pygame.draw.ellipse(screen, c, [x, y, w, h])
    else:
        pygame.draw.ellipse(screen, c, [x, y, w, h], 2)

def draw_map():
    draw_rect(100, 50, 100, 200, BLACK, True)
    pygame.draw.line(screen, BLACK, [200, 50], [650, 50], 2)
    pygame.draw.line(screen, BLACK, [200, 56], [650, 56], 2)
    pygame.draw.line(screen, BLACK, [200, 244], [650, 244], 2)
    pygame.draw.line(screen, BLACK, [200, 250], [650, 250], 2)
    pygame.draw.line(screen, BLACK, [694, 100], [694, 200], 2)
    pygame.draw.line(screen, BLACK, [700, 100], [700, 200], 2)
    pygame.draw.arc(screen, BLACK, [601, 56, 95, 95], 0, pi/2, 2)
    pygame.draw.arc(screen, BLACK, [601, 50, 100, 100], 0, pi/2, 2)
    pygame.draw.arc(screen, BLACK, [601, 150, 95, 95], 3*pi/2, 2*pi, 2)
    pygame.draw.arc(screen, BLACK, [601, 150, 100, 100], 3*pi/2, 2*pi, 2)
    draw_circle(296, 244, 8, 8, GREEN, True)
    draw_rect(275, 150, 50, 50, BLACK, False)
    draw_circle(496, 244, 8, 8, YELLOW, True)
    draw_rect(475, 150, 50, 50, BLACK, False)
    draw_circle(596, 244, 8, 8, RED, True)
    draw_rect(575, 150, 50, 50, BLACK, False)

def draw_bot(x, y, o):
    if o == 0:
        draw_rect(x-10, y-20, 20, 40, LIGHTBLUE, True)
        draw_rect(x-10, y-20, 20, 40, BLUE, False)
        draw_circle(x-13, y-15, 4, 8, BLUE, True)
        draw_circle(x+10, y-15, 4, 8, BLUE, True)
        draw_circle(x-3, y+20, 6, 8, BLUE, True)
    if o == 1:
        draw_rect(x-10, y-20, 20, 40, LIGHTBLUE, True)
        draw_rect(x-10, y-20, 20, 40, BLUE, False)
        draw_circle(x-13, y+15, 4, 8, BLUE, True)
        draw_circle(x+10, y+15, 4, 8, BLUE, True)
        draw_circle(x-3, y-25, 6, 8, BLUE, True)
    if o == 2:
        draw_rect(x-20, y-10, 40, 20, LIGHTBLUE, True)
        draw_rect(x-20, y-10, 40, 20, BLUE, False)
        draw_circle(x-15, y-13, 8, 4, BLUE, True)
        draw_circle(x-15, y+10, 8, 4, BLUE, True)
        draw_circle(x+20, y-3, 8, 4, BLUE, True)
    if o == 3:
        draw_rect(x-20, y-10, 40, 20, LIGHTBLUE, True)
        draw_rect(x-20, y-10, 40, 20, BLUE, False)
        draw_circle(x+15, y-13, 8, 4, BLUE, True)
        draw_circle(x+15, y+10, 8, 4, BLUE, True)
        draw_circle(x-25, y-3, 8, 6, BLUE, True)

def connection(ip):
	global client_socket
	client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	client_socket.connect((ip, 5000))
	

def connect_joystick():
	global joystick
	# Initialize the joysticks
	if pygame.joystick.get_count() != 0:
		joystick = pygame.joystick.Joystick(0)
		joystick.init()
		return True
	return False
		
		
def window_init():
	global screen
	pygame.init()	
	
	pygame.joystick.init()
		
	# Set the width and height of the screen [width,height]
	size = [WINDOW_WIDTH, WINDOW_HEIGHT]
	screen = pygame.display.set_mode(size)

	pygame.display.set_caption("LEGO RANGERS")

def close():
	# sock.close()
	pygame.quit ()
		
	
def center_joystick(XAxis):
	if(XAxis == 0):
		return WINDOW_WIDTH/3
	else:
		return WINDOW_WIDTH - WINDOW_WIDTH/3
	
def draw_joystick(XAxis):
	draw_circle(center_joystick(XAxis) -50, WINDOW_HEIGHT -50 -50, 100, 100, GRAY, False)
	draw_circle(center_joystick(XAxis) -10, WINDOW_HEIGHT -50 -10, 20, 20, GRAY, False)

	
def draw_cross():
	draw_rect(WINDOW_WIDTH/4 -20, WINDOW_HEIGHT -150 -60, 40, 40, GRAY, False)
	draw_rect(WINDOW_WIDTH/4 -20, WINDOW_HEIGHT -150 +20, 40, 40, GRAY, False)
	draw_rect(WINDOW_WIDTH/4 +20, WINDOW_HEIGHT -150 -20, 40, 40, GRAY, False)
	draw_rect(WINDOW_WIDTH/4 -60, WINDOW_HEIGHT -150 -20, 40, 40, GRAY, False)
	
def draw_buttons():
	draw_circle(WINDOW_WIDTH - WINDOW_WIDTH/4 -20, WINDOW_HEIGHT -150 -60, 40, 40, GRAY, False)
	draw_circle(WINDOW_WIDTH - WINDOW_WIDTH/4 -20, WINDOW_HEIGHT -150 +20, 40, 40, GRAY, False)
	draw_circle(WINDOW_WIDTH - WINDOW_WIDTH/4 +20, WINDOW_HEIGHT -150 -20, 40, 40, GRAY, False)
	draw_circle(WINDOW_WIDTH - WINDOW_WIDTH/4 -60, WINDOW_HEIGHT -150 -20, 40, 40, GRAY, False)
	
def draw_joystick_state_and_button():
	pass
	#b = new pygButton()
	
	
def xy_joystick(XAxis):
	global joystick
	x = joystick.get_axis(XAxis)
	y = joystick.get_axis(XAxis + 1)
	r = math.sqrt(x*x + y*y) + 1
	y = y/r * 100	
	x = x/r * 100
	return (x,y)

def color_pressed_button():
	global joystick
	global a_pressed
	global x_pressed
	global y_pressed
	global b_pressed
	global up
	global down
	global right
	global left
	
	if up:
		draw_rect(WINDOW_WIDTH/4 -20, WINDOW_HEIGHT -150 -60, 40, 40, BLUE, True)
	if down: 
		draw_rect(WINDOW_WIDTH/4 -20, WINDOW_HEIGHT -150 +20, 40, 40, BLUE, True)
	if right:
		draw_rect(WINDOW_WIDTH/4 +20, WINDOW_HEIGHT -150 -20, 40, 40, BLUE, True)
	if left: 
		draw_rect(WINDOW_WIDTH/4 -60, WINDOW_HEIGHT -150 -20, 40, 40, BLUE, True)
	if y_pressed:
		draw_circle(WINDOW_WIDTH - WINDOW_WIDTH/4 -20, WINDOW_HEIGHT -150 -60, 40, 40, BLUE, False)
	if a_pressed:
		draw_circle(WINDOW_WIDTH - WINDOW_WIDTH/4 -20, WINDOW_HEIGHT -150 +20, 40, 40, BLUE, False)
	if x_pressed:
		draw_circle(WINDOW_WIDTH - WINDOW_WIDTH/4 +20, WINDOW_HEIGHT -150 -20, 40, 40, BLUE, False)
	if b_pressed:
		draw_circle(WINDOW_WIDTH - WINDOW_WIDTH/4 -60, WINDOW_HEIGHT -150 -20, 40, 40, BLUE, False)
	
	(x,y) = xy_joystick(0)
	draw_circle(center_joystick(0) + x -10, WINDOW_HEIGHT -50 + y -10, 20, 20, RED, True)
	(x,y) = xy_joystick(2)
	draw_circle(center_joystick(2) + x -10, WINDOW_HEIGHT -50 + y -10, 20, 20, RED, True)
	
def process_events():
	global joystick
	global a_pressed
	global x_pressed
	global y_pressed
	global b_pressed
	global up
	global down
	global right
	global left
	global done
	
	for event in pygame.event.get():
		if event.type == pygame.QUIT:
			done = True
		elif event.type == pygame.MOUSEBUTTONDOWN:
			print(event)
	
	if joystick:
		up = False
		down = False
		right = False
		left = False
		a_pressed = False
		x_pressed = False
		y_pressed = False
		b_pressed = False

		#DPad
		if joystick.get_hat(0)[1]==1:
			up = True
		if joystick.get_hat(0)[1]==-1:
			down = True
		if joystick.get_hat(0)[0]==1: 
			right = True
		if joystick.get_hat(0)[0]==-1:
			left = True
		
		#buttons
		if joystick.get_button(0):
			a_pressed = True
		if joystick.get_button(2):
			x_pressed = True
		if joystick.get_button(1):
			y_pressed = True
		if joystick.get_button(3):
			b_pressed = True
	
def draw_screen():
	global screen
	# CLEAR THE SCREEN
	screen.fill(LIGHTGRAY)
	
	draw_map()
	draw_bot(250, 54, 2)
	
	# CROSS
	draw_cross()
	
	draw_buttons()
	
	draw_joystick(0)
	draw_joystick(2)
	
	if joystick:
		color_pressed_button()
	
	pygame.display.flip()
		

def send_data():
	global client_socket
	global joystick
	if client_socket:
		#send left stick position
		data = "move:%.2f,%.2f\n" % (joystick.get_axis(0), joystick.get_axis(1))
		print(data)
		client_socket.send(data.encode())
	
	
	
#global variables
screen = None
joystick = None
client_socket = None
default_ip = "10.0.1.1"
up = False
down = False
right = False
left = False
a_pressed = False
x_pressed = False
y_pressed = False
b_pressed = False
if __name__ == "__main__":
	
	# Used to manage how fast the screen updates
	clock = pygame.time.Clock()
	
	window_init()
	
	connect_joystick()
	connection("10.0.1.1")
	done = False
	while not done:		
			
		draw_screen()
		
		process_events()
				
		send_data()
		
		# FPS
		clock.tick(10)
	
	close()
	
	
	
	# port = 12345
	# bd_addr = ""

	# try:
	   # nearby_devices = bluetooth.discover_devices(lookup_names=True)
	   # print("found %d devices" % len(nearby_devices))

	   # for addr, name in nearby_devices:
		   # print(" %s - %s" % (addr, name) )
		   # bd_addr = addr
	# except OSError:
	   # print("Bluetooth inactif ou pas de peripheriques bluetooth detectes")
	   # pygame.quit ()
	   # raise SystemExit


	# sock=bluetooth.BluetoothSocket( bluetooth.RFCOMM )
	# sock.connect((bd_addr, port))

	# sock.send("hello!!")
	# potato = sock.recv(1024)