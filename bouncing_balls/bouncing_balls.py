# Bouncing Balls 
# Version 1.0 - Date: August 22rd
# Version 1.1 - Date: August 23rd: Adding sound when the ball hit the circle border. 

# Next task: count the number of the ball in the circle and display it
# Next task: add a restart button
# Next task: make a ball is able to hit another ball


import pygame as pg
import numpy as np 
import math as mt
import random as rd
import pyglet as pl

class Ball:
    def __init__(self, position, velocity):
    	self.pos = np.array(position, dtype=np.float64)
    	self.vel = np.array(velocity, dtype=np.float64)
    	self.color = (rd.randint(0, 255), rd.randint(0, 255), rd.randint(0, 255))
    	self.is_in_arc = True

def draw_arc(window, color, center, radius, start_angle, end_angle):
	p1 = center + (radius + 1000) * np.array([mt.cos(start_angle), mt.sin(start_angle)])
	p2 = center + (radius + 1000) * np.array([mt.cos(end_angle), mt.sin(end_angle)])
	pg.draw.polygon(window, color, [center, p1, p2], 0)

def is_ball_in_arc(ball_position, circle_center, start_angle, end_angle):
	dx = ball_position[0] - circle_center[0]
	dy = ball_position[1] - circle_center[1]

	ball_angle = mt.atan2(dy, dx)
	start_angle = start_angle % (2 * mt.pi)
	end_angle = end_angle % (2 * mt.pi)

	if start_angle > end_angle:
		end_angle += 2 * mt.pi 
	if (start_angle <= ball_angle <= end_angle) or (start_angle <= ball_angle + 2 * mt.pi <= end_angle):
		return True

# def restart():
# 	text = bigfont.reader("Restart ?", 13 (0, 0, 0))
# 	text
pg.init()
pg.display.set_caption("Bouncing Balls")

WIDTH = 800
HEIGHT = 800
BLACK = (0, 0, 0)
ORANGE = (255, 165, 0)
RED = (255, 0, 0)
WHITE = (255, 255, 255)

window = pg.display.set_mode((WIDTH, HEIGHT))
clock = pg.time.Clock()

circle_center = np.array([WIDTH/2, HEIGHT/2], dtype=np.float64)
circle_radius = 250

arc_degree = 60
start_angle = mt.radians(-arc_degree/2)
end_angle = mt.radians(arc_degree/2)

spinning_speed = 0.01
GRAVITY = 0.09
running = True

ball_radius = 15
ball_position = np.array([WIDTH/2, HEIGHT/2 - 120], dtype=np.float64)
ball_velocity = np.array([0, 0], dtype=np.float64)
balls = [Ball(ball_position, ball_velocity)]

# sound = pl.resource.media('game_ball_tap.wav')
# sound.play()
# pl.app.run()

ball_count = 1
font = pg.font.Font(None, 74)

ball_hit_arc_sound = pg.mixer.Sound('game_ball_tap.wav')

while running:
	for event in pg.event.get():
		if event.type == pg.QUIT:
			running = False
	start_angle += spinning_speed
	end_angle += spinning_speed

	for ball in balls:
		if ball.pos[1] > HEIGHT or ball.pos[0] < 0 or ball.pos[0] > WIDTH or ball.pos[1] < 0:
		# if ball.pos[1] > (circle_radius * 2 + (HEIGHT - circle_radius *2)/2) or ball.pos[0] < (WIDTH - circle_radius)/2 or ball.pos[0] > (circle_radius * 2 + (WIDTH - circle_radius *2)/2)  or ball.pos[1] < (HEIGHT - circle_radius)/2:
			balls.remove(ball)
			balls.append(Ball([rd.uniform(circle_radius, circle_radius + circle_radius * 2/mt.sqrt(2)), rd.uniform(circle_radius, circle_radius + circle_radius * 2/mt.sqrt(2))], [rd.uniform(-4, 4), rd.uniform(-1, 1)]))
			balls.append(Ball([rd.uniform(circle_radius, circle_radius + circle_radius * 2/mt.sqrt(2)), rd.uniform(circle_radius, circle_radius + circle_radius * 2/mt.sqrt(2))], [rd.uniform(-4, 4), rd.uniform(-1, 1)]))
			
			# ball_count += 1
			# print(ball_count)
			# abc = font.render(str(ball_count), True, WHITE)
			# text_rect = abc.get_rect(center=(WIDTH // 2, HEIGHT + 20))
			# window.blit(abc, text_rect)

			ball_count += 1

		# ball_pos[1] = ball_pos[1] + GRAVITY
		ball.vel[1] += GRAVITY
		ball.pos += ball.vel

		dis = np.linalg.norm(ball.pos - circle_center)
		if dis + ball_radius > circle_radius:
			if is_ball_in_arc(ball.pos, circle_center, start_angle, end_angle):
				ball.is_in_arc = False
			if ball.is_in_arc:
				d = ball.pos - circle_center
				d_unit = d/np.linalg.norm(d)
				ball.pos = circle_center + (circle_radius - ball_radius) * d_unit
				t = np.array([-d[1], d[0]], dtype=np.float64)
				projection_v_t = (np.dot(ball.vel, t)/np.dot(t, t)) * t
				ball.vel = 2 * projection_v_t - ball.vel
				ball.vel += t * spinning_speed
				ball_hit_arc_sound.play()

		abc = font.render(str(ball_count), True, WHITE)
		text_rect = abc.get_rect(center=(WIDTH // 2, HEIGHT - 20))
		window.blit(abc, text_rect)

	window.fill(BLACK)
	pg.draw.circle(window, ORANGE, circle_center, circle_radius, 3)
	draw_arc(window, BLACK, circle_center, circle_radius, start_angle, end_angle)
	for ball in balls:
		pg.draw.circle(window, ball.color, ball.pos, ball_radius)
	pg.display.flip()
	clock.tick(60)

pg.quit()

