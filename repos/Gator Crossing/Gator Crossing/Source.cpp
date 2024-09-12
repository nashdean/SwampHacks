/* Authors: Andres Inguanzo, Jack Hillery, Nash Dean, Nathan Scwartz, Rishabh Patel, and Camryn Cominsky
/* COP3503 Group 38
/* Term Project: "Gator Crossing" */

#include "iostream"
#include "time.h"
#include "conio.h"
#include "windows.h"
#include "MMSystem.h"
#pragma comment(lib,"Winmm.lib")

using namespace std;

int speeds[5]; //Represents the array of randomly generated speeds
int const enemyNum = 25; //Represents the # of obstacles/enemies on each lane
const int screenWidth = 60, screenHeight = 35; //Represent the width and height of the game console

enum Dir { STOP = 0, LEFT = 1, RIGHT = 2, UP = 3, DOWN = 4 };

struct Position
{
	int x;
	int y;
};

//Creates a new window with inputed console size
void SetConsoleSize(int width, int height)
{
	HWND console = GetConsoleWindow();
	RECT r;
	GetWindowRect(console, &r);
	MoveWindow(console, r.left, r.top, width, height, TRUE);

}

//Generates the speeds for the Obstacles in the game to a set of 5 random values between 1 and 2
void initSpeeds(int* speeds) {
	for (int i = 0; i < 5; i++) {
		speeds[i] = rand() % 2 + 1;
	}
}

//Removes the blinking character cursor from screen
//so that when console creates/deletes new enemies,
//the cursor is not seen scrolling on the screen
void removeCursor()
{
	CONSOLE_CURSOR_INFO curInfo;
	GetConsoleCursorInfo(GetStdHandle(STD_OUTPUT_HANDLE), &curInfo);
	curInfo.bVisible = 0;
	SetConsoleCursorInfo(GetStdHandle(STD_OUTPUT_HANDLE), &curInfo);
}

//Goes to the x and y coordinate in the console
void goToXY(int x, int y)
{
	HANDLE hConsole = GetStdHandle(STD_OUTPUT_HANDLE);

	_COORD pos;
	pos.X = x;
	pos.Y = y;

	SetConsoleCursorPosition(hConsole, pos);
}

//Allows text color to change
void TextColor(int x)
{
	HANDLE mau;
	mau = GetStdHandle(STD_OUTPUT_HANDLE);
	SetConsoleTextAttribute(mau, x);
}

//Initializes console window
void WindowInit()
{
	SetConsoleSize(470, 640);
	removeCursor();
}


//The object that represents the player character
class Gator
{
private:
	Position originalPivotPos; Position pivotPos;
	Dir direction;

public:
	Position bodyPos[3][3];
	//Determines the player's current position
	inline void CalcBodyPos()
	{
		//Does not allow Gator to go off screen
		if (pivotPos.x <= 2)
			pivotPos.x = 2;
		if (pivotPos.x > screenWidth - 2)
			pivotPos.x = screenWidth - 2;

		if (pivotPos.y > screenHeight - 2)
			pivotPos.y = screenHeight - 3;

		//Calculates the change in body position
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				if (i < 1)
				{
					bodyPos[i][j].y = pivotPos.y - 1;
				}
				else if (i > 1)
				{
					bodyPos[i][j].y = pivotPos.y + 1;
				}
				else
				{
					bodyPos[i][j].y = pivotPos.y;
				}

				if (j < 1)
				{
					bodyPos[i][j].x = pivotPos.x - 1;
				}
				else if (j > 1)
				{
					bodyPos[i][j].x = pivotPos.x + 1;
				}
				else
				{
					bodyPos[i][j].x = pivotPos.x;
				}
			}
		}
	}

	//Constructor
	Gator(int posX, int posY)
	{
		originalPivotPos.x = posX;
		originalPivotPos.y = posY;
		pivotPos = originalPivotPos;
		CalcBodyPos();
		direction = STOP;
	}
	//Resets the Gator to the default location
	void Reset()
	{
		pivotPos = originalPivotPos;
		CalcBodyPos();
		direction = STOP;
	}
	//Increments the Gator's position based on user key input
	void Move()
	{
		switch (direction)
		{
		case STOP:
			break;

		case LEFT:
			pivotPos.x -= 3;
			break;

		case RIGHT:
			pivotPos.x += 3;
			break;

		case UP:
			pivotPos.y -= 3;
			break;

		case DOWN:
			pivotPos.y += 3;
			break;

		default:
			break;
		}
		CalcBodyPos();
	}

	//Getter method for the player's y-pivot position
	inline int getPivotPosY() { return pivotPos.y; }
	//Setter method for the player's direction
	inline void setDirection(Dir dir) { direction = dir; }
};

//Enemy or Blank Space Class
class Obstacle
{
private:
	int speed; char bodyChar;
	Position originalPivotPos; Position pivotPos;
	Dir direction;
	int level;
public:
	Position bodyPos[3][3];
	//Determines enemies/blankspaces current position
	inline void CalcBodyPos()
	{
		if (pivotPos.x > screenWidth)
		{
			pivotPos.x = -1;

		}
		//Resets the position if the enemy/blankspace
		//reaches the screen width
		if (pivotPos.x < -1)
		{
			pivotPos.x = screenWidth - 1;

		}

		//Calculates the change in body position for the enemy/free space
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				if (i < 1)
				{
					bodyPos[i][j].y = pivotPos.y - 1;
				}
				else if (i > 1)
				{
					bodyPos[i][j].y = pivotPos.y + 1;
				}
				else
				{
					bodyPos[i][j].y = pivotPos.y;
				}

				if (j < 1)
				{
					bodyPos[i][j].x = pivotPos.x - 1;
				}
				else if (j > 1)
				{
					bodyPos[i][j].x = pivotPos.x + 1;
				}
				else
				{
					bodyPos[i][j].x = pivotPos.x;
				}
			}
		}
	}

	//Constructs the "Obstacle" object
	Obstacle(int posX, int posY, Dir dir, int spd, char ch)
	{
		originalPivotPos.x = posX;
		originalPivotPos.y = posY;
		pivotPos = originalPivotPos;
		CalcBodyPos();

		speed = spd;
		direction = dir;
		bodyChar = ch;
	}

	//Resets the enemy to the original/default position
	void Reset()
	{
		pivotPos = originalPivotPos;
		CalcBodyPos();
	}

	//Changes the position of the "Obstacle"
	void Move()
	{
		switch (direction)
		{
		case STOP:
			break;

		case LEFT:
			pivotPos.x -= speed;
			break;

		case RIGHT:
			pivotPos.x += speed;
			break;

		default:
			break;
		}
		CalcBodyPos();
	}

	//Getter method for bodyChar
	inline char getBodyChar() { return bodyChar; }
};

//The GameManager Class which handles the creation and maintenance of all objects in the game
class GameManager
{
private:
	int score; //Current score
	int level; //Current level
	int width; int height; //Width and height of the game screen

						   //Booleans that are changed throughout the game's runtime and checked by Run()
	bool gameOver; bool paused; bool playing;


	Gator *player; //Pointer to a Gator object
	Obstacle *enemy[5][enemyNum]; //2D array of pointers to Obstacle objects, represents the set of enemies in one level								

public:

	//The constructor for the GameManager Class
	//Takes in w - the desired width, and h - the desired height of the screen
	GameManager(int h, int w)
	{
		height = h;
		width = w;
		score = 0;
		gameOver = false;
		paused = false;
		playing = false;
		level = 1;

		//Gator is created
		player = new Gator(width / 2 - 1, height - 3);

		int randomDir;
		for (int i = 0; i < 5; i++) //i represents the road #
		{
			randomDir = rand() % 2 + 1; //A direction (right = 1, left = 2) is randomly chosen
			for (int j = 0; j < enemyNum; j++) //j represents the obstacle # on each road
			{
				//Creates appropriate Obstacles
				if (randomDir == 1)
				{
					if (j % 10 != 0)
					{
						enemy[i][j] = new Obstacle(2 * (j + 2), i * 6 + 2, RIGHT, speeds[i], ' ');
					}
					else
					{
						enemy[i][j] = new Obstacle(2 * (j + 2), i * 6 + 2, RIGHT, speeds[i], '>');
					}
				}
				else if (randomDir == 2)
				{
					if (j % 10 != 0)
					{
						enemy[i][j] = new Obstacle(2 * (j + 2), i * 6 + 2, LEFT, speeds[i], ' ');
					}
					else
					{
						enemy[i][j] = new Obstacle(2 * (j + 2), i * 6 + 2, LEFT, speeds[i], '<');
					}
				}
			}
		}
	}

	//Destructor for the GameManager
	~GameManager()
	{
		delete player, enemy;
	}

	//Resets the player and enemy positions to generate the next level
	void Reset()
	{
		score = 0;
		gameOver = false;
		player->Reset();
		for (int t = 0; t < 5; t++) {
			for (int q = 0; q < enemyNum; q++)
				enemy[t][q]->Reset();
		}
		MainMenu();
	}

	//Checks the user input for the game controls as well as the pause menu
	void CheckInput()
	{
		if (_kbhit())
		{
			char current = _getch();

			if (current == 'w' || current == 'W')
			{
				player->setDirection(UP);
			}
			else if (current == 's' || current == 'S')
			{
				if (player->getPivotPosY() < height - 3)
					player->setDirection(DOWN);
			}
			else if (current == 'a' || current == 'A')
			{
				player->setDirection(LEFT);
			}
			else if (current == 'd' || current == 'D')
			{
				player->setDirection(RIGHT);
			}
			else if (current == 'p' || current == 'P')
			{
				Pause();
			}
		}
		else
			player->setDirection(STOP);
	}

	//Increases and displays the score, then calls Reset() and increments the array speeds
	void scoreUp()
	{
		score += 100;
		level++;
		TextColor(14);
		goToXY(width / 2 - 8, height / 2);
		cout << "YOU GOT 100 POINTS";
		Beep(1200, 100);
		Sleep(1000);
		player->Reset();

		for (int i = 0; i < 5; i++)
			speeds[i]++;
	}

	//Handles the game logic
	void Logic()
	{
		player->Move(); //Processes player movement

						//Loops that cycle through each enemy on each lane
		for (int t = 0; t < 5; t++) {
			for (int q = 0; q < enemyNum; q++) {

				enemy[t][q]->Move(); //Processes enemy movement

									 //If the player has reached the top of the screen, scoreUp() is called and a new random set of enemies is created
				if (player->getPivotPosY() < 2)
				{
					scoreUp();
					int randomDir;
					for (int i = 0; i < 5; i++)
					{
						randomDir = rand() % 2 + 1;
						for (int j = 0; j < enemyNum; j++)
						{

							if (randomDir == 1)
							{
								if (j % 10 != 0)
								{
									enemy[i][j] = new Obstacle(2 * (j + 2), i * 6 + 2, RIGHT, speeds[i], ' ');
								}
								else
								{
									enemy[i][j] = new Obstacle(2 * (j + 2), i * 6 + 2, RIGHT, speeds[i], '>');
								}
							}
							else if (randomDir == 2)
							{
								if (j % 10 != 0)
								{
									enemy[i][j] = new Obstacle(2 * (j + 2), i * 6 + 2, LEFT, speeds[i], ' ');
								}
								else
								{
									enemy[i][j] = new Obstacle(2 * (j + 2), i * 6 + 2, LEFT, speeds[i], '<');
								}
							}
						}
					}
				}
			}
		}
	}

	//Draws game components to the screen
	void Draw()
	{
		goToXY(0, 0);

		//Cycles through each point of input on the game console
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				//Booleans that are used to check whether the current location has something printed
				bool isPrinted = false;
				bool playerPrinted = false;

				//Prints the finish line
				if (y == 0 || y == height - 1)
				{
					TextColor(119);
					cout << '#';
					isPrinted = true;
					TextColor(0);
				}


				if (!isPrinted)
				{
					//Cycles through each character in the 3x3 character matrix that represents the player
					for (int i = 0; i < 3; i++)
					{
						for (int j = 0; j < 3; j++) {

							//Draws the player if player is located in current location
							if (y == player->bodyPos[i][j].y && x == player->bodyPos[i][j].x)
							{
								TextColor(47 - 5 * (i + 1));
								switch (i) {
								case 0: if (j == 0)cout << "^ ^"; break;
								case 1: if (j == 0)cout << "(|)"; break;
								case 2: if (j == 0)cout << "(|)"; break;
								}
								isPrinted = true;
								playerPrinted = true;
								TextColor(137);
							}

						}
					}
				}

				for (int t = 0; t < 5; t++)
				{
					for (int q = 0; q < enemyNum; q++) {

						//Cycles through each character in the 3x3 character matrix that represents the obstacle
						for (int u = 0; u < 3; u++)
						{
							for (int v = 0; v < 3; v++)
							{
								if (y == enemy[t][q]->bodyPos[u][v].y && x == enemy[t][q]->bodyPos[u][v].x)
								{
									if (!playerPrinted)
									{
										//Colors the characters appropriately
										if (enemy[t][q]->getBodyChar() == '>')
										{
											TextColor(142);
										}
										else
										{
											TextColor(140);
										}
										if (enemy[t][q]->getBodyChar() != ' ') {
											cout << enemy[t][q]->getBodyChar();
											isPrinted = true;
											TextColor(137);

										}

									}

									else if (enemy[t][q]->getBodyChar() != ' ') {
										gameOver = true;
									}
								}
							}
						}
					}
				}

				if (!isPrinted)
				{
					//Draws the safe spaces in between lanes
					if (y >= 5 && (y + 1) % 6 == 0)
					{
						TextColor(7);
						cout << '-';
						isPrinted = true;
						TextColor(137);
					}
				}

				if (!isPrinted) {
					cout << ' ';
					TextColor(137);
				}
			}
			cout << endl;
		}

		//Prints out the player's current score & level at the bottom of the screen
		TextColor(15);
		goToXY(width / 2 - 13, height);
		cout << "Your score: " << score;

		goToXY(width / 2 + 7, height);
		cout << "Level: " << level;

		TextColor(137);
		TextColor(10);

		goToXY(width / 2 - 16, height + 1);
	}

	//Creates the Main Menu for the game and checks input for the menu options
	void MainMenu()
	{
		//Re-initializes speeds
		initSpeeds(speeds);


		goToXY(width / 2 - 13, height / 3);
		TextColor(10);
		cout << "Gator Crossing" << endl;

		TextColor(11);
		goToXY(width / 2 - 13, height / 3 + 2);
		cout << "Press 1 to Start";

		goToXY(width / 2 - 13, height / 3 + 3);
		cout << "Press 2 to Exit";

		goToXY(width / 2 - 13, height / 3 + 6);
		cout << "W: Move forward";
		goToXY(width / 2 - 13, height / 3 + 7);
		cout << "A: Move left";
		goToXY(width / 2 - 13, height / 3 + 8);
		cout << "S: Move backward";
		goToXY(width / 2 - 13, height / 3 + 9);
		cout << "D: Move right";
		goToXY(width / 2 - 13, height / 3 + 10);
		cout << "P: Pause game";

		goToXY(10, height - height / 8);
		cout << "Created by: Jack Hillery, Andres Inguanzo,";
		goToXY(10, height - height / 8 + 1);
		cout << "Nash Dean, Nathan Schwartz, Rishabh Patel,";
		goToXY(25, height - height / 8 + 2);
		cout << "Camryn Comiskey";

		goToXY(0, 1);
		TextColor(2);
		goToXY(15, 1);
		cout << "                   .-\"\"._.\"``\"." << endl;
		goToXY(15, 2);
		cout << "                  /`_-._.-_    `." << endl;
		goToXY(15, 3);
		cout << "                  >(/)--.(/)    |-" << endl;
		goToXY(15, 4);
		cout << "                .\'.-=\"\"\"=.      ` " << endl;
		goToXY(15, 5);
		cout << " .-.--.   _.\'           _      |" << endl;
		goToXY(15, 6);
		cout << "(O/ O) \\-\'     ,    |\\.\'\\)-.__/ " << endl;
		goToXY(15, 7);
		cout << " >      ,      )\\_.-\'`V   .-\' _,/" << endl;
		goToXY(15, 8);
		cout << " \\__..-/|-.__.-\'V    __..\'--;`" << endl;
		goToXY(15, 9);
		cout << "  V-.__..-.\\)..-\'---\'        `" << endl;


		if (_kbhit())
		{
			char current = _getch();
			if (current == '1')
			{
				level = 0;
				playing = true;
			}
			if (current == '2') {
				system("CLS");
				exit(0);
			}
		}
	}

	//References booleans to determine when to loop the game logic/draw functions or when to call GameOver()
	void Run()
	{
	play:
		while (!playing)
		{
			MainMenu();

		}
		while (!gameOver && !paused && playing)
		{

			CheckInput();
			Logic();
			Draw();
		}
		GameOver();

	}

	//Displays "Game Over" and resets the game to the main menu
	void GameOver()
	{
		PlaySound(0, 0, NULL);
		PlaySound("C:\\Users\\Nash\\Downloads\\Shrek-What-Are-You-Doing-In-My-Swamp-sound-effect.wav", NULL, SND_ASYNC);
		Sleep(500);
		system("CLS");
		goToXY(width / 2 - 4, height / 2);
		TextColor(12);
		cout << "GAME OVER" << endl;
		Beep(300, 500);

		goToXY(width / 2 - 18, height / 2 + 1);
		Sleep(2500);
		Reset();
		level = 1;
		playing = false;
		system("CLS");

		MainMenu();
	}

	//Pauses the game and asks the player if they would like to resume
	//Takes in input to check if the player would like to resume
	void Pause()
	{
		goToXY(width / 2 - 4, height / 2);
		TextColor(12);
		cout << "PAUSED" << endl;

		goToXY(width / 2 - 18, height / 2 + 1);
		cout << "Would you like to resume (y)? ";
		while (true)
		{
			char current = _getch();
			if (current == 'y')
			{
				paused = false;
				break;
			}
		}
	}
};


int main()
{
	//PlaySound("C:\\Users\\rdbri\\Downloads\\All-Star-8-Bit-Nick-Copper-Remix-Smash-Mouth.wav", NULL, SND_ASYNC | SND_LOOP);
	//Seeds the rand() function according to system time
	srand(time(NULL));

	//Central "play" loop
play:
	PlaySound("C:\\Users\\Nash\\Downloads\\All-Star-8-Bit-Nick-Copper-Remix-Smash-Mouth.wav", NULL, SND_ASYNC | SND_LOOP);
	initSpeeds(speeds); //First speeds are initialized
	WindowInit(); //Then the console is initialized

				  //GameManager object is created and Run() is called
	GameManager GM(screenHeight, screenWidth);
	GM.Run();

	goto play;

	return 0;
}



