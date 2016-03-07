package com.anshzinc.testrun;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TestRun extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	float posX = 0.0f; 
	float posY = 0.0f;
	
	float newPosX = 0.0f;
	float newPosY = 0.0f;
	
	Texture img2;
	float posX2 = 0.0f; 
	float posY2 = 0.0f;
	
	private ArrayList<Texture> objs = new ArrayList<Texture>();
		
	private float screenWidth;
	private float screenHeight;
	
	private Socket socket;
	private OutputStream outStream;
	private InputStream inStream;
	
	private String text;
	
	@Override
	public void create () {
		
		connectToServer();
		
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		img2 = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(248, 242, 210, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, posX, posY, 30, 30);
		batch.draw(img2, posX2, posY2, 30, 30);
		batch.end();
		
		tick();
	}
	
	private void tick() {
		handleInput();
		sendDataToServer();
		drawClients();
	}
	
	private void drawClients() {
		System.out.println("drawclients");
		BufferedReader in = null;
		try {
			in = new BufferedReader(
			        new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		 String inputLine;
		 String[] objects;
		 String[] objPos;
		 
		 try {
			 System.out.println("try");
			if (in.ready()) {
				if ((inputLine = in.readLine()) != null) {
					System.out.println("inputLine");
					objects = inputLine.split(";");
					
					if (objects.length > 1) {
						for (String s: objects) {
							objPos = s.split(",");
							this.newPosX = Float.valueOf(objPos[0]);
							this.newPosY = Float.valueOf(objPos[1]);
							
							this.posX2 = Float.valueOf(objPos[0]);
							this.posY2 = Float.valueOf(objPos[1]);
							
							/*if (this.posX2 > this.newPosX) {
								do {
									this.posX2--;
								} while(this.posX2 == this.newPosX);
							} else {
								do {
									this.posX2++;
								} while(this.posX2 == this.newPosX);
							}*/
							
						}
					} else {
						objPos = objects[0].split(",");
						this.newPosX = Float.valueOf(objPos[0]);
						this.newPosY = Float.valueOf(objPos[1]);
						
						this.posX2 = Float.valueOf(objPos[0]);
						this.posY2 = Float.valueOf(objPos[1]);
						
						/*if (this.posX2 > this.newPosX) {
							do {
								this.posX2--;
							} while(this.posX2 == this.newPosX);
						} else {
							do {
								this.posX2++;
							} while(this.posX2 == this.newPosX);
						}*/
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	private void sendDataToServer() {
		String data = String.valueOf(this.posX) + "," + String.valueOf(this.posY) + "\n";
		try {
			this.outStream.write(data.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void connectToServer() {
		try {
			 socket = new Socket("127.0.0.1", 5555);
			
			 if (socket.isConnected()) {
				 System.out.println("Client has connected!");
			 }
			 
			 this.outStream = socket.getOutputStream();
			 this.inStream = socket.getInputStream();
			 
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void handleInput() {
		if (Gdx.input.isKeyPressed(Keys.D)) {
			this.posX++;
		}
		if (Gdx.input.isKeyPressed(Keys.A)) {
			this.posX--;
		}
		
		if (Gdx.input.isKeyPressed(Keys.W)) {
			this.posY++;
		}
		
		if (Gdx.input.isKeyPressed(Keys.S)) {
			this.posY--;
		}
	}
}
